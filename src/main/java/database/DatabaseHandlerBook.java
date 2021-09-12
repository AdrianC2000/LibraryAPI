package database;

import data.*;
import data.messages.ReturnMessage;
import data.messages.ReturnMessageBook;
import models.Book;
import models.BookRequirements;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.*;
import java.util.*;

@ApplicationScoped
public class DatabaseHandlerBook {

    @Inject
    DatabaseHandler databaseHandler;

    @Inject
    Parsers parsers;

    @Inject
    Validators validators;

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ReturnMessageBook getBooks(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM " + tableName);
        System.out.println("querying SELECT * FROM " + tableName);
        return new ReturnMessageBook("OK", parsers.parseResultSetIntoBookList(result), true);
    }

    public ReturnMessage addBook(String tableName, Book newBook) {
        boolean areFieldsValid = validators.fieldsValidationBook(newBook);
        if (areFieldsValid) {
            PreparedStatement preparedStmt;
            try {
                String query = "INSERT INTO " + tableName + " (title, author, is_taken, taken_by, taken_date, return_date) VALUES (?, ?, ?, ?, ?, ?)";
                preparedStmt = connection.prepareStatement(query);
                System.out.println(parsers.prepareBook(preparedStmt, newBook));
                parsers.prepareBook(preparedStmt, newBook).execute();
                System.out.println("querying INSERT INTO " + tableName);
                return new ReturnMessage("Resource added correctly.", true);
            } catch (SQLException e) {
                return new ReturnMessage("Database error: " + e.getMessage() + ".\nCheck your input for typos.", false);
            }
        } else {
            return new ReturnMessage("Fields error: Check your input for typos or forgotten parameters.", false);
        }
    }

    public ReturnMessageBook filterBook(String tableName, BookRequirements allRequirements, String logic) {
        try {
            HashMap<String, String[]> requirementsMap = parsers.parseBookRequirementsIntoHashMap(allRequirements);
            ResultSet result = databaseHandler.filterResource(tableName, logic, requirementsMap);
            return new ReturnMessageBook("OK", parsers.parseResultSetIntoBookList(result), true);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ReturnMessageBook("Database error: " + e.getMessage(), null, false);
        }
    }
}