/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zogirdex.pcmonitorserver;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author tom3k
 */
@Entity
@Table(name = "sensor_info")
public class Sensor {
    
    @Id
    private long computerId;
    
    @Column
    private String hardwareName;
    
    @Column
    private String subHardwareName;
    
    @Column
    private String sensorName;
    
    @Column
    private String sensorType;
        
    @Column
    private String value;
    
    public Sensor() {
        
    }

    public Sensor(long computerId, String hardwareName, String subHardwareName, String sensorName, String sensorType, String value) {
        this.computerId = computerId;
        this.hardwareName = hardwareName;
        this.subHardwareName = subHardwareName;
        this.sensorName = sensorName;
        this.sensorType = sensorType;
        this.value = value;
    }

    public long getComputerId() {
        return computerId;
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

    public String getValue() {
        return value;
    }

    public void setComputerId(long computerId) {
        this.computerId = computerId;
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

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SensorInfo{" + "computerId=" + computerId + ", hardwareName=" + hardwareName + ", subHardwareName=" + subHardwareName + ", sensorName=" + sensorName + ", sensorType=" + sensorType + ", value=" + value + '}';
    }

//    void addOcena(Ocena ocena) {
//        oceny.add(ocena);
//        ocena.getPrzedmiot().getOceny().add(ocena);
//        ocena.setStudent(this);
//    }
}
