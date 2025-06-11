using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PCMonitor
{
    // Dane pojedyńczego sensora
    public class MonitorDataDTO
    {
        public string HardwareName { get; set; }
        public string SubHardwareName { get; set; } // null jeśli brak
        public string SensorName { get; set; }
        public string SensorType { get; set; }
        public float? SensorValue { get; set; }
        public string TimestampUtc { get; set; }
    }
}
