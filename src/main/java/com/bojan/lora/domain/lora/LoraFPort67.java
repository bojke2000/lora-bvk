package com.bojan.lora.domain.lora;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class LoraFPort67 extends LoraFPort {
    @JsonIgnore
    public static int FPORT_51 = 51;
    public static byte HEADER_UPDATE = (byte)0xFF;
    byte header;
}
