using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PCMonitor
{
    internal class Utility
    {
        public readonly static string ResourcesPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Resources");

        public static string GetResourcePath(string resourceName)
        {
            return Path.Combine(ResourcesPath, resourceName);
        }
    }
}
