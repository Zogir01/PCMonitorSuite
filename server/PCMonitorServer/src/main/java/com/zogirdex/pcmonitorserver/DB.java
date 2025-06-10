package com.zogirdex.pcmonitorserver;

import java.util.List;
import java.util.Properties;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Join;
import org.h2.tools.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author tom3k
 */
public class DB {

	SessionFactory sessionFactory;
	Session session;

	public void connect() {
		Properties prop = new Properties();

		prop.setProperty("hibernate.connection.url", "jdbc:h2:file:./sensor_db");
		prop.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		prop.setProperty("connection.driver_class", "org.h2.Driver");
		prop.setProperty("hibernate.enable_lazy_load_no_trans", "true");
		prop.setProperty("hbm2ddl.auto", "update");
		//prop.setProperty("javax.persistence.schema-generation.database.action", "update");
		prop.setProperty("javax.persistence.schema-generation.database.action", "drop-and-create"); // drop-and-create - do testów
		prop.setProperty("hibernate.show_sql", "true");

		Configuration configuration = new Configuration().addProperties(prop);
		configuration.addAnnotatedClass(Computer.class);
		configuration.addAnnotatedClass(Sensor.class);
		configuration.addAnnotatedClass(SensorReading.class);
		sessionFactory = configuration.buildSessionFactory();
		session = sessionFactory.openSession();

		// Do testów bazy danych 
		try {
			Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8090").start();
		} catch (Exception ex) {

		}
	}

	public void addObject(Object object) {
		session.beginTransaction();
		session.save(object);
		session.getTransaction().commit();
	}

	public List<Computer> getAllComputers() {
		List<Computer> computers = session.createQuery("from Computer", Computer.class).list();
		return computers;
	}

	public List<Sensor> getAllSensors() {

		List<Sensor> sensors = session.createQuery("from Sensor", Sensor.class).list();
		return sensors;
	}

	public List<SensorReading> getAllSensorReadings() {

		List<SensorReading> sensorReadings = session.createQuery("from SensorReading", SensorReading.class).list();
		return sensorReadings;
	}

	public Computer getComputer(long id) {
		return session.get(Computer.class, id);
	}

	public Sensor getSensor(long id) {
		return session.get(Sensor.class, id);
	}

	public SensorReading getSensorReading(long id) {
		return session.get(SensorReading.class, id);
	}

	public void removeComputer(Computer computer) {
		session.beginTransaction();
		session.remove(computer);
		session.getTransaction().commit();
	}

	public void removeSensor(Sensor sensor) {
		session.beginTransaction();
		session.remove(sensor);
		session.getTransaction().commit();
	}

	public void removeSensorReading(SensorReading sensorReading) {
		session.beginTransaction();
		session.remove(sensorReading);
		session.getTransaction().commit();
	}

	public void updateComputer(Computer computer) {
		session.beginTransaction();
		session.persist(computer);
		session.getTransaction().commit();
	}

	public void updateSensor(Sensor sensor) {
		session.beginTransaction();
		session.persist(sensor);
		session.getTransaction().commit();
	}

	public void updateSensorReading(SensorReading sensorReading) {
		session.beginTransaction();
		session.persist(sensorReading);
		session.getTransaction().commit();
	}

	public void addComputer(Computer computer) {
		session.beginTransaction();
		session.save(computer);
		session.getTransaction().commit();
	}

	public void addSensor(Sensor sensor) {
		session.beginTransaction();
		session.save(sensor);
		session.getTransaction().commit();
	}

	public void addSensorReading(SensorReading sensorReading) {
		session.beginTransaction();
		session.save(sensorReading);
		session.getTransaction().commit();
	}

	public Sensor findOrCreateSensor(String hw, String subhw, String name, String type) {
		//session.beginTransaction();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Sensor> criteria = builder.createQuery(Sensor.class);
		Root<Sensor> root = criteria.from(Sensor.class);

		// Sprawdza czy istnieje rekord z takimi samymi parametrami (te parametry są unikalne dla sensorów)
//		System.out.println("hardwareName = " + root.get("hardwareName") + " subHardwareName = " + root.get("subHardwareName") + " sensorName =  " + root.get("sensorName") + " sensorType = " + root.get("sensorType"));
		
		Predicate predicate = builder.and(
			builder.equal(root.get("hardwareName"), hw),
			builder.equal(root.get("subHardwareName"), subhw),
			builder.equal(root.get("sensorName"), name),
			builder.equal(root.get("sensorType"), type)
		);

		criteria.select(root).where(predicate);

		List<Sensor> results = session.createQuery(criteria).getResultList();

		if (results.isEmpty()) {
			Sensor newSensor = new Sensor(hw, subhw, name, type);
			//this.updateSensor(newSensor);
			this.addSensor(newSensor);
			//session.save(newSensor); // zamiast addSensor
			// lub: session.persist(newSensor);
			//session.flush(); // <- bardzo ważne!
			//session.getTransaction().commit();
			return newSensor;
		}
		//session.getTransaction().commit();
		return results.get(0);
	}

	public Computer findOrCreateComputer(String name) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Computer> criteria = builder.createQuery(Computer.class);
		Root<Computer> root = criteria.from(Computer.class);

		// Sprawdza czy istnieje rekord komputera z taką samą nazwą (nazwy komputerów w systemie powinny być unikalne)
		criteria.select(root).where(builder.equal(root.get("computerName"), name));

		List<Computer> results = session.createQuery(criteria).getResultList();

		if (results.isEmpty()) {
			Computer newComputer = new Computer(name);
			//this.updateComputer(newComputer);
			this.addComputer(newComputer);
			return newComputer;
		} else {
			return results.get(0);
		}
	}
	
	public List<SensorReading> findSensorReadings(long computerId, long sensorId) {
		 CriteriaBuilder builder = session.getCriteriaBuilder();
		 CriteriaQuery<SensorReading> criteria = builder.createQuery(SensorReading.class);
		 Root<SensorReading> root = criteria.from(SensorReading.class);

		 // Dołączenie encji Computer i Sensor
		 Join<Object, Object> computerJoin = root.join("computer");
		 Join<Object, Object> sensorJoin = root.join("sensor");

		 // Warunki WHERE: porównanie id
		 Predicate computerPredicate = builder.equal(computerJoin.get("id"), computerId);
		 Predicate sensorPredicate = builder.equal(sensorJoin.get("id"), sensorId);

		 criteria.select(root).where(builder.and(computerPredicate, sensorPredicate));

		 return session.createQuery(criteria).getResultList();
	}

	public void disconnect() {
		session.close();
		sessionFactory.close();
	}
}
