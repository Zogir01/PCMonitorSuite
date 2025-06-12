using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;

namespace PCMonitor
{
    public partial class DiagnosticForm : Form
    {
        private ListView listView;
        private bool shouldCheckAll = true;
        public DiagnosticForm()
        {
            this.Text = "Dane diagnostyczne";
            this.Width = 1400;
            this.Height = 600;

            listView = new ListView
            {
                View = View.Details,
                Dock = DockStyle.Fill,
                FullRowSelect = true,
                CheckBoxes = true
            };
            
            listView.Columns.Add("Hardware", 150);
            listView.Columns.Add("SubHardware", 150);
            listView.Columns.Add("Sensor", 200);
            listView.Columns.Add("Type", 100);
            listView.Columns.Add("SensorValue", 100);
            listView.Columns.Add("TimestampUtc", 100);

            Button reloadButton = new Button
            {
                Text = "Przeładuj dane",
                Dock = DockStyle.Bottom,
                Height = 40
            };
            reloadButton.Click += (s, e) => LoadSensorData();


            Button toggleCheckAllButton = new Button
            {
                Text = "Zaznacz wszystko",
                Dock = DockStyle.Bottom,
                Height = 40
            };
            toggleCheckAllButton.Click += (s, e) => toggleCheckAll();

            Button setFiltersButton = new Button
            {
                Text = "Ustaw filtry",
                Dock = DockStyle.Bottom,
                Height = 40
            };
            setFiltersButton.Click += (s, e) => setFilters();

            Button deleteFiltersButton = new Button
            {
                Text = "Usuń filtry",
                Dock = DockStyle.Bottom,
                Height = 40
            };
            deleteFiltersButton.Click += (s, e) => clearFilters();

            this.Controls.Add(listView);
            this.Controls.Add(reloadButton);
            this.Controls.Add(toggleCheckAllButton);
            this.Controls.Add(setFiltersButton);
            this.Controls.Add(deleteFiltersButton);
        }

        public void LoadSensorData()
        {
            listView.Items.Clear();

            // Kopia danych z singletona Monitor
            List<MonitorDataDTO> data = Monitor.Instance.getMonitorData();

            if(data == null || data.Count == 0)
            {
                Logger.Log("Nie można wczytać danych do tabeli, gdyż dane diagnostyczne nie zostały odczytane");
                return;
            }

            foreach (var sensor in data)
            {
                if (listView.Items.ContainsKey(sensor.SensorName))
                {
                    Console.WriteLine("test + " + sensor.SensorName);
                }

                ListViewItem item = new ListViewItem(sensor.HardwareName);
                item.SubItems.Add(sensor.SubHardwareName ?? "-");
                item.SubItems.Add(sensor.SensorName);
                item.SubItems.Add(sensor.SensorType);
                item.SubItems.Add(sensor.SensorValue?.ToString("F1") ?? "N/A");
                item.SubItems.Add(sensor.TimestampUtc);

                // tag do zastosowania filtra
                item.Tag = sensor.SensorName;

                listView.Items.Add(item);
            }

            // Załadowanie filtrów do wizualizacji
            HashSet<string> filters = Monitor.Instance.getFilters();

            foreach (ListViewItem item in listView.Items)
            {
                if (filters.Contains((string)item.Tag))
                {
                    item.Checked = true;
                }
            }
        }

        // TO-DO - nie da się ustawić filtrów dla niektórych sensorów, np. dla "System Fan #1"
        private void setFilters()
        {
            // Zbierz HashSet SensorName do wysłania (tych zaznaczonych)
            // Tag w elemencie listView = SensorName do wysłania
            HashSet<string> sensorsToInclude = new HashSet<string>();

            foreach (ListViewItem item in listView.Items)
            {
                if (item.Checked && item.Tag is string tag)
                {
                    sensorsToInclude.Add(tag);
                }
            }

            // Debug/logowanie
            //foreach (var item in output)
            //{
            //    Console.WriteLine(item.SensorName);
            //}

            Monitor.Instance.setFilters(sensorsToInclude);
        }

        private void clearFilters()
        {
            foreach (ListViewItem item in listView.Items)
            {
                item.Checked = false;
            }
            shouldCheckAll = true;
            Monitor.Instance.clearFilters();
        }

        private void toggleCheckAll()
        {
            foreach (ListViewItem item in listView.Items)
            {
                item.Checked = shouldCheckAll;
            }

            // Przełącz flagę
            shouldCheckAll = !shouldCheckAll;
        }
    }
}
