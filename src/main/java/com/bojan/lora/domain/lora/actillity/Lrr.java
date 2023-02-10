package com.bojan.lora.domain.lora.actillity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Lrr {
    private String Lrrid;
    private int Chain;
    private int LrrRSSI;
    private double LrrSNR;
    private int LrrESP;
    private String ForwardingNetID;
    private String ForwardingNSID;
}