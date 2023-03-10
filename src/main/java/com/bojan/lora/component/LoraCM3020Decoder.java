package com.bojan.lora.component;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.Base64;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import com.bojan.lora.domain.dto.LoraFPort50Dto;
import com.bojan.lora.domain.lora.LoraFPort14;
import com.bojan.lora.domain.lora.LoraFPort24;
import com.bojan.lora.domain.lora.LoraFPort50;
import com.bojan.lora.domain.lora.LoraFPort51;
import com.bojan.lora.domain.lora.LoraFPort99;
import com.bojan.lora.exception.LoraException;

import lombok.extern.slf4j.Slf4j;
import nl.sedit.encoders.BCD;

@Component
@Slf4j
public class LoraCM3020Decoder extends CommonDecoder {

    public LoraFPort14 decodeF14(String messageBase64) {
        var loraF14 = new LoraFPort14();
        byte[] bytes = hexStringToByteArray(messageBase64);
        byte[] rezz = flipBytes(bytes, 0, 4);
        loraF14.setCounter(BCD.decode(rezz).intValue());

        return loraF14;
    }

    public LoraFPort24 decodeF24(String messageBase64) {
        try {
            // String hexString = getHexString(messageBase64);
            return decodeFPort24Core(messageBase64);
        } catch (Exception e) {
            LoraCM3020Decoder.log.error(e.getMessage());
            throw new LoraException(e);
        }
    }

    public String decodeRegister(String message, int startByte) {
        String p4 = flipHexaChars(message, startByte, 2);
        String p3 = flipHexaChars(message, startByte + 2, 2);
        String p2 = flipHexaChars(message, startByte + 4, 2);
        String p1 = flipHexaChars(message, startByte + 6, 2);
        StringBuilder register = new StringBuilder(p1).append(p2).append(p3).append(p4);
        return register.toString();
    }

    public String flipHexaChars(String message, int startChar, int n) {
        StringBuilder sb = new StringBuilder();
        for (int ii = n; ii > 0; ii = ii - 2) {
            sb.append(message.substring(startChar + ii - 2, startChar + ii));
        }
        return sb.toString();
    }

    public LoraFPort24 decodeFPort24Core(String messageBase64) throws DecoderException {
        var loraFPort24 = new LoraFPort24();
        var hexString = getHexString(messageBase64);

        // byte[] bytes = Base64.getDecoder().decode(messageBase64);
        byte[] bytes = hexStringToByteArray(messageBase64);
        byte[] alarmLE = new byte[2];
        var alarmBE = flipBytes(bytes, 0, 2);
        printBites(alarmBE);
        ByteBuffer buffer = ByteBuffer.wrap(alarmBE);
        short alarms = buffer.getShort();
        String alarm = printBites(buffer.array());
        System.out.printf("Alarms %s \n", alarm);
        loraFPort24.setAlarms(alarms);
        loraFPort24.setDateTime(decodeDatetime(hexString, 4));
        byte[] battery = new byte[1];
        battery[0] = bytes[6];
        loraFPort24.setBattery(Short.valueOf(Hex.encodeHexString(battery), 16) / 2);
        byte[] temperature = new byte[1];
        temperature[0] = bytes[7];
        loraFPort24.setTemperature(Short.valueOf(Hex.encodeHexString(temperature), 16));
        var volume = flipBytes(bytes, 8, 4);
        loraFPort24.setTotalVolume(BCD.decode(volume).intValue());
        var reverse = flipBytes(bytes, 12, 4);
        loraFPort24.setReverseVolume(BCD.decode(reverse).intValue());
        loraFPort24.setReadDate(decodeDate(hexString, 32));

        return loraFPort24;
    }

