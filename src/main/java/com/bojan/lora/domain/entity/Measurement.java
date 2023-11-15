package com.bojan.lora.domain.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
public class Measurement implements Serializable {
  private Long id;
  private Long customerId;
  private Integer counter;
  private Timestamp readAt;

}
