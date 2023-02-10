package com.bojan.lora.domain.lora;
import com.bojan.lora.domain.lora.actillity.DevEUIUplink;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoraMtsRequest {
     @JsonProperty("DevEUI_uplink")
     private DevEUIUplink devEUIUplink;  
}