    public String encodefPort50(LoraFPort50 loraFPort50) throws LoraException {
        try {
            ByteBuffer encodefPort50 = ByteBuffer.allocate(0);
            switch (loraFPort50.getHeaderInReadableFormat()) {
                case LoraFPort50Dto.HEADER_TOTAL_VOLUME:
                    encodefPort50 = ByteBuffer.allocate(9);
                    encodefPort50.put(LoraFPort50.HEADER_VOLUME);
                    var totalValue = Integer.parseInt(loraFPort50.getData1());
                    encodefPort50.put(getBCDBytesLE(totalValue, 4, true));
                    var reverseValue = Integer.parseInt(loraFPort50.getData2());
                    encodefPort50.put(getBCDBytesLE(reverseValue, 4, true));
                    break;
                case LoraFPort50Dto.HEADER_PERIODIC_INTERVAL:
                    encodefPort50 = ByteBuffer.allocate(5);
                    encodefPort50.put(LoraFPort50.HEADER_INTERVAL);
                    var inteval = Short.parseShort(loraFPort50.getData1());
                    encodefPort50.put(flipBytes(shortToBytes(inteval), 0, 2));
                    var status = Short.parseShort(loraFPort50.getData2());
                    encodefPort50.put(flipBytes(shortToBytes(status), 0, 2));
                    break;
                case LoraFPort50Dto.HEADER_DATETIME:
                    encodefPort50 = ByteBuffer.allocate(5);
                    encodefPort50.put(LoraFPort50.HEADER_DATETIME);

                    String dtas = loraFPort50.getData1();
                    var dateTime = LocalDateTime.parse(dtas, formatter);
                    short time = (short) this.packTimeRTC2F(0, dateTime.getHour(), dateTime.getMinute());
                    encodefPort50.put(this.shortToBytes(time));
                    short date = (short) this.packDateRTC2F(dateTime.getYear(), dateTime.getMonth().getValue(),
                            dateTime.getDayOfMonth());
                    encodefPort50.put(this.shortToBytes(date));
                    break;
            }
            printBites(encodefPort50.array());
            return getBase64String(encodefPort50.array());
        } catch (Exception e) {
            LoraCM3020Decoder.log.error(e.getMessage());
            throw new LoraException(e.getMessage());
        }
    }

    private byte[] getBCDBytesLE(int totalValue, int length, boolean flip) {
        var bcd = BCD.encode(totalValue);
        if (flip) {
            bcd = flipBytes(bcd, 0, bcd.length);
        }
        byte[] rezz = new byte[length];
        for (int i = 0; i < bcd.length; i++) {
            rezz[i] = bcd[i];
        }

        return rezz;
    }

    public String encodefPort51(LoraFPort51 loraFPort51) {
        var msg = new byte[1];
        msg[0] = loraFPort51.getHeader();
        return getBase64String(msg);
    }

    public LoraFPort99 decodeF99(String messageBase64) {
        try {
            var loraFPort99 = new LoraFPort99();
            String hexString = getHexString(messageBase64);

            if (hexString.substring(0, 2).equals("01")) {
                String reason = hexString.substring(2, 4);
                loraFPort99.setHeader(reason.equals("31") ? LoraFPort99.REASON_SHUTDOWN_MAGNET
                        : LoraFPort99.REASON_CALIBRATION_TIMEOUT);
                loraFPort99.setLoradFport24(decodeFPort24Core(hexString.substring(4)));
            } else if (hexString.substring(0, 2).equals("00")) {
                // FIXME - NOT YET IMPLEMENTED.
            }

            return loraFPort99;
        } catch (Exception e) {
            this.log.error(e.getMessage());
            throw new LoraException(e);
        }
    }

    private String getHexString(String messageBase64) {
        // byte[] result = Base64.getDecoder().decode(messageBase64);
        byte[] result = hexStringToByteArray(messageBase64);
        String hexString = Hex.encodeHexString(result).toUpperCase();
        System.out.println(hexString);
        return hexString;
    }

    private String getBase64String(byte[] hex) {
        return Base64.getEncoder().encodeToString(hex);
    }

    public String toLittleEndian(final String hex) {
        var hexLittleEndian = new StringBuilder();
        for (int i = hex.length() - 2; i >= 0; i -= 2) {
            hexLittleEndian.append(hex, i, i + 2);
        }

        return hexLittleEndian.toString();
    }

