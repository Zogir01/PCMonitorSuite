package com.zogirdex.pcmonitorserver;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author tom3k
 * 
 * Model danych aplikacji (encja). Reprezentuje dane komputera w systemie.
 * 
 */
@Entity
@Table(name = "Computer")
public class Computer {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String computerName;

	@OneToMany(mappedBy = "computer")
	private List<SensorReading> readings = new ArrayList<SensorReading>();

	public Computer() {
	}

	public Computer(String computerName) {
		this.computerName = computerName;
	}

	public Long getId() {
                                    return id;
	}

	public String getComputerName() {
		return computerName;
	}

	public List<SensorReading> getReadings() {
		return readings;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}

	public void setReadings(List<SensorReading> readings) {
		this.readings = readings;
	}

	void addSensorReading(SensorReading sensorReading) {
		readings.add(sensorReading);
	}
	
	@Override
	public String toString() {
		return "Computer{" + "id=" + id + ", computerName=" + computerName + ", readings=" + "xd" + '}';
	}
}
