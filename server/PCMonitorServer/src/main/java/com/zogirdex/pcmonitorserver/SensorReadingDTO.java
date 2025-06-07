package com.zogirdex.pcmonitorserver;

/**
 *
 * @author tom3k
 * 
 * DTO — Data Transfer Object
 * 
 * Klasa ta określa dane diagnostyczne przychodzące od clienta po rest api
 */
public class SensorReadingDTO {
    public String hardwareName;
    public String subHardwareName;
    public String sensorName;
    public String sensorType;
    public Float value;
}
