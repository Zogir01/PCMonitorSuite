/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zogirdex.pcmonitorserver;

import java.time.LocalDateTime;

/**
 *
 * @author tom3k
 */
public class SensorReadingDTO {
	public Long id;
	public Float sensorValue;
	public String timestampUtc;

	public SensorReadingDTO(Long id, Float sensorValue, String timestampUtc) {
		this.id = id;
		this.sensorValue = sensorValue;
		this.timestampUtc = timestampUtc;
	}
}
