package com.zogirdex.pcmonitorserver;

/**
 *
 * @author tom3k
 *
 * DTO — Data Transfer Object
 *
 * Dane diagnostyczne - przychodzące
 *
 *  * Nazwy parametrów tej klasy muszą być zgodne z danymi w json, można by też użyć adnotacji, np.: (w przypadku google.gson)
 * @SerializedName("HardwareName")
 */
public class MonitorDataDTO {
	public String HardwareName;
	public String SubHardwareName;
	public String SensorName;
	public String SensorType;
	public Float SensorValue;
	public String TimestampUtc;
}
