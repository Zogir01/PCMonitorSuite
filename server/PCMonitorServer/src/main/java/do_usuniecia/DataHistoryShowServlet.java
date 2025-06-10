//package com.zogirdex.pcmonitorserver;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.annotation.WebServlet;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;
//
///**
// *
// * @author tom3k
// * 
// * DO USUNIĘCIA
// * 
// * 
// */
//@WebServlet("/api/data/history/show")
//public class DataHistoryShowServlet extends HttpServlet {
//
//	private void printTable(PrintWriter out, List<SensorReading> readings) {
//		out.println("<table>");
//		out.println("<tr>");
//		out.println("<th>Value</th>");
//		out.println("</tr>");
//		for (SensorReading sr : readings) {
//			out.println("<tr>");
//			out.println("<td>" + sr.getValue() + "</td>");
//			out.println("</tr>");
//		}
//		out.println("</table>");
//		out.println("<br><br>");
//	}
//	
//	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//	@Override
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		
//		DB db = (DB) getServletContext().getAttribute("db");
//		String computerIdReqParam = request.getParameter("ComputerId");
//		String sensorIdReqParam = request.getParameter("SensorId");
//		
//		try (PrintWriter out = response.getWriter()) {
//			out.println("<!DOCTYPE html>");
//			out.println("<html>");
//			out.println("<head>");
//			out.println("<title>Servlet Start</title>");
//			out.println("</head>");
//			out.println("<body>");
//			
//			if(computerIdReqParam == null || sensorIdReqParam == null) {
//				out.println("Brak pomiarów - błąd w podanych parametrach.");
//				out.println("<br>");
//				out.println("</body>");
//				out.println("</html>");
//				return;
//			}
//			
//			long computerId = Long.parseLong(computerIdReqParam);
//			long sensorId = Long.parseLong(sensorIdReqParam);
//			
//			Computer computer = db.getComputer(computerId);
//			Sensor sensor = db.getSensor(sensorId);
//			
//			out.println("<br>");
//			out.println("Wyświetlam historię dla podanego komputera i sensora:");
//			out.println("<br>");
//			out.println("Komputer = " + computer.toString());
//			out.println("<br>");
//			out.println("Sensor = " + sensor.toString());
//			out.println("<br>");
//			out.println("<button onclick=\"location.reload()\">Odśwież</button>");
//			out.println("<br>");
//			
//			printTable(out, db.findSensorReadings(computerId, sensorId));
//
//			out.println("</body>");
//			out.println("</html>");
//		}
//	}
//
//	@Override
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//	}
//
//	/**
//	 * Returns a short description of the servlet.
//	 *
//	 * @return a String containing servlet description
//	 */
//	@Override
//	public String getServletInfo() {
//		return "Short description";
//	}// </editor-fold>
//}
