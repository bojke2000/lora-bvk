package com.bojan.lora.domain.lora;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class LoraFPort99 extends LoraFPort {
    @JsonIgnore
    public static int FPORT_99 = 99;
    public static byte HEADER_SHUTDOWN = (byte)0x01;
    public static byte REASON_SHUTDOWN_MAGNET = (byte)0x31;
    public static byte REASON_CALIBRATION_TIMEOUT = (byte)0x10;
    byte header;
    LoraFPort24 loradFport24;
}
