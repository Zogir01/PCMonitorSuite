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
	public Float value;
	public LocalDateTime timestamp;

	public SensorReadingDTO(Long id, Float value) {
		this.id = id;
		this.value = value;
	}
}
