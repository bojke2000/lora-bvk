package com.bojan.lora.component;

import org.springframework.stereotype.Component;

import com.bojan.lora.exception.LoraException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoraAdeunisDecoder {

  private static final byte FRAME_CODE_46 = 0x46;
  private static final byte FRAME_CODE_30 = 0x30;

  public int decode(String data) {
    try {
      var header = Integer.parseInt(data.substring(0, 2), 16);
      switch (header) {
        case FRAME_CODE_46:
          return decodeMessage46(data);
        case FRAME_CODE_30:
          return decodeMessage30(data);
      }
    } catch (Exception e) {
      throw new LoraException(e.getMessage());
    }

    return -1;
  }

  public int decodeMessage46(String data) {
    try {
      var header = Integer.parseInt(data.substring(0, 2), 16);
      if (header != FRAME_CODE_46) {
        throw new IllegalArgumentException("Invalid frame code: " + header);
      }

      var status = Integer.parseInt(data.substring(2, 4), 16);
      int counterA = Integer.parseInt(data.substring(4, 12), 16);
      System.out.println(counterA);
      int counterB = Integer.parseInt(data.substring(12, 20), 16);
      System.out.println(counterB);

      return counterA;

    } catch (Exception e) {
      throw new LoraException(e.getMessage());
    }
  }

  public int decodeMessage30(String data) {
    try {
      var header = Integer.parseInt(data.substring(0, 2), 16);
      if (header != FRAME_CODE_30) {
        throw new IllegalArgumentException("Invalid frame code: " + header);
      }

      var status = Integer.parseInt(data.substring(2, 4), 16);
      var alarms = Integer.parseInt(data.substring(4, 6), 16);
      printBits(alarms);
      var maxValueA = Integer.parseInt(data.substring(6, 10), 16);
      log.info("{}", maxValueA);
      var maxValueB = Integer.parseInt(data.substring(10, 14), 16);
      log.info("{}", maxValueB);

      var minValueA = Integer.parseInt(data.substring(14, 18), 16);
      log.info("{}", minValueA);
      var minValueB = Integer.parseInt(data.substring(18, 22), 16);
      log.info("{}", minValueB);

      return maxValueA;
      
    } catch (Exception e) {
      throw new LoraException(e.getMessage());
    }
  }

  public static void printBits(int value) {
    for (int i = 31; i >= 0; i--) {
      System.out.print((value >> i) & 1);
    }
    System.out.println();
  }

  public static void main(String[] args) {
    String message46 = "462000015C4F0000F74A";
    String message30 = "30e0000c00080005000700";
    var decoder = new LoraAdeunisDecoder();
    // System.out.println(decoder.decodeMessage46(message46));
    System.out.println(decoder.decodeMessage30(message30));

  }
}