    // function that rotates s towards left by d
    private String leftrotate(String str, int d) {
        String ans = str.substring(d) + str.substring(0, d);
        return ans;
    }

    // function that rotates s towards right by d
    private String rightrotate(String str, int d) {
        return leftrotate(str, str.length() - d);
    }

    public int packTimeRTC2F(int valid, int hour, int minute) {
        return (valid & 0x8000) | ((hour << 8) & 0x1F00) |
                (minute & 0x3F);
    }

    int packDateRTC2F(int year, int month, int day) {
        return (((year % 100) << 9) & 0xF000) | (((year % 100) << 5) & 0x00E0) |
                ((month << 8) & 0x0F00) | (day & 0x001F);
    }

    public static void main(String[] args) {
        try {
            var decoder = new LoraCM3020Decoder();
            // System.out.println(decoder.decodeF14("62761200"));
            // System.out.println(decoder.decodeF14("57140000"));
            // System.out.println(decoder.decodeF14("51598699"));
            // System.out.println(decoder.decodeF14("29289999"));
            // System.out.println(decoder.decodeF14("98113364"));
            // System.out.println(decoder.decodeF14("43992934"));

            // System.out.println(decoder.decodeF24("91f0280dd115de35981133644399293448b7"));
            System.out.println(decoder.decodeF24("0030120e1225481429289999606423008cdd"));

            // System.out.println(decoder.decodeF24("ADguCAEhSBRkAAAACgAAAAEh"));
            // System.out.println(decoder.decodeF24("8y0NABF/QbQFAAAAAQAAABB/"));
            // decoder = new LoraCM3020Decoder();
            //// System.out.println(decoder.decodeF24("kigiBQkhSBQ3AAAABgAAAAYh"));
            //// System.out.println(decoder.decodeFPort24Core("922805120821481437000000060000000621"));
            //

            // var loraFPort50 = new LoraFPort50();
            // loraFPort50.setHeader(LoraFPort50.HEADER_VOLUME);
            // loraFPort50.setHeaderInReadableFormat("0");
            // loraFPort50.setData1("452");
            // loraFPort50.setData2("10");
            // System.out.println(decoder.encodefPort50(loraFPort50));

            // var loraFPort501 = new LoraFPort50();
            // loraFPort501.setHeader(LoraFPort50.HEADER_INTERVAL);
            // loraFPort501.setHeaderInReadableFormat("2");
            // loraFPort501.setData1("10");
            // loraFPort501.setData2("160");
            // System.out.println(decoder.encodefPort50(loraFPort501));

            // loraFPort501 = new LoraFPort50();
            // loraFPort501.setHeader(LoraFPort50.HEADER_DATETIME);
            // loraFPort501.setHeaderInReadableFormat("1");
            // loraFPort501.setData1("18.03.2022 08:41");
            // String encoded = decoder.encodefPort50(loraFPort501);
            // System.out.println(encoded);
            // System.out.println(decoder.decodeDatetime(decoder.getHexString(encoded), 2));

            // var loraFPort50 = new LoraFPort50();
            // loraFPort50.setHeader(LoraFPort50.HEADER_DATETIME);
            // loraFPort50.setHeaderInReadableFormat("1");
            // loraFPort50.setData1("08.01.2016 18:46");
            // System.out.println(decoder.encodefPort50(loraFPort50));

            // var sb = new StringBuilder();
            // short d = (short)decoder.packTimeRTC2F(0, 18, 46);
            // var f = Hex.encodeHex(decoder.shortToBytes(d));
            // sb.append(f);
            // short a = (short)decoder.packDateRTC2F(2016, 1, 8);
            // var c = Hex.encodeHex(decoder.shortToBytes(a));
            // sb.append(c);

            // loraFPort50.setData1("08.01.2016 18:05");
            // var out = decoder.encodefPort50(loraFPort50);
            // System.out.println(out);
            // System.out.println(decoder.decodeDatetime(decoder.getHexString(out), 0));
            //
            // double a = 2.46;
            // System.out.println(a);
            // System.out.printf("%f\n", a);
            // System.out.printf("%.2f\n", a);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
