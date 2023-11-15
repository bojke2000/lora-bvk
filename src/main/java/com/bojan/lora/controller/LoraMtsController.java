package com.bojan.lora.controller;

import com.bojan.lora.domain.dto.MqttUplinkMessage;
import com.bojan.lora.domain.entity.LoraMts;
import com.bojan.lora.domain.entity.Measurement;
import com.bojan.lora.domain.lora.LoraMtsRequest;
import com.bojan.lora.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.json.JsonObjectMapper;
import org.springframework.integration.support.json.JsonObjectMapperProvider;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/loramts")
public class LoraMtsController {
  @Autowired
  private MqttService mqttService;
  private JsonObjectMapper objectMapper = JsonObjectMapperProvider.newInstance();

  @GetMapping
  public List<LoraMts> getAllLoraMts() {
    return new ArrayList<LoraMts>();
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

      MqttUplinkMessage mqttUplinkMessage = new MqttUplinkMessage();
      mqttUplinkMessage.setDevEUI(devEUI);
      mqttUplinkMessage.setFPort(loraMtsRequest.getDevEUIUplink().getFPort());
      mqttUplinkMessage.setData(loraMtsRequest.getDevEUIUplink().getPayloadHex());
      mqttService.publish(objectMapper.toJson(mqttUplinkMessage));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return null;
  }
}
