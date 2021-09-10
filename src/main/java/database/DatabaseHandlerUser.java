package database;

import data.Parsers;
import data.messages.ReturnMessage;
import data.messages.ReturnMessageUser;
import data.Validators;
import models.User;
import models.UserRequirements;
import javax.inject.Inject;

import javax.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.*;

@ApplicationScoped
public class DatabaseHandlerUser {

    @Inject
    DatabaseHandler databaseHandler;
    Parsers parsers;
    Validators validators;

    private final Connection connection = databaseHandler.getConnection();

    public ReturnMessageUser getUsers(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM " + tableName);
        System.out.println("querying SELECT * FROM " + tableName);
        return new ReturnMessageUser("OK", parsers.parseResultSetIntoUserList(result), true);
    }

    public ReturnMessage addUser(String tableName, User newUser) {
        boolean areFieldsValid = validators.fieldsValidationUser(newUser);
        if (areFieldsValid) {
            PreparedStatement preparedStmt;
            try {
                String query = "INSERT INTO " + tableName + " (login, email, first_name, last_name, creation_date) VALUES (?, ?, ?, ?, ?)";
                preparedStmt = connection.prepareStatement(query);
                System.out.println(parsers.prepareUser(preparedStmt, newUser));
                parsers.prepareUser(preparedStmt, newUser).execute();
                System.out.println("querying INSERT INTO " + tableName);
                return new ReturnMessage("Resource added correctly.", true);
            } catch (SQLException e) {
                return new ReturnMessage("Database error: " + e.getMessage() + ".\nCheck your input for typos.", false);
            }
        } else {
            return new ReturnMessage("Fields error: Check your input for typos or forgotten parameters.", false);
        }
    }

    public ReturnMessageUser filterUser(String tableName, UserRequirements allRequirements, String logic) {
        try {
            HashMap<String, String[]> requirementsMap = parsers.parseUserRequirementsIntoHashMap(allRequirements);
            ResultSet result = databaseHandler.filterResource(tableName, logic, requirementsMap);
            return new ReturnMessageUser("OK", parsers.parseResultSetIntoUserList(result), true);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ReturnMessageUser("Database error: " + e.getMessage(), null, false);
        }
    }

}