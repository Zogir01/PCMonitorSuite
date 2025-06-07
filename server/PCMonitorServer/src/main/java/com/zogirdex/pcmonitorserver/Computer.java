package com.zogirdex.pcmonitorserver;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author tom3k
 */
@Entity
@Table (name = "Computer")
public class Computer {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String computerName;

    @OneToMany(mappedBy = "computer")
    private List<SensorReading> readings;
}
