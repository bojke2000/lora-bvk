package com.bojan.lora.service;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class MqttService {

    private MqttClient mqttClient;
    private final Object mqttClientLock = new Object();
    @Value("${mqtt.broker.url}")
    private String brokerUrl; // Replace with your MQTT broker URL
    @Value("${mqtt.topic}")
    private String topic; // Replace with the topic you want to publish to

    public MqttService() {}

    public void connect() {
        synchronized (mqttClientLock) {
            if (mqttClient != null && mqttClient.isConnected()) {
                return;
            }
            closeClientQuietly();

            String clientId = MqttClient.generateClientId();
            try {
                MqttClient newClient = new MqttClient(brokerUrl, clientId);
                MqttConnectOptions connectOptions = new MqttConnectOptions();
                connectOptions.setCleanSession(true);
                connectOptions.setAutomaticReconnect(true);
                newClient.connect(connectOptions);
                mqttClient = newClient;
            } catch (MqttException e) {
                log.error("Unable to connect MQTT client to broker {}", brokerUrl, e);
                mqttClient = null;
            }
        }
    }

    public void publish(String message) {
        if (mqttClient == null || !mqttClient.isConnected()) {
            connect();
        }

        if (mqttClient == null || !mqttClient.isConnected()) {
            log.warn("Skipping publish because MQTT client is not connected");
            return;
        }

        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
            mqttClient.publish(topic, mqttMessage);
            log.debug("Message published to topic {}", topic);
        } catch (MqttException e) {
            log.error("Failed to publish message to topic {}", topic, e);
        }
    }

    public void stop() {
        synchronized (mqttClientLock) {
            closeClientQuietly();
            mqttClient = null;
        }
    }

    @PreDestroy
    public void onShutdown() {
        stop();
    }

    private void closeClientQuietly() {
        if (mqttClient == null) {
            return;
        }

        try {
            if (mqttClient.isConnected()) {
                mqttClient.disconnect();
            }
        } catch (MqttException e) {
            log.warn("Error disconnecting MQTT client", e);
        } finally {
            try {
                mqttClient.close();
            } catch (MqttException e) {
                log.warn("Error closing MQTT client", e);
            }
        }
    }
}
