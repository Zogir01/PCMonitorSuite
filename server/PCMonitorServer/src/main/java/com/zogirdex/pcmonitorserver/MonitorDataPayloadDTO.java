package com.zogirdex.pcmonitorserver;

import java.util.List;

/**
 *
 * @author tom3k
 *
 * Cały ładunek JSON danych diagnostycznych - przychodzących
 *
 */
public class MonitorDataPayloadDTO {
	public String ComputerName;
	public List<MonitorDataDTO> Readings;
}
