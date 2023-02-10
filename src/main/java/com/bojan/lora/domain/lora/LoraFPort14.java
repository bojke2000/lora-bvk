package com.bojan.lora.domain.lora;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class LoraFPort14 extends LoraFPort {
    @JsonIgnore
    public static int FPORT_14 = 14;
    long counter;
}
