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
                        WRÓC DO TEJ STRUKTURY:
https://chatgpt.com/c/6832f256-2064-8003-8a9e-8257e7d9cb36


-- Lista unikalnych sensorów (wspólna dla wszystkich komputerów)
CREATE TABLE Sensors (
    SensorId INT PRIMARY KEY IDENTITY,
    HardwareName NVARCHAR(100),
    SubHardwareName NVARCHAR(100),
    SensorName NVARCHAR(100),
    SensorType NVARCHAR(50)
);

-- Lista komputerów
CREATE TABLE Computers (
    ComputerId INT PRIMARY KEY IDENTITY,
    MachineName NVARCHAR(100) UNIQUE
);

-- Powiązanie komputer-sensor z wartościami w czasie
CREATE TABLE SensorReadings (
    ReadingId INT PRIMARY KEY IDENTITY,
    ComputerId INT FOREIGN KEY REFERENCES Computers(ComputerId),
    SensorId INT FOREIGN KEY REFERENCES Sensors(SensorId),
    Value FLOAT,
    Timestamp DATETIME
);
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
    
    public void addSensor(Sensor sensor){
        session.beginTransaction();
        session.save(sensor);
        session.getTransaction().commit();
    }
    
     public void addObject(Object object){
        session.beginTransaction();
        session.save(object);
        session.getTransaction().commit();
    }
    
    public List<Sensor> getAllSensors(){
        
        List<Student> studenci = session.createQuery("from Student", Student.class).list();
        return studenci;
    }
    
    public Student getStudent(long id){
        return session.get(Student.class, id);
    }
    
    public void removeStudent(Student student){
        session.beginTransaction();
        session.remove(student);
        session.getTransaction().commit();
    }
    
    public void updeateStudent(Student student){
        session.beginTransaction();
        session.persist(student);
        session.getTransaction().commit();
    }
    
    public void addOcena(Ocena ocena){
        session.beginTransaction();
        session.save(ocena);
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
