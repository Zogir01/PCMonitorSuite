using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PCMonitor
{
    // Dane pojedyńczego sensora
    public class SensorReading
    {
        public string HardwareName { get; set; }
        public string SubHardwareName { get; set; } // null jeśli brak
        public string SensorName { get; set; }
        public string SensorType { get; set; }
        public float? Value { get; set; }
    }

    // Cały ładunek JSON wysyłany do serwera
    public class SensorReadingsPayload
    {
        public string ComputerName { get; set; }
        public List<SensorReading> Readings { get; set; }
    }
}
