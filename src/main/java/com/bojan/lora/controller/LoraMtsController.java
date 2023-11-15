package com.bojan.lora.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bojan.lora.domain.entity.Customer;
import com.bojan.lora.domain.entity.LoraMts;
import com.bojan.lora.domain.entity.Measurement;
import com.bojan.lora.domain.lora.LoraMtsRequest;
import com.bojan.lora.service.CustomerService;
import com.bojan.lora.service.DecoderService;
import com.bojan.lora.service.LoraMtsService;
import com.bojan.lora.service.MeasurementService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/loramts")
public class LoraMtsController {

  @Autowired
  private LoraMtsService loraMtsService;
  @Autowired
  private CustomerService customerService;
  @Autowired
  private MeasurementService measurementService;
  @Autowired
  private DecoderService decoderService;

  @GetMapping
  public List<LoraMts> getAllLoraMts() {
    return new ArrayList<LoraMts>();
  }

  @GetMapping("/{id}")
  public LoraMts getLoraMtsById(@PathVariable Long id) {
    return loraMtsService.getLoraMtsById(id);
  }

  @PostMapping
  public Measurement createLoraMts(@RequestBody LoraMtsRequest loraMtsRequest) {
    try {
      log.info("MTS Actillity LoRaWAN message: {}", loraMtsRequest.toString());

      if (loraMtsRequest.getDevEUIUplink() == null) {
        log.error("DevEUIUplink is null " + loraMtsRequest.toString());
        return null;
      }

      var devEUI = loraMtsRequest.getDevEUIUplink().getDevEUI();
      if (devEUI == null) {
        log.error("devEUI doesnt exist for " + loraMtsRequest.toString());
        return null;
      }

      var customerOpt = this.customerService.findByDevEui(devEUI);
      Customer customer = customerOpt.orElse(null);
      if (customer == null) {
        log.error("Cannot find customer with devEUI = " + loraMtsRequest.getDevEUIUplink().getDevEUI());
        return null;
      }

      var measurement = new Measurement();
      // measurement.setDevEui(loraMtsRequest.getDevEUIUplink().getDevEUI());
      measurement.setCustomerId(customer.getId());
      java.util.Date date = new java.util.Date();
      java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
      measurement.setReadAt(timestamp);
      measurement.setCounter(
          this.decoderService.decode(loraMtsRequest.getDevEUIUplink().getPayloadHex(), customer.getDeviceType(),
              loraMtsRequest.getDevEUIUplink().getFPort()));

      return this.measurementService.createMeasurement(measurement);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return null;
  }
}
