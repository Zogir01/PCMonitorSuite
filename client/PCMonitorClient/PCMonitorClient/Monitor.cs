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
using LibreHardwareMonitor.Hardware.Cpu;

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
        private int sendingDataIntervalMs = 10000;
        private readonly object _lock = new object();

        // Lista danych diagnostycznych
        private List<MonitorDataDTO> monitorDataDTOs = new List<MonitorDataDTO>();

        // filtry, gdzie pojedyńczy filtr = SensorName
        private HashSet<string> monitorDataFilters = new HashSet<string>();

        public List<MonitorDataDTO> getMonitorData()
        {
            lock (_lock)
            {
                return new List<MonitorDataDTO>(monitorDataDTOs); // zwraca kopie
            };

        }

        private void setMonitorData(List<MonitorDataDTO> monitorDataDTOs)
        {
            lock (_lock)
            {
                this.monitorDataDTOs = monitorDataDTOs;
            }
        }

        public HashSet<string> getFilters()
        {
            lock (_lock)
            {
                return new HashSet<string>(monitorDataFilters); // zwraca kopie
            };

        }

        public void setFilters(HashSet<string> filters)
        {
            lock (_lock)
            {
                this.monitorDataFilters = filters;
            }
        }

        public int getReadingDataIntervalMs()
        {
            lock (_lock)
            {
                int interval = readingDataIntervalMs;
                return interval;
            }
        }

        public void setReadingDataIntervalMs(int interval)
        {
            lock (_lock)
            {
                this.readingDataIntervalMs = interval;
            }
        }

        public int getSendingDataIntervalMs()
        {
            lock (_lock)
            {
                int interval = sendingDataIntervalMs;
                return interval;
            }
        }

        public void setSendingDataIntervalMs(int interval)
        {
            lock (_lock)
            {
                this.sendingDataIntervalMs = interval;
            }
        }

        public void clearFilters()
        {
            lock (_lock)
            {
                this.monitorDataFilters.Clear();
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

            StartReading();
        }

        private void StartReading()
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
            }, null, 0, getReadingDataIntervalMs());
        }

        // Metoda do uruchomienia monitorowania w tle (poza wątkiem UI)
        public void StartSending(string apiUrl)
        {
            sendingTimer = new Timer(_ =>
            {
                try
                {
                    List<MonitorDataDTO> monitorData = getMonitorData();
                    HashSet<string> filters = getFilters();
                    List<MonitorDataDTO> monitorDataFiltered = new List<MonitorDataDTO>();

                    // Zastosuj filtry
                    foreach(MonitorDataDTO md in monitorData)
                    {
                        if (filters.Contains(md.SensorName))
                        {
                            monitorDataFiltered.Add(md);
                        }
                    }

                    MonitorDataPayloadDTO payload = new MonitorDataPayloadDTO
                    {
                        ComputerName = Environment.MachineName, // unikalny identyfikator komputera
                        Readings = monitorDataFiltered
                    };
   
                    SendToApi(payload, apiUrl);
                }
                catch (Exception ex)
                {
                    Logger.Log("Błąd podczas wysyłania danych: " + ex.Message);
                }
            }, null, 0, getSendingDataIntervalMs());
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

            Console.WriteLine(json);

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

        // TO-DO
        public void saveFiltersToFile()
        {

        }

        // TO-DO
        public void loadFiltersFromFile()
        {

        }
    }
}
