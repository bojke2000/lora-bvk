package com.bojan.lora.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class MqttUplinkMessage {
    private String applicationID;
    private String applicationName;
    private String deviceName;
    private String devEUI;
    private List<RxInfo> rxInfo = null;
    private TxInfo txInfo;
    private Boolean adr;
    @JsonProperty("fCnt")
    private Integer fCnt;
    @JsonProperty("fPort")
    private Integer fPort;
    private String data;
    private Map<String, Object> additionalProperties = new HashMap<>();
}
