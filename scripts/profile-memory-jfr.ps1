param(
    [int]$Requests = 2000,
    [int]$SampleEvery = 400,
    [int]$Port = 7788,
    [int]$HeapMb = 256,
    [int]$StartupTimeoutSec = 120
)

$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot
$targetDir = Join-Path $root "target"
$jar = Join-Path $targetDir "lora-0.0.1-SNAPSHOT.jar"
$jfrPath = Join-Path $targetDir "memory-profile.jfr"
$appOutLog = Join-Path $targetDir "memory-profile-app.out.log"
$appErrLog = Join-Path $targetDir "memory-profile-app.err.log"

Write-Host "Building JAR..."
& "$root\mvnw.cmd" -q -DskipTests package

if (-not (Test-Path $jar)) {
    throw "Could not find JAR at $jar"
}

if (Test-Path $jfrPath) {
    Remove-Item $jfrPath -Force
}
if (Test-Path $appOutLog) {
    Remove-Item $appOutLog -Force
}
if (Test-Path $appErrLog) {
    Remove-Item $appErrLog -Force
}

$javaArgs = @(
    "-Xms${HeapMb}m",
    "-Xmx${HeapMb}m",
    "-XX:StartFlightRecording=filename=$jfrPath,settings=profile,dumponexit=true",
    "-jar", $jar,
    "--server.port=$Port",
    "--mqtt.broker.url=tcp://127.0.0.1:1884",
    "--mqtt.topic=application/mts",
    "--logging.level.root=ERROR"
)

Write-Host "Starting application..."
$proc = Start-Process -FilePath "java" -ArgumentList $javaArgs -PassThru -RedirectStandardOutput $appOutLog -RedirectStandardError $appErrLog

try {
    $started = $false
    for ($i = 0; $i -lt $StartupTimeoutSec; $i++) {
        if ($proc.HasExited) {
            $errTail = if (Test-Path $appErrLog) { (Get-Content $appErrLog -Tail 40) -join "`n" } else { "" }
            throw "Application exited during startup. Last stderr lines:`n$errTail"
        }

        Start-Sleep -Milliseconds 800
        try {
            $resp = Invoke-WebRequest -Uri "http://127.0.0.1:$Port/api/loramts" -Method Get -TimeoutSec 1
            if ($resp.StatusCode -eq 200) {
                $started = $true
                break
            }
        } catch {
        }
    }

    if (-not $started) {
        $outTail = if (Test-Path $appOutLog) { (Get-Content $appOutLog -Tail 30) -join "`n" } else { "" }
        $errTail = if (Test-Path $appErrLog) { (Get-Content $appErrLog -Tail 30) -join "`n" } else { "" }
        throw "Application did not start in time on port $Port. Last stdout lines:`n$outTail`nLast stderr lines:`n$errTail"
    }

    $payload = '{"DevEUI_uplink":{"DevEUI":"A840411D1134ABCD","FPort":1,"payload_hex":"01020304"}}'
    $rows = @()

    Write-Host "Sending $Requests requests..."
    foreach ($i in 1..$Requests) {
        try {
            Invoke-RestMethod -Uri "http://127.0.0.1:$Port/api/loramts" -Method Post -ContentType "application/json" -Body $payload -TimeoutSec 3 | Out-Null
        } catch {
        }

        if ($i % $SampleEvery -eq 0) {
            & jcmd $proc.Id GC.run | Out-Null
            Start-Sleep -Milliseconds 300
            $heapInfo = & jcmd $proc.Id GC.heap_info 2>&1 | Out-String
            $usedKb = [int]([regex]::Match($heapInfo, "used\s+(\d+)K").Groups[1].Value)
            $p = Get-Process -Id $proc.Id -ErrorAction SilentlyContinue
            $rows += [PSCustomObject]@{
                request     = $i
                heap_used_mb = [math]::Round($usedKb / 1024, 2)
                working_set_mb = [math]::Round($p.WorkingSet64 / 1MB, 2)
                private_mem_mb = [math]::Round($p.PrivateMemorySize64 / 1MB, 2)
            }
        }
    }

    Write-Host ""
    Write-Host "Memory samples (after forced GC):"
    $rows | Format-Table -AutoSize
    Write-Host ""
    Write-Host "JFR recording written to:"
    Write-Host $jfrPath
    Write-Host "Open it in JDK Mission Control (JMC) for leak analysis."
    Write-Host "Application logs:"
    Write-Host $appOutLog
    Write-Host $appErrLog
}
finally {
    if (Get-Process -Id $proc.Id -ErrorAction SilentlyContinue) {
        Stop-Process -Id $proc.Id -Force
    }
}
