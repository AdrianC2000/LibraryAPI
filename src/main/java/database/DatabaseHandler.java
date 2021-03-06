package database;

import data.Parsers;
import data.messages.ReturnMessage;
import data.Validators;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class DatabaseHandler {

    @Inject
    Connection connection;

    @Inject
    Logger logger;

    @Inject
    Parsers parsers;

    @Inject
    Validators validators;

    public ReturnMessage updateResource(String tableName, Integer id, String parameter, String valueToSet) {
        if (parameter.contains("ID")) {
            return new ReturnMessage("You can't edit the ID field.", false);
        }
        if (valueToSet.equals("null")) {
            valueToSet = null;
        }
        try {
            String IDname = "ID_" + tableName.substring(0, tableName.length() - 1);
            if (parameter.contains("taken_by") && valueToSet != null) {
                try {
                    if (!validators.resourceExistence(tableName, Integer.parseInt(valueToSet), connection)) {
                        throw new SQLException("Database error: " + parsers.resourceName(tableName) + " with the ID " + id + " does not exist.");
                    }
                } catch (NumberFormatException exc) {
                    logger.error(exc.getMessage());
                    throw new SQLException("Wrong taken by value format - it has to be an positive integer or null.");
                }
            }
            if (validators.resourceExistence(tableName, id, connection)) {
                PreparedStatement preparedStmt = connection.prepareStatement("UPDATE " + tableName + " SET " + parameter + " = ? WHERE " + IDname + " = ? ");
                preparedStmt.setString(1, valueToSet);
                preparedStmt.setInt(2, id);
                preparedStmt.execute();
                logger.info("Querying " + parsers.getQuery(preparedStmt));
                return new ReturnMessage("Parameter " + parameter + " changed for " + valueToSet + " for " + parsers.resourceName(tableName).toLowerCase() + " with the id " + id + " correctly.", true);
            } else {
                return new ReturnMessage(parsers.resourceName(tableName) + " with the id " + id + " does not exist.", false);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return new ReturnMessage("Database error: " + e.getMessage(), false);
        }
    }

    public ReturnMessage deleteResource(String tableName, Integer id) {
        if (validators.resourceExistence(tableName, id, connection)) {
            try {
                String IDname = "ID_" + tableName.substring(0, tableName.length() - 1);

                PreparedStatement preparedStmt = connection.prepareStatement("DELETE FROM " + tableName + " WHERE " + IDname + " = ?");
                preparedStmt.setInt(1, id);
                preparedStmt.execute();
                logger.info("Querying " + parsers.getQuery(preparedStmt));
                return new ReturnMessage("User with the id " + id + " deleted correctly.", true);
            } catch (SQLException e) {
                logger.error(e.getMessage());
                return new ReturnMessage("Error: " + e.getMessage(), false);
            }
        }
        return new ReturnMessage(parsers.resourceName(tableName) +
                " with the id " + id + " does not exist.", false);
    }

    public ResultSet filterResource(String tableName, String logic, HashMap<String, String[]> requirementsMap) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");
        for (Map.Entry<String, String[]> entry : requirementsMap.entrySet()) {
            String parameter = entry.getKey();
            String[] values = entry.getValue();
            if (values != null) {
                if (values.length > 1) {
                    for (int i = 0; i < values.length; i++) {
                        if (i == 0) {
                            query.append(parameter).append(" IN (?, ");
                        } else if (i == values.length - 1) {
                            query.append("?)");
                        } else {
                            query.append("?, ");
                        }
                    }
                } else {
                    query.append(parameter).append(" IN (?)");
                }
                query.append(" ").append(logic).append(" ");
            }
        }
        String queryString = "";
        if (logic.equals("AND")) {
            queryString = query.substring(0, query.length() - 5);
        } else if (logic.equals("OR")) {
            queryString = query.substring(0, query.length() - 4);
        }
        PreparedStatement preparedStmt = connection.prepareStatement(queryString);
        int index = 1;
        for (Map.Entry<String, String[]> entry : requirementsMap.entrySet()) {
            String[] values = entry.getValue();
            if (values != null) {
                for (String value : values) {
                    preparedStmt.setString(index, value);
                    index++;
                }
            }
        }
        logger.info("Querying " + parsers.getQuery(preparedStmt));
        return preparedStmt.executeQuery();
    }

}