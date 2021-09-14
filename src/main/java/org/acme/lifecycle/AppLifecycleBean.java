/*
package org.acme.lifecycle;

import database.DatabaseHandler;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.ws.rs.Produces;

@ApplicationScoped
public class AppLifecycleBean {

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    private static final Logger LOG = LoggerFactory.getLogger(AppLifecycleBean.class);

    @Inject
    DatabaseHandler databaseHandler;

    void onStart(@Observes StartupEvent ev) {
        boolean result = databaseHandler.establishConnection();
        if (result)
            LOG.info("Connection established correctly.");
        else {
            LOG.info("Could not establish connection.");
            System.exit(0);
        }
    }

    */
/*void onStop(@Observes ShutdownEvent ev) {
        *//*
*/
/*databaseHandler.closeConnection();*//*
*/
/*
        LOGGER.info("Connection with the database closed.");
        System.exit(0);
    }*//*


}*/
