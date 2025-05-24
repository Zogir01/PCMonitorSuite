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


// OBCZAJ TEN TUTORIAL NA TEMAT TWORZENIA PROGRAMU-SERWISU W WINDOWS
// https://www.youtube.com/watch?v=RcA-TLQfpp8&ab_channel=CodeMaze
// Ew. zrób jako zwykłą apke konsolową i użyj task scheduler
// ew. może jakiś tray-icon?
//
// TO-DO
// - trzeba zrobić zapis konkretnych parametrów do json
// - zrobić przesył jsona po http

namespace PCMonitor
{
    static class Program
    {
        private static NotifyIcon trayIcon;
        //private static Timer timer;
        private static DiagnosticForm diagnosticForm;

        static void Main(string[] args)
        {
            Application.ApplicationExit += Application_Exit;
            diagnosticForm = new DiagnosticForm();
  
            InitTrayIcon();
            //Init_Timer();
            
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
                Monitor.Instance.StartMonitoring(10000, "http://127.0.0.1/api/data");
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

        //private static void Init_Timer()
        //{
        //    // Timer co 10 sekund
        //    timer = new Timer();
        //    timer.Interval = 10000; // 10 sekund
        //    timer.Tick += Timer_Tick;
        //    timer.Start();
        //}


        //private static void Timer_Tick(object sender, EventArgs e)
        //{
        //    // Odczytaj dane czujników
        //    List<SensorInfo> sensData = Monitor.Instance.ReadData();

        //    // Wyślij dane czujników na serwer
        //    Monitor.Instance.SendToApi(sensData, "http://127.0.0.1/api/data");
        //}

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
