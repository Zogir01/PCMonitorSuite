/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zogirdex.pcmonitorserver;

import java.util.List;
import java.util.Properties;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.h2.tools.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/*
WAŻNA KONWERSACJA
https://chatgpt.com/c/6832f256-2064-8003-8a9e-8257e7d9cb36
*/

/**
 *
 * @author tom3k
 */
public class DB {
    
    SessionFactory sessionFactory;
    Session session;
    
    public void connect(){
        Properties prop = new Properties();

        prop.setProperty("hibernate.connection.url", "jdbc:h2:file:./baza_sensorow");
        prop.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        prop.setProperty("connection.driver_class", "org.h2.Driver");
        prop.setProperty("hibernate.enable_lazy_load_no_trans", "true");
        prop.setProperty("hbm2ddl.auto", "update");
        prop.setProperty("javax.persistence.schema-generation.database.action", "update");
        prop.setProperty("hibernate.show_sql", "true");

        Configuration configuration = new Configuration().addProperties(prop);
        configuration.addAnnotatedClass(Sensor.class);
        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
        
        // Do testów bazy danych 
        try {
            Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        }
        catch(Exception ex) {
            
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
    
    public List<Ocena> getOcenyByPrzedmiot(String przedmiot){
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Ocena> criteria = builder.createQuery(Ocena.class);
        Root<Ocena> root = criteria.from(Ocena.class);

        criteria.where(builder.equal(root.get("przedmiot"), przedmiot));
//        criteria.where(builder.and(builder.equal(root.get("przedmiot"), "POiW"), builder.gt(root.get("ocena"), 3.5)));


//        criteria.orderBy(builder.asc(root.get("ocena")));
        List<Ocena> oceny = session.createQuery(criteria).getResultList();
        
        return oceny;
    }
    
    public List<Ocena> getOcenyPozByPrzedmiot(String przedmiotStr){
        
        Przedmiot przedmiot = session.get(Przedmiot.class, przedmiotStr);
        
        if(przedmiot!=null){
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Ocena> criteria = builder.createQuery(Ocena.class);
        Root<Ocena> root = criteria.from(Ocena.class);

//        criteria.where(builder.equal(root.get("przedmiot"), przedmiot));
        criteria.where(builder.and(builder.equal(root.get("przedmiot"), przedmiot), builder.gt(root.get("ocena"), 2.0)));


//        criteria.orderBy(builder.asc(root.get("ocena")));
        List<Ocena> oceny = session.createQuery(criteria).getResultList();
        
        return oceny;
        }
        return null;
    }
    
    public void disconnect(){
        session.close();
        sessionFactory.close();
    }
