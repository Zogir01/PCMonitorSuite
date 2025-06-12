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
        private static string apiUrl = "http://127.0.0.1:8080/PCMonitorServer/api/data";

        static void Main(string[] args)
        {
            Application.ApplicationExit += Application_Exit;
            
            InitTrayIcon();
            
            Logger.Log("Start PCMonitor.");
            Application.Run();
        }

        private static void InitTrayIcon()
        {
            ContextMenu contextMenu = new ContextMenu();

            contextMenu.MenuItems.Add("Pokaż dane diagnostyczne.", (s, e) =>
            {
                diagnosticForm = new DiagnosticForm();
                diagnosticForm.LoadSensorData();
                diagnosticForm.Show();
            });

            contextMenu.MenuItems.Add("Start sending", (s, e) =>
            {
                Monitor.Instance.StartSending(apiUrl);
                Logger.Log("Wysyłanie danych zostało aktywowane przez użytkownika.");
                MessageBox.Show("Wysyłanie danych zostało aktywowane.", "Informacja");
            });

            contextMenu.MenuItems.Add("Stop sending", (s, e) =>
            {
                Monitor.Instance.StopSending();
                Logger.Log("Wysyłanie danych zostało zatrzymane przez użytkownika.");
                MessageBox.Show("Wysyłanie danych zostało zatrzymane.", "Informacja");
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
