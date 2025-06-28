/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zogirdex.pcmonitorserver;

/**
 *
 * @author tom3k
 */
public class SensorDTO {
	public Long id;
	public String hardwareName;
	public String subHardwareName;
	public String sensorName;
	public String sensorType;

	public SensorDTO(Long id, String hardwareName, String subHardwareName, String sensorName, String sensorType) {
		this.id = id;
		this.hardwareName = hardwareName;
		this.subHardwareName = subHardwareName;
		this.sensorName = sensorName;
		this.sensorType = sensorType;
	}
}
