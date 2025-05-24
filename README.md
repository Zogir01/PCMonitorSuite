# 🖥️ PCMonitorSuite

**PCMonitorSuite** is a system for monitoring a computer's hardware diagnostics (temperatures, CPU/GPU usage, memory, etc.), saving data locally and periodically sending it to a REST API server. The project consists of two main components: a desktop application (Windows) and a backend server (Java REST API).

---

## ✅ Requirements
To build and run the PCMonitorSuite, make sure you have the following installed:

### 🖥️ PCMonitor (desktop application)
- Windows 10/11 (x64)
- .NET Framework 4.7.2 or higher
- LibreHardwareMonitorLib (included as reference)
- Newtonsoft.Json (installed via NuGet)
- Visual Studio 2019 or newer

### 🌐 Server API (backend)
- Java 11+
- Apache NetBeans (or any IDE with Jakarta EE / Jersey support)
- Maven
-REST API deployed on local or remote server (e.g., Tomcat, GlassFish)

---

## 📁 Project Structure
PCMonitorSuite/
├── client/ # Desktop application (Windows Forms, C#)
│ └── PCMonitor/ # Visual Studio project
├── server/ # REST API backend
│ └── PCMonitorAPI/ # NetBeans / Java project
└── README.md


---

## 🧩 Components

### 🖥️ Client Application – `PCMonitor`
- Written in **C#** (.NET)
- Uses **LibreHardwareMonitor** to read sensor data (CPU, GPU, RAM, etc.)
- Runs in the background as a **tray icon**
- Supports:
  - automatic data collection every 10 seconds
  - viewing diagnostics in a separate window
  - local logging
  - log cleanup
  - sending JSON data to REST API
- Mode: **Windows Forms**, optionally set to run at startup using Task Scheduler

### 🌐 Server – `PCMonitorAPI`
- REST API written in **Java (Jakarta EE / JAX-RS)**
- Receives diagnostic data in JSON format
- Can store data:
  - to a file
  - (optionally) to a database (not implemented in MVP)
- Provides simple endpoints:
  - `POST /api/data` – receive data from the client

---

## ⚙️ How to Run

### 🖥️ Client
1. Open the `PCMonitor` project in Visual Studio.
2. Build the project (`Ctrl + Shift + B`).
3. Run `PCMonitor.exe` from `bin/Debug` or `bin/Release`.

**Note:** The app creates a log file in the working directory: `PCMonitor.log`.

### 🌐 Server
1. Open the `PCMonitorAPI` project in Apache NetBeans.
2. Run it on a Java EE server (e.g., GlassFish or Payara).
3. Ensure the `/api/data` endpoint is available at `http://localhost:8080/api/data`.

---

## 🔒 Security (TODO)
- No authentication – data is sent in plain form.
- No encryption – consider using HTTPS and JWT in future versions.

---

## 🚀 Roadmap / Planned Features
- Export data to CSV
- System notifications on temperature thresholds
- Client-server authentication
- Web dashboard for historical data visualization

---

## 📃 License

For personal and educational use only. Not intended for production environments.

---

## ✍️ Authors




# Opis do wysłania

- Temat: System monitorowania danych diagnostycznych komputerów w sieci lokalnej.

- Skład zespołu: Paweł Kurek, Tomasz Wojtasek

- Wstępny opis:
System będzie składał się z komputerów z systemem Windows, zbierających dane diagnostyczne (takie jak temperatura CPU, użycie RAM, obciążenie CPU) oraz wysyłających te dane do centralnego serwera przez REST API. Serwer zapisze dane w bazie i udostępni je przez przeglądarkowy interfejs użytkownika (HTML/JS) w formie wykresów i tabel. 

