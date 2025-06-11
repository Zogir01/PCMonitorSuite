using System;
using System.Collections.Generic;
using System.Windows.Forms;

namespace PCMonitor
{
    public partial class DiagnosticForm : Form
    {
        private ListView listView;
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

            listView.ItemCheck += ListView_ItemCheck;
            //listView.AutoResizeColumns(ColumnHeaderAutoResizeStyle.HeaderSize);

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

        public void LoadSensorData()
        {
            listView.Items.Clear();
            List<MonitorDataDTO> data = Monitor.Instance.ReadData();

            foreach (var sensor in data)
            {
                ListViewItem item = new ListViewItem(sensor.HardwareName);
                item.SubItems.Add(sensor.SubHardwareName ?? "-");
                item.SubItems.Add(sensor.SensorName);
                item.SubItems.Add(sensor.SensorType);
                item.SubItems.Add(sensor.SensorValue?.ToString("F1") ?? "N/A");
                item.SubItems.Add(sensor.TimestampUtc);
                listView.Items.Add(item);
            }
        }

        // TO-DO - zaimplementować filtry wyboru wysyłanych danych diagnostycznych do serwera.
        private void ListView_ItemCheck(object sender, ItemCheckEventArgs e)
        {
            // Zaznaczony element w listView
            var item = listView.Items[e.Index];

            if(e.NewValue == CheckState.Checked)
            {

            }
            else
            {
  
            }
        }
    }
}
