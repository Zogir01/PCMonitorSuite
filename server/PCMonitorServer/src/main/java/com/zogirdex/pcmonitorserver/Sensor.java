package com.zogirdex.pcmonitorserver;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.GeneratedValue;

/**
 *
 * @author tom3k
 */
@Entity
//@Table(name = "Sensor")
@Table(name = "Sensor", uniqueConstraints = @UniqueConstraint(columnNames = {
    "hardwareName", "subHardwareName", "sensorName", "sensorType"
}))
public class Sensor {
	
	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String hardwareName;

	@Column
	private String subHardwareName;

	@Column
	private String sensorName;

	@Column
	private String sensorType;

	@OneToMany(mappedBy = "sensor")
	private List<SensorReading> readings = new ArrayList<SensorReading>();

	public Sensor() {
	}

	public Sensor(String hardwareName, String subHardwareName, String sensorName, String sensorType) {
		this.hardwareName = hardwareName;
		this.subHardwareName = subHardwareName;
		this.sensorName = sensorName;
		this.sensorType = sensorType;
	}

	public Long getId() {
		return id;
	}

	public String getHardwareName() {
		return hardwareName;
	}

	public String getSubHardwareName() {
		return subHardwareName;
	}

	public String getSensorName() {
		return sensorName;
	}

	public String getSensorType() {
		return sensorType;
	}

	public List<SensorReading> getReadings() {
		return readings;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setHardwareName(String hardwareName) {
		this.hardwareName = hardwareName;
	}

	public void setSubHardwareName(String subHardwareName) {
		this.subHardwareName = subHardwareName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}

	public void setReadings(List<SensorReading> readings) {
		this.readings = readings;
	}
	
	void addSensorReading(SensorReading sensorReading) {
		readings.add(sensorReading);
	}

	@Override
	public String toString() {
		return "Sensor{" + "id=" + id + ", hardwareName=" + hardwareName + ", subHardwareName=" + subHardwareName + ", sensorName=" + sensorName + ", sensorType=" + sensorType + ", readings=" + "xd" + '}';
	}
}
