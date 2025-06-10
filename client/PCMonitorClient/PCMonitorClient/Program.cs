using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.IO;
using System.Windows.Forms;
using System.Drawing;
using LibreHardwareMonitor.Hardware;
using Newtonsoft.Json;

namespace PCMonitor
{
    static class Program
    {
        private static NotifyIcon trayIcon;
        private static DiagnosticForm diagnosticForm;

        static void Main(string[] args)
        {
            Application.ApplicationExit += Application_Exit;
            diagnosticForm = new DiagnosticForm();
  
            InitTrayIcon();
            
            Logger.Log("Start PCMonitor.");
            Application.Run();
        }

        private static void InitTrayIcon()
        {
            ContextMenu contextMenu = new ContextMenu();

            contextMenu.MenuItems.Add("Pokaż dane diagnostyczne.", (s, e) =>
            {
                diagnosticForm.LoadSensorData();
                diagnosticForm.Show();
            });

            contextMenu.MenuItems.Add("Start monitoring", (s, e) =>
            {
                Monitor.Instance.StartMonitoring(10000, "http://127.0.0.1:8080/PCMonitorServer/api/data");
                Logger.Log("Monitoring został aktywowany przez użytkownika.");
                MessageBox.Show("Monitoring został aktywowany.", "Informacja");
            });

            contextMenu.MenuItems.Add("Stop monitoring", (s, e) =>
            {
                Monitor.Instance.StopMonitoring();
                Logger.Log("Monitoring został zatrzymany przez użytkownika.");
                MessageBox.Show("Monitoring został zatrzymany.", "Informacja");
            });

            contextMenu.MenuItems.Add("Pokaż logi", (s, e) =>
            {
                System.Diagnostics.Process.Start("notepad.exe", Logger.logPath);
            });

            contextMenu.MenuItems.Add("Wyczyść logi", (s, e) =>
            {
                try
                {
                    File.WriteAllText(Logger.logPath, string.Empty);
                    Logger.Log("Logi został wyczyszczone ręcznie.");
                    MessageBox.Show("Logi zostały wyczyszczone.", "Informacja");
                }
                catch (Exception ex)
                {
                    Logger.Log("Błąd przy czyszczeniu loga: " + ex.Message);
                    MessageBox.Show("Wystąpił błąd przy czyszczeniu loga: " + ex.Message, "Informacja");
                }
            });

            contextMenu.MenuItems.Add("Instrukcja", (s, e) =>
            {
                System.Diagnostics.Process.Start("notepad.exe", Utility.GetResourcePath("README.txt"));
            });

            contextMenu.MenuItems.Add("Zamknij", (s, e) =>
            {
                Application.Exit();
            });

            contextMenu.MenuItems.Add("Testowa wysylka", (s, e) =>
            {
                Monitor.Instance.testowa_wysylka("http://127.0.0.1:8080/Przyklad3/SecondServlet");
            });

            trayIcon = new NotifyIcon();
            trayIcon.Text = "PCMonitor";
            trayIcon.Visible = true;
            trayIcon.DoubleClick += TrayIcon_DoubleClick;
            trayIcon.ContextMenu = contextMenu;

            try
            {
                string iconPath = Utility.GetResourcePath("PCMonitor.ico");
                trayIcon.Icon = new Icon(iconPath);
            }
            catch
            {
                trayIcon.Icon = SystemIcons.Application;
                Logger.Log("Błąd podczas ustawiania ikonu tray.");
                MessageBox.Show("Błąd podczas ustawiania ikonu tray.", "Błąd");
            }
        }

        private static void TrayIcon_DoubleClick(object sender, EventArgs e)
        {
            diagnosticForm.LoadSensorData();
            diagnosticForm.Show();
        }

        private static void Application_Exit(object sender, EventArgs e)
        {
            Logger.Log("Zakończenie PCMonitor.");
        }
    }
}
