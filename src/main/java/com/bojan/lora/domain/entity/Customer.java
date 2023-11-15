package com.bojan.lora.domain.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Customer implements Serializable {
  public static final int DEVICE_TYPA_PULSE = 1;
  public static final int DEVICE_TYPA_INSA = 2;
  public static final int DEVICE_TYPA_SOLAR = 3;
}
