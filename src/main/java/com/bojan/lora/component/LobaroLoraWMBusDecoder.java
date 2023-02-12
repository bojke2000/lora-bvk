package com.bojan.lora.component;

import org.springframework.stereotype.Component;

import nl.sedit.encoders.BCD;

@Component
public class LobaroLoraWMBusDecoder extends CommonDecoder {

  private static final int PORT_1 = 1;
  private static final int PORT_11 = 11;

  public int decode(String payload, int port) {
    int counter = 0;
    switch (port) {
      case PORT_1:
        counter = decodePort1(payload);
        break;
      case PORT_11:
        counter = decodePort11(payload);
        break;
      default:
        System.out.println("Unknown port: " + port);
        break;
    }

    return counter;
  }

  private int decodePort1(String payload) {
    return 0;
  }

  private int decodePort11(String payload) {
    String pls1 = flipChars(payload, 32, 8);
    byte[] bytes = hexStringToByteArray(pls1);
    return BCD.decode(bytes).intValue();
  }

  public static void main(String[] args) {
    String message = "4469267856341203077A100000200C13270485020C1331021100046D050CC1164C1300048502426CC11501FD170032220201";
    var decoder = new LobaroLoraWMBusDecoder();
    decoder.decode(message, LobaroLoraWMBusDecoder.PORT_11);
  }
}