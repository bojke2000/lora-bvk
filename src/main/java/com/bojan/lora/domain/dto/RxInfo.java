package com.bojan.lora.domain.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RxInfo {

    private String gatewayID;
    private String uplinkID;
    private String name;
    private String time;
    private Integer rssi;
    private Double loRaSNR;
    private Location location;
    private Map<String, Object> additionalProperties = new HashMap<>();

}
