package com.bojan.lora.domain.lora;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class LoraFPort50 extends LoraFPort {
    @JsonIgnore
    public static int FPORT_50 = 50;
    public static byte HEADER_VOLUME = (byte)0x00;
    public static byte HEADER_DATETIME = (byte)0x01;
    public static byte HEADER_INTERVAL = (byte)0x02;
//    public static byte HEADER_REPORTING_INTERVAL = (byte)0x01;
//    public static byte HEADER_STATUS_INTERVAL = (byte)0x04;
//    public static byte HEADER_COUNTER = (byte)0x08;
//    public static byte HEADER_TEMPERATURE_THRESHOLD = (byte)0x10;
//    public static byte HEADER_FUNCTIONS = (byte)0x80;
    String headerInReadableFormat;
    byte header;
    String data1;
    String data2;
    Integer payload;
}
