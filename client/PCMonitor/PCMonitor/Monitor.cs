using LibreHardwareMonitor.Hardware;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.IO;
using System.Threading.Tasks;
using System.Threading;
using System.Security.Policy;

namespace PCMonitor
{
    internal class Monitor
    {
        // singleton
        private static readonly Lazy<Monitor> instance = new Lazy<Monitor>(() => new Monitor());
        public static Monitor Instance => instance.Value;
        // singleton

        private Timer timer;

        private Computer computer;

        private Monitor ()
        {
            computer = new Computer
            {
                IsCpuEnabled = true,
                IsGpuEnabled = true,
                IsMemoryEnabled = true,
                IsMotherboardEnabled = true,
                IsControllerEnabled = true,
                IsNetworkEnabled = true,
                IsStorageEnabled = true
            };

            computer.Open();
        }

        // Metoda do uruchomienia monitorowania w tle (poza wątkiem UI)
        public void StartMonitoring(int intervalMs, string apiUrl)
        {
            timer = new Timer(_ =>
            {
                try
                {
                    var data = ReadData();
                    SendToApi(data, apiUrl);
                }
                catch (Exception ex)
                {
                    Logger.Log("Błąd w monitoringu: " + ex.Message);
                }
            }, null, 0, intervalMs); // 0 = start natychmiast, potem co intervalMs
        }

        // Metoda do zatrzymania monitorowania
        public void StopMonitoring()
        {
            timer?.Change(Timeout.Infinite, Timeout.Infinite);
            timer?.Dispose();
            timer = null;
        }

        public List<SensorInfo> ReadData()
        {
            List<SensorInfo> sensorData = new List<SensorInfo>();

            foreach (IHardware hardware in computer.Hardware)
            {
                hardware.Update();

                if (hardware.HardwareType == HardwareType.Motherboard
                    || hardware.HardwareType == HardwareType.Cpu
                    || hardware.HardwareType == HardwareType.GpuNvidia
                    || hardware.HardwareType == HardwareType.GpuAmd
                    || hardware.HardwareType == HardwareType.GpuIntel)
                {
                    foreach (IHardware subhardware in hardware.SubHardware)
                    {
                        subhardware.Update();

                        foreach (ISensor sensor in subhardware.Sensors)
                        {
                            sensorData.Add(new SensorInfo
                            {
                                HardwareName = hardware.Name,
                                SubHardwareName = subhardware.Name,
                                SensorName = sensor.Name,
                                SensorType = sensor.SensorType.ToString(),
                                Value = sensor.Value
                            });
                        }
                    }

                    foreach (ISensor sensor in hardware.Sensors)
                    {
                        sensorData.Add(new SensorInfo
                        {
                            HardwareName = hardware.Name,
                            SubHardwareName = null,
                            SensorName = sensor.Name,
                            SensorType = sensor.SensorType.ToString(),
                            Value = sensor.Value
                        });
                    }
                }
            }
            return sensorData;
        }

        public void SendToApi(List<SensorInfo> sensorData, String Url)
        {
            var json = JsonConvert.SerializeObject(sensorData, Formatting.Indented);

            using (var client = new HttpClient())
            {
                var content = new StringContent(json, Encoding.UTF8, "application/json");
                try
                {
                    var response = client.PostAsync(Url, content).Result;
                    Logger.Log("Status: " + response.StatusCode);
                }
                catch (Exception ex)
                {
                    Logger.Log("Błąd API: " + ex.Message);
                }
            }
        }

        public void testowa_wysylka(String url)
        {
            using (var client = new HttpClient())
            {
                var content = new StringContent("testxd");
                try
                {
                    var response = client.PostAsync(url, content).Result;
                    Logger.Log("Status: " + response.StatusCode);

                    string responseBody = response.Content.ReadAsStringAsync().Result;
                    Logger.Log("Wiadomość od serwera: " + responseBody);
                }
                catch (Exception ex)
                {
                    Logger.Log("Błąd API: " + ex.Message);
                }
            }
        }
    }
}
