package com.bojan.lora.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@Entity
public class Measurement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "dev_eui", referencedColumnName = "dev_eui", nullable = false)
  private Customer customer;

  private Integer counter;

  private Timestamp readAt;

}
