package com.zogirdex.pcmonitorserver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author tom3k
 */
@WebServlet("/api/data/history/start")
public class DataHistoryStartServlet extends HttpServlet {

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// odczytaj sensor z bazy
		DB db = (DB) getServletContext().getAttribute("db");
		List<Computer> computers = db.getAllComputers();

		// Trzeba jakoś wyciągnąć wszystkie sensory dla danych komputerów
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet Start</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Historia komputera</h1>");
			out.println("<br>");
			out.println("<button onclick=\"location.reload()\">Odśwież</button>");
			out.println("<br><br>");

			// Tu jest jakaś przykładowa pętla - TO DO (trzeba wyświetlać sensory dla każego komputera i każdemu nadać przycisk)
			for (Computer computer : computers) {
				out.println("<h2>" + computer.getComputerName() + "</h2>");

				// Zbiór unikalnych sensorów (na podstawie powiązanych odczytów)
				Set<Sensor> uniqueSensors = new HashSet<>();
				for (SensorReading reading : computer.getReadings()) {
					if (reading.getSensor() != null) {
						uniqueSensors.add(reading.getSensor());
					}
				}

				for (Sensor sensor : uniqueSensors) {
					out.println("<p>" + sensor.getSensorName()
						+ " <form action='/api/data/history/show' method='get' style='display:inline;'>"
						+ "<input type='hidden' name='computerId' value='" + computer.getId() + "'>"
						+ "<input type='hidden' name='sensorId' value='" + sensor.getId() + "'>"
						+ "<input type='submit' value='pokaż historię'>"
						+ "</form></p>"
					);
				}
			}

			out.println("</body>");
			out.println("</html>");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
