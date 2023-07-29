package com.bojan.lora.domain.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Location {
    private Double latitude;
    private Double longitude;
    private Integer altitude;
    private Map<String, Object> additionalProperties = new HashMap<>();
}