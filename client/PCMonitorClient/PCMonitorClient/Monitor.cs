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

        private Timer readingTimer;
        private Timer sendingTimer;
        private Computer computer;
        private int readingDataIntervalMs = 5000;
        private readonly object _lock = new object();

        // Lista danych diagnostycznych
        private List<MonitorDataDTO> monitorDataDTOs = new List<MonitorDataDTO>();

        // getter z zabezpieczeniem wielowątkowym
        public List<MonitorDataDTO> getMonitorData()
        {
            lock (_lock)
            {
                return new List<MonitorDataDTO>(monitorDataDTOs); // zwraca kopie
            };

        }

        // setter z zabezpieczeniem wielowątkowym
        private void setMonitorData(List<MonitorDataDTO> monitorDataDTOs)
        {
            lock (_lock)
            {
                this.monitorDataDTOs = monitorDataDTOs;
            }
        }

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

            StartReading(readingDataIntervalMs);
        }

        private void StartReading(int intervalMs)
        {
            readingTimer = new Timer(_ =>
            {
                try
                {
                    setMonitorData(ReadData());
                }
                catch (Exception ex)
                {
                    Logger.Log("Błąd podczas odzytywania danych diagnostycznych: " + ex.Message);
                }
            }, null, 0, intervalMs);
        }

        // Metoda do uruchomienia monitorowania w tle (poza wątkiem UI)
        public void StartSending(int intervalMs, string apiUrl)
        {
            sendingTimer = new Timer(_ =>
            {
                try
                {
                    var payload = new MonitorDataPayloadDTO
                    {
                        ComputerName = Environment.MachineName, // unikalny identyfikator komputera
                        Readings = getMonitorData()
                    };
                    SendToApi(payload, apiUrl);
                }
                catch (Exception ex)
                {
                    Logger.Log("Błąd podczas wysyłania danych: " + ex.Message);
                }
            }, null, 0, intervalMs);
        }

        public void StopSending()
        {
            sendingTimer?.Change(Timeout.Infinite, Timeout.Infinite);
            sendingTimer?.Dispose();
            sendingTimer = null;
        }

        private List<MonitorDataDTO> ReadData()
        {
            List<MonitorDataDTO> loadedMonitorData = new List<MonitorDataDTO>();

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
                            loadedMonitorData.Add(new MonitorDataDTO
                            {
                                HardwareName = hardware.Name,
                                SubHardwareName = subhardware.Name,
                                SensorName = sensor.Name,
                                SensorType = sensor.SensorType.ToString(),
                                SensorValue = sensor.Value,
                                TimestampUtc = DateTime.UtcNow.ToString("o") // data w formacie ISO 8601
                            });
                        }
                    }

                    foreach (ISensor sensor in hardware.Sensors)
                    {
                        loadedMonitorData.Add(new MonitorDataDTO
                        {
                            HardwareName = hardware.Name,
                            SubHardwareName = null,
                            SensorName = sensor.Name,
                            SensorType = sensor.SensorType.ToString(),
                            SensorValue = sensor.Value,
                            TimestampUtc = DateTime.UtcNow.ToString("o") // // data w formacie ISO 8601
                        });
                    }
                }
            }
            return loadedMonitorData;
        }

        private void SendToApi(MonitorDataPayloadDTO payload, String Url)
        {
            var json = JsonConvert.SerializeObject(payload, Formatting.Indented);

            // Testowo - zapis do pliku
            File.WriteAllText("readings.json", json);

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
                    Logger.Log("Stack trace: " + ex.StackTrace);
                    Logger.Log("ex.ToString(): " + ex.ToString());
                }
            }
        }
    }
}
