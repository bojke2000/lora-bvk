package com.bojan.lora.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bojan.lora.component.LoraAdeunisDecoder;
import com.bojan.lora.component.LoraCM3020Decoder;
import com.bojan.lora.domain.entity.LoraMts;
import com.bojan.lora.domain.entity.Measurement;
import com.bojan.lora.domain.lora.LoraFPort;
import com.bojan.lora.domain.lora.LoraMtsRequest;
import com.bojan.lora.service.CustomerService;
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
  private LoraCM3020Decoder decoder;
  @Autowired
  private LoraAdeunisDecoder loraAdeunisDecoder;
  @Autowired
  private CustomerService customerService;
  @Autowired
  private MeasurementService measurementService;

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

      var measurement = new Measurement();
      measurement.setDevEui(loraMtsRequest.getDevEUIUplink().getDevEUI());
      java.util.Date date = new java.util.Date();
      java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
      measurement.setReadAt(timestamp);

      switch (loraMtsRequest.getDevEUIUplink().getFPort()) {
        case LoraFPort.FPORT_1:
          int counter = loraAdeunisDecoder.decode(loraMtsRequest.getDevEUIUplink().getPayloadHex());
          measurement.setCounter(counter);
          log.info("loraAdeunisDecoder: counter = " + counter);
          break;

        case LoraFPort.FPORT_14:
          var loraFport14 = decoder.decodeF14(loraMtsRequest.getDevEUIUplink().getPayloadHex());
          measurement.setCounter((int) loraFport14.getCounter());
          log.info(loraFport14.toString());
          break;
        case LoraFPort.FPORT_24:
          var loraFport24 = decoder.decodeF24(loraMtsRequest.getDevEUIUplink().getPayloadHex());
          measurement.setCounter((int) loraFport24.getTotalVolume());
          log.info(loraFport24.toString());
          break;
      }

      return this.measurementService.createMeasurement(measurement);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  @PutMapping("/{id}")
  public LoraMts updateLoraMts(@PathVariable Long id, @RequestBody LoraMts loraMts) {
    return loraMtsService.updateLoraMts(id, loraMts);
  }

  @DeleteMapping("/{id}")
  public void deleteLoraMts(@PathVariable Long id) {
    loraMtsService.deleteLoraMts(id);
  }
}
