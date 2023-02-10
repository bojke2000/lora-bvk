package com.bojan.lora.domain.lora.actillity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CustomerData {
    private Loc loc;
    private Alr alr;
    private String[] tags;
    private Doms[] doms;
    private String name;
}