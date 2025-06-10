using System;
using System.Collections.Generic;
using System.Windows.Forms;

namespace PCMonitor
{
    public partial class DiagnosticForm : Form
    {
        public DiagnosticForm()
        {
            this.Text = "Dane diagnostyczne";
            this.Width = 800;
            this.Height = 600;

            listView = new ListView
            {
                View = View.Details,
                Dock = DockStyle.Fill,
                FullRowSelect = true
            };

            listView.Columns.Add("Hardware", 150);
            listView.Columns.Add("SubHardware", 150);
            listView.Columns.Add("Sensor", 200);
            listView.Columns.Add("Type", 100);
            listView.Columns.Add("Value", 100);

            Button reloadButton = new Button
            {
                Text = "Przeładuj dane",
                Dock = DockStyle.Bottom,
                Height = 40
            };
            reloadButton.Click += (s, e) => LoadSensorData();

            this.Controls.Add(listView);
            this.Controls.Add(reloadButton);
        }

        private ListView listView;

        public void LoadSensorData()
        {
            listView.Items.Clear();
            List<SensorReading> data = Monitor.Instance.ReadData();

            foreach (var sensor in data)
            {
                ListViewItem item = new ListViewItem(sensor.HardwareName);
                item.SubItems.Add(sensor.SubHardwareName ?? "-");
                item.SubItems.Add(sensor.SensorName);
                item.SubItems.Add(sensor.SensorType);
                item.SubItems.Add(sensor.Value?.ToString("F1") ?? "N/A");
                listView.Items.Add(item);
            }
        }
    }
}
