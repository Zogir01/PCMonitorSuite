using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PCMonitor
{
    internal class Logger
    {
        public static readonly string logPath = Utility.GetResourcePath("PCMonitor.log");
        public static void Log(string message)
        {
            string logLine = $"{DateTime.Now:yyyy-MM-dd HH:mm:ss} | {message}";
            File.AppendAllText(logPath, logLine + Environment.NewLine);
        }
    }
}
