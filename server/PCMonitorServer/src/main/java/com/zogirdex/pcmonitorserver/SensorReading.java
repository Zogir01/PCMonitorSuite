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
	private Float value;

	@Column
	private LocalDateTime timestamp;

	public SensorReading() {
	}

	public SensorReading(Sensor sensor, Float value) {
		this.sensor = sensor;
		this.value = value;
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

	public Float getValue() {
		return value;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
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

	public void setValue(Float value) {
		this.value = value;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "SensorReading{" + "id=" + id + ", computer=" + computer + ", sensor=" + sensor + ", value=" + value + ", timestamp=" + timestamp + '}';
	}
}
