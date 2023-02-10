package com.bojan.lora.domain.lora;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
public class LoraFPort24 extends LoraFPort {
    @JsonIgnore
    public static int FPORT_24 = 24;
    int alarms;
    LocalDateTime dateTime;
    int battery;
    int temperature;
    long totalVolume;
    long reverseVolume;
    LocalDate readDate;
}
