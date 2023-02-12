package com.bojan.lora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bojan.lora.component.LobaroLoraWMBusDecoder;
import com.bojan.lora.component.LoraAdeunisDecoder;
import com.bojan.lora.component.LoraCM3020Decoder;
import com.bojan.lora.domain.entity.Customer;
import com.bojan.lora.domain.lora.LoraFPort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DecoderService {
    @Autowired
    private LoraAdeunisDecoder loraAdeunisDecoder;

    @Autowired
    private LoraCM3020Decoder loraCM3020Decoder;

    @Autowired
    private LobaroLoraWMBusDecoder lobaroLoraWMBusDecoder;

    public int decode(String message, int deviceType, int fPort) {
        int counter = 0;
        switch (fPort) {
            case LoraFPort.FPORT_1:
                counter = decodeFport1(message, deviceType);
                log.info("loraAdeunisDecoder: counter = " + counter);
                break;
            case LoraFPort.FPORT_11:
                counter = decodeFport11(message, deviceType);
                log.info("lobaroLoraWMBusDecoder: counter = " + counter);
                break;

            case LoraFPort.FPORT_14:
                var loraFport14 = loraCM3020Decoder.decodeF14(message);
                counter = (int) loraFport14.getCounter();
                log.info(loraFport14.toString());
                break;
            case LoraFPort.FPORT_24:
                var loraFport24 = loraCM3020Decoder.decodeF24(message);
                counter = (int) loraFport24.getTotalVolume();
                log.info(loraFport24.toString());
                break;
        }

        return counter;
    }

    private int decodeFport11(String message, int deviceType) {
        return lobaroLoraWMBusDecoder.decode(message, LoraFPort.FPORT_11);
    }

    private int decodeFport1(String message, int deviceType) {
        int counter = 0;
        switch (deviceType) {
            case Customer.DEVICE_TYPA_PULSE:
                counter = loraAdeunisDecoder.decode(message);
                break;
            case Customer.DEVICE_TYPA_SOLAR:
                counter = lobaroLoraWMBusDecoder.decode(message, LoraFPort.FPORT_1);
                break;
        }
        return counter;
    }
}
