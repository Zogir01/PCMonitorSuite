package com.zogirdex.pcmonitorserver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

/**
 *
 * @author tom3k
 * 
 * Na POST odczytuje dane diagnostyczne (w postaci JSON) od clienta
 * Na GET umożliwia wyświetlenie aktualnych danych przesyłanych do serwera dla wszystkich komputerów
 * 
 */
@WebServlet("/api/data")
public class DataReceiverServlet extends HttpServlet {

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	Map<String, MonitorDataPayloadDTO> latestSensorUploads = new HashMap<String, MonitorDataPayloadDTO>();

	private void printTable(PrintWriter out, MonitorDataPayloadDTO upload) {
		out.println("<h2> ComputerName = " + upload.ComputerName + "</h2>");
		out.println("<table>");
		out.println("<tr>");
		out.println("<th>HardwareName</th>");
		out.println("<th>SubHardwareName</th>");
		out.println("<th>SensorName</th>");
		out.println("<th>SensorType</th>");
		out.println("<th>Value</th>");
		out.println("</tr>");
		for (MonitorDataDTO sensor : upload.Readings) {
			out.println("<tr>");
			out.println("<td>" + sensor.HardwareName + "</td>");
			out.println("<td>" + sensor.SubHardwareName + "</td>");
			out.println("<td>" + sensor.SensorName + "</td>");
			out.println("<td>" + sensor.SensorType + "</td>");
			out.println("<td>" + sensor.Value + "</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		out.println("<br><br>");
	}

	// Mapowanie DTO -> Encje Hibernate i zapis do DB
	private void saveComputerData(MonitorDataPayloadDTO dto, DB db) {
		Computer c = db.findOrCreateComputer(dto.ComputerName);

		for (MonitorDataDTO readingDto : dto.Readings) {
			Sensor s = db.findOrCreateSensor(readingDto.HardwareName, readingDto.SubHardwareName, readingDto.SensorName, readingDto.SensorType);
			//Sensor s = db.findOrCreateSensor("h", "sh", "s", "st");
			SensorReading sr = new SensorReading(s, readingDto.Value);
			sr.setSensor(s);
			sr.setComputer(c);
			c.addSensorReading(sr);
			s.addSensorReading(sr);
			db.addSensorReading(sr);
		}
	}

	private void printComputerData(MonitorDataPayloadDTO dto) {
		System.out.println("Otrzymano dane z komputera: " + dto.ComputerName);
		for (MonitorDataDTO reading : dto.Readings) {
			System.out.printf("  - %s | %s | %s | %s = %.2f\n",
				reading.HardwareName,
				reading.SubHardwareName,
				reading.SensorName,
				reading.SensorType,
				reading.Value);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet Start</title>");
			out.println("<style>");
			out.println("table {");
			out.println("border-collapse: collapse;");
			out.println("width: 50%;");
			out.println("margin-top: 20px;");
			out.println("}");
			out.println("th, td {");
			out.println("border: 1px solid #000;");
			out.println("padding: 8px 12px;");
			out.println("text-align: left;");
			out.println("}");
			out.println("th {");
			out.println("background-color: #f2f2f2;");
			out.println("}");
			out.println("</style>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Najnowsze dane diagnostyczne komputerów</h1>");
			out.println("<br>");
			out.println("<button onclick=\"location.reload()\">Odśwież</button>");
			out.println("<br><br>");

			if (latestSensorUploads.isEmpty()) { 
				// Brak danych w cache - serwer jeszcze nie odczytał danych od clienta                
				out.println("Brak aktualnych danych - uruchom PCMonitorClient");
				// Ew. można by odczytać najświeższe dane z bazy
			} 
			else { 
				// Wyswetli dane wszystkich komputerów zapisanych w cache
				for (MonitorDataPayloadDTO latestUpload : latestSensorUploads.values()) {
					printTable(out, latestUpload);
				}
			}
			out.println("</body>");
			out.println("</html>");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");

		try (BufferedReader reader = request.getReader(); PrintWriter out = response.getWriter()) {

			Gson gson = new Gson();

			// Parsowanie JSON do obiektu Java
			MonitorDataPayloadDTO dto = gson.fromJson(reader, MonitorDataPayloadDTO.class);
			latestSensorUploads.put(dto.ComputerName, dto);

			// Zapis do bazy danych
			DB db = (DB) getServletContext().getAttribute("db");
			saveComputerData(dto, db);

			// Wyświetlenie w celach testowych
			//printComputerData(dto);

			// Odpowiedź OK
			response.setStatus(HttpServletResponse.SC_OK);
			out.write("Zapisano dane.");

		} catch (Exception ex) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                try (PrintWriter out = response.getWriter()) {
//                    out.print("{\"status\":\"error\",\"message\":\"" + ex.getMessage() + "\"}");
//                }
			ex.printStackTrace();
		}
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}
