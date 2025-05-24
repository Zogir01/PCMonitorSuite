using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PCMonitor
{
    public class SensorInfo
    {
        public string HardwareName { get; set; }
        public string SubHardwareName { get; set; } // null jeśli brak
        public string SensorName { get; set; }
        public string SensorType { get; set; }
        public float? Value { get; set; }
    }
}
