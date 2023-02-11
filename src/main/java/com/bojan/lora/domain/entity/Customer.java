package com.bojan.lora.domain.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Entity(name = "customers")
public class Customer implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long deviceId;

  private String reg;

  @Column(name = "dev_eui")
  private String devEui;

  private String address;

  private String profile;

  private String note;

  @OneToMany
  @JoinColumn(name="dev_eui", referencedColumnName = "dev_eui")
  private List<Measurement> measurements;

}
