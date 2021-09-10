package org.acme.lifecycle;

import database.DatabaseHandler;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class AppLifecycleBean {

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    @Inject
    DatabaseHandler databaseHandler;

    void onStart(@Observes StartupEvent ev) {
        boolean result = databaseHandler.establishConnection();
        if (result)
            LOGGER.info("Connection established correctly.");
        else {
            LOGGER.info("Could not establish connection.");
            System.exit(0);
        }
    }

    /*void onStop(@Observes ShutdownEvent ev) {
        *//*databaseHandler.closeConnection();*//*
        LOGGER.info("Connection with the database closed.");
        System.exit(0);
    }*/

}