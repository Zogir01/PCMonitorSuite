package com.zogirdex.pcmonitorserver;

import java.util.List;

/**
 *
 * @author tom3k
 *
 * Cały ładunek json odbierany od clienta
 *
 * Nazwy parametrów tej klasy muszą być zgodne z danymi w json, można by też użyć adnotacji, np.: (w przypadku google.gson)
 * @SerializedName("ComputerName")
 */
public class ComputerDTO {
	public String ComputerName;
	public List<SensorReadingDTO> Readings;
}
