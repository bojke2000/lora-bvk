package com.bojan.lora.domain.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TxInfo {
    private Integer frequency;
    private Integer dr;
    private Map<String, Object> additionalProperties = new HashMap<>();
}
