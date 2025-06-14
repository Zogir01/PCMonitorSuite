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
 */
@WebServlet("/test")
public class TestServlet extends HttpServlet {

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	
//	void printSensorReading(PrintWriter out, SensorReading sr) {
//		out.println("<br>");
//		out.println(sr.getId());
//		out.println(sr.getValue());
//		out.println("<br>");
//		out.println("Komputer tego pomiaru: " + sr.getComputer().toString()); 
//		out.println("<br>");
//		out.println("Sensor tego pomiaru: " + sr.getSensor().toString()); 
//		out.println("<br>");
//	}

	void printAllComputers(PrintWriter out, DB db) {
		out.println("<br>");
		out.println("Wszystkie komputery:"); 
		out.println("<br>");
		for(Computer c : db.getAllComputers()) {
			out.println(c.toString());
			out.println("<br>");
		}
	}
	
	void printAllSensors(PrintWriter out, DB db) {
		out.println("<br>");
		out.println("Wszystkie sensory:"); 
		out.println("<br>");
		for(Sensor s : db.getAllSensors()) {
			out.println(s.toString());
			out.println("<br>");
		}
	}
	
	void printAllSensorReadings(PrintWriter out, DB db) {
		out.println("<br>");
		out.println("Wszystkie pomiary:"); 
		out.println("<br>");
		for(SensorReading sr : db.getAllSensorReadings()) {
			//printSensorReading(out, sr);
			out.println(sr.toString());
			out.println("<br>");
		}
	}
	
	void printComputerSensorReadings(PrintWriter out, Computer c) {
		out.println("<br>");
		out.println("Wszystkie pomiary dla komputera " + c.getId() + ":"); 
		out.println("<br>");
		for(SensorReading sr : c.getReadings()) {
			//printSensorReading(out, sr);
			out.println(sr.toString());
			out.println("<br>");
		}
	}
	
	void printSensorSensorReadings(PrintWriter out, Sensor s) {
		out.println("<br>");
		out.println("Wszystkie pomiary dla sensora " + s.getId() + ":"); 
		out.println("<br>");
		for(SensorReading sr : s.getReadings()) {
			//printSensorReading(out, sr);
			out.println(sr.toString());
			out.println("<br>");
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
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Strona testowa</h1>");
			out.println("<br>");
			
			DB db = (DB) getServletContext().getAttribute("db");
			
			// TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST 
			
			// Test funkcji findOrCreateComputer
			Computer comp1 = db.findOrCreateComputer("testowy-komputer-1");
			Computer comp2 = db.findOrCreateComputer("testowy-komputer-1");
			printAllComputers(out, db);
			// Test funkcji findOrCreateComputer
			
			// Test dodawania nowych danych
			Sensor s1 = db.findOrCreateSensor("CPU", "CPU-1", "3VOUT", "Voltage");
			SensorReading sr1 = new SensorReading(s1, 3.11f, "2025-06-11T12:23:45.123Z");
			SensorReading sr2 = new SensorReading(s1, 3.22f, "2025-06-11T12:23:45.123Z");
			sr1.setSensor(s1);
			sr2.setSensor(s1);
			sr1.setComputer(comp2);
			sr2.setComputer(comp2);
			comp2.addSensorReading(sr1);
			comp2.addSensorReading(sr2);
			s1.addSensorReading(sr1);
			s1.addSensorReading(sr2);
			db.addSensorReading(sr1);
			db.addSensorReading(sr2);
			
			Sensor s2 = db.findOrCreateSensor("CPU", "CPU-1", "3VOUT", "Voltage");
			SensorReading sr3 = new SensorReading(s2, 3.33f, "2025-06-11T12:23:45.123Z");
			SensorReading sr4 = new SensorReading(s2, 3.41f, "2025-06-11T12:23:45.123Z");
			sr3.setSensor(s2);
			sr4.setSensor(s2);
			sr3.setComputer(comp2);
			sr4.setComputer(comp2);
			comp2.addSensorReading(sr3);
			comp2.addSensorReading(sr4);
			s2.addSensorReading(sr3);
			s2.addSensorReading(sr4);
			db.addSensorReading(sr3);
			db.addSensorReading(sr4);
			
			printAllSensors(out, db);
			printAllSensorReadings(out, db);
			printComputerSensorReadings(out, comp2);
			printSensorSensorReadings(out, s2);
			// Test dodawania nowych danych
			
			// TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST 
			
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
