package com.zogirdex.pcmonitorserver;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tom3k
 */
@WebListener
public class MyServletContextListener implements ServletContextListener {

    DB db;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (db != null) {
            db.disconnect();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        try {
            Class.forName("org.h2.Driver");
            
            db = new DB();
            db.connect();
            sce.getServletContext().setAttribute("db", db);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
