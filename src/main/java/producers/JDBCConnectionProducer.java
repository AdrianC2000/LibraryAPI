package producers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.slf4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RequestScoped
public class JDBCConnectionProducer {

    @Inject
    Logger logger;

    @Produces
    protected Connection createConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/sys";
        String user = "Adrian";
        String password = "DatabasePassword123";
        logger.info("Connection opened.");
        return DriverManager.getConnection(url, user, password);
    }
    protected void closeConnection(@Disposes Connection connection) throws SQLException {
        logger.info("Connection closed.");
        connection.close();
    }

}