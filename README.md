# lora-bvk
LoraWAN protocol za Beogradski Vodovod

## Memory Profiling

Run repeatable memory profiling with Java Flight Recorder:

```powershell
.\scripts\profile-memory-jfr.ps1
```

Optional parameters:

```powershell
.\scripts\profile-memory-jfr.ps1 -Requests 5000 -SampleEvery 500 -HeapMb 384 -Port 7788
```
