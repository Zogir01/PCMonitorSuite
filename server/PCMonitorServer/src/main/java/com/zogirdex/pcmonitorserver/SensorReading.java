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
}