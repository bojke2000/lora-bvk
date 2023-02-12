package com.bojan.lora.component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonDecoder {
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    protected DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    protected static void printBits(int value) {
        for (int i = 31; i >= 0; i--) {
            System.out.print((value >> i) & 1);
        }
        System.out.println();
    }

    protected void printBites(byte[] bytes) {
        String out = new String();
        for (byte b : bytes) {
            out += Integer.toBinaryString(b & 255 | 256).substring(1) + " ";
        }
        System.out.println(out);
    }

    public byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    protected byte[] flipBytes(byte[] bytes, int start, int n) {
        byte[] rezz = new byte[n];

        int j = 0;
        for (int i = start + n - 1; i >= start; i--, j++) {
            rezz[j] = bytes[i];
        }

        return rezz;
    }

    public String flipChars(String message, int startChar, int n) {
        StringBuilder sb = new StringBuilder();
        for (int ii=n; ii > 0; ii = ii -2) {
           sb.append(message.substring(startChar + ii -2, startChar + ii));
        }
        return sb.toString();
    }

    protected String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    protected byte[] shortToBytes(final short data) {
        return new byte[] { (byte) (data & 0xff), (byte) ((data >> 8) & 0xff) };
    }

    protected LocalDateTime decodeDatetime(String msg, int startChar) {
        LocalDateTime rezz = null;

        var dt1 = msg.substring(startChar, startChar + 2);
        var dt2 = msg.substring(startChar + 2, startChar + 4);
        int minutes = Integer.parseInt(dt1, 16) & 0x3F;
        int hours = Integer.parseInt(dt2, 16) & 0x3F;

        var dt3 = msg.substring(startChar + 4, startChar + 6);
        var dt4 = msg.substring(startChar + 6, startChar + 8);
        var firstTwoBytes = Integer.parseInt(dt3, 16);
        var secondTwoBytes = Integer.parseInt(dt4, 16);

        int day = (0x1F & firstTwoBytes);
        int month = (0x0F & secondTwoBytes);
        int year = ((firstTwoBytes & 0xE0) >> 5) | ((secondTwoBytes & 0xF0) >> 1);

        var result = new StringBuilder((day > 9) ? String.valueOf(day) : "0" + day)
                .append(".").append((month > 9) ? String.valueOf(month) : "0" + month)
                .append(".").append(year < 10 ? "200" : "20").append(year)
                .append(" ").append((hours > 9) ? String.valueOf(hours) : "0" + hours)
                .append(":").append((minutes > 9) ? String.valueOf(minutes) : "0" + minutes);

        try {
            rezz = LocalDateTime.parse(result.toString(), formatter);
        } catch (Exception e) {
            log.error("Cannot parse date for message: {}", msg);
            rezz = LocalDateTime.of(LocalDate.EPOCH, LocalTime.NOON);
        }

        return rezz;
    }

    protected LocalDate decodeDate(String msg, int startChar) {
        LocalDate rezz = null;
        var dt3 = msg.substring(startChar, startChar + 2);
        var dt4 = msg.substring(startChar + 2, startChar + 4);
        var firstTwoBytes = Integer.parseInt(dt3, 16);
        var secondTwoBytes = Integer.parseInt(dt4, 16);

        int day = (byte) (0x1F & firstTwoBytes);
        int month = (byte) (0x0F & secondTwoBytes);

        var year1 = secondTwoBytes & 0xF0;
        year1 = year1 >>> 1;
        var year2 = firstTwoBytes >>> 5;
        int year = year1 + year2;

        var result = new StringBuilder((day > 9) ? String.valueOf(day) : "0" + day)
                .append(".").append((month > 9) ? String.valueOf(month) : "0" + month)
                .append(".").append("20").append(year);

        try {
            rezz = LocalDate.parse(result.toString(), dateFormatter);
        } catch (Exception e) {
            rezz = LocalDate.EPOCH;
        }

        return rezz;
    }

}
