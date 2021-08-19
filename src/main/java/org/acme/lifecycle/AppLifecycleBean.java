package org.acme.lifecycle;

import database.DatabaseHandler;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class AppLifecycleBean {

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    void onStart(@Observes StartupEvent ev) {
        boolean result = DatabaseHandler.establishConnection();
        if (result)
            LOGGER.info("Connection established correctly.");
        else {
            LOGGER.info("Could not establish connection.");
            System.exit(0);
        }
    }

    /*void onStop(@Observes ShutdownEvent ev) {
        *//*DatabaseHandler.closeConnection();*//*
        LOGGER.info("Connection with the database closed.");
        System.exit(0);
    }*/

}