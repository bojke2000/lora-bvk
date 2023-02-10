package com.bojan.lora.domain.dto;

import lombok.Data;

@Data
public class LoraFPort50Dto {
    public static final String HEADER_TOTAL_VOLUME = "0";
    public static final String HEADER_DATETIME = "1";
    public static final String HEADER_PERIODIC_INTERVAL = "2";
    private String deviceType;
    private String fPort;
    private String applicationKey;
    private String devEUI;
    private String header1;
    private String data1;
    private String header2;
    private String data2;
}
