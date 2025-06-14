using PCMonitor;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PCMonitor
{
    public class MonitorDataPayloadDTO
    {
        public string ComputerName { get; set; }
        public List<MonitorDataDTO> Readings { get; set; }
    }
}
