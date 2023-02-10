package com.bojan.lora.domain.lora.actillity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DevEUIUplink {
    @JsonProperty("Time")
    private String time;
    @JsonProperty("DevEUI")
    private String devEUI;
    @JsonProperty("FPort")
    private int fPort;
    @JsonProperty("FCntUp")
    private int fCntUp;
    private int ACKbit;
    private int ADRbit;
    private int MType;
    private int FCntDn;
    @JsonProperty("payload_hex")
    private String payloadHex;
    private String mic_hex;
    private String Lrcid;
    private int LrrRSSI;
    private double LrrSNR;
    private int LrrESP;
    private int SpFact;
    private String SubBand;
    private String Channel;
    private int DevLrrCnt;
    private String Lrrid;
    private int Late;
    private double LrrLAT;
    private double LrrLON;
    private Lrrs Lrrs;
    private String DevLocTime;
    private double DevLAT;
    private double DevLON;
    private int DevAlt;
    private int DevLocRadius;
    private int DevAltRadius;
    private int DevUlFCntUpUsed;
    private int DevLocDilution;
    private int DevAltDilution;
    private int DevNorthVel;
    private int DevEastVel;
    private int NwGeolocAlgo;
    private int NwGeolocAlgoUsed;
    private String CustomerID;
    private CustomerData CustomerData;
    private BaseStationData BaseStationData;
    private String ModelCfg;
    private DriverCfg DriverCfg;
    private int BatteryLevel;
    private String BatteryTime;
    private int Margin;
    private double InstantPER;
    private double MeanPER;
    private String DevAddr;
    private double deviceUplinkDC;
    private double deviceUplinkDCSubBand;
    private int TxPower;
    private int NbTrans;
    private double Frequency;
    private String DynamicClass;
    private int ClassBPeriodicity;
}
