package com.zogirdex.pcmonitorserver;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

/**
 *
 * @author tom3k
 * 
 * Model danych aplikacji (encja). Reprezentuje dane pojedyńczego pomiaru danych diagnostycznych w systemie.
 * 
 */
@Entity
@Table(name = "SensorReading")
public class SensorReading {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "computer_id")
	private Computer computer;

	@ManyToOne
	@JoinColumn(name = "sensor_id")
	private Sensor sensor;

	@Column
	private Float sensorValue;

	@Column
	private String timestampUtc;

	public SensorReading() {
	}

	public SensorReading(Sensor sensor, Float sensorValue, String timestampUtc) {
		this.sensor = sensor;
		this.sensorValue = sensorValue;
		this.timestampUtc = timestampUtc;
	}

	public Long getId() {
		return id;
	}

	public Computer getComputer() {
		return computer;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public Float getSensorValue() {
		return sensorValue;
	}

	public String getTimestampUtc() {
		return timestampUtc;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setComputer(Computer computer) {
		this.computer = computer;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public void setSensorValue(Float sensorValue) {
		this.sensorValue = sensorValue;
	}

	public void setTimestampUtc(String timestampUtc) {
		this.timestampUtc = timestampUtc;
	}

	@Override
	public String toString() {
		return "SensorReading{" + "id=" + id + ", computer=" + computer + ", sensor=" + sensor + ", sensorValue=" + sensorValue + ", timestampUtc=" + timestampUtc + '}';
	}

	
}
