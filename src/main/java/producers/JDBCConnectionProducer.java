package producers;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnectionProducer {

    @Produces
    protected Connection createConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/sys";
        String user = "Adrian";
        String password = "DatabasePassword123";
        return DriverManager.getConnection(url, user, password);
    }
    protected void closeConnection(@Disposes Connection connection) throws SQLException {
        connection.close();
    }

}