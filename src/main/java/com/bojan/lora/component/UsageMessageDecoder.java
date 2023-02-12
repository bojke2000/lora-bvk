package com.bojan.lora.component;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UsageMessageDecoder {

  public static int decodeUsageMessage(byte[] message) {
    // Flip the byte order from big-endian to little-endian
    ByteBuffer bb = ByteBuffer.wrap(message);
    bb.order(ByteOrder.BIG_ENDIAN);

    // Read the 32-bit integer representing the liter count
    int literCount = bb.getInt();

    // Return the liter count
    return literCount;
  }

  public static byte[] hexStringToByteArray(String hexString) {
    int len = hexString.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                             + Character.digit(hexString.charAt(i+1), 16));
    }
    return data;
}

  public static void main(String[] args) {
    byte[] message = new byte[] { (byte) 0x80, 0x0A, 0x00, 0x00 };
    byte[] msg = hexStringToByteArray("54070000");
    int literCount = decodeUsageMessage(msg);
    System.out.println("Liter count: " + literCount);
  }
}
