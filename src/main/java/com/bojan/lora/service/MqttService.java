package com.bojan.lora.service;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;

public class MqttService {

    private MqttClient mqttClient;
    @Value("${mqtt.broker.url}")
    private String brokerUrl = "tcp://mqtt.eclipse.org:1883"; // Replace with your MQTT broker URL
    @Value("${mqtt.topic}")
    private String topic = "test/topic"; // Replace with the topic you want to publish to
    private String message = "Hello, MQTT!"; // Replace with the message you want to publish

    public MqttService() {
        String clientId = MqttClient.generateClientId();

        try {
            mqttClient = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);

            mqttClient.connect(connectOptions);


        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String message) {
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttClient.publish(topic, mqttMessage);

            System.out.println("Message published: " + message);
        } catch (Exception e) {
        }
    }

    public void stop() {
        try {
            if (mqttClient != null) {
                mqttClient.disconnect();
                mqttClient.close();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
