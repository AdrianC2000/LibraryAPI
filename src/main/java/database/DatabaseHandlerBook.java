package database;

import data.*;
import data.messages.ReturnMessage;
import data.messages.ReturnMessageBook;
import models.Book;
import models.BookRequirements;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.*;
import java.util.*;

@ApplicationScoped
public class DatabaseHandlerBook {

    @Inject
    Connection connection;

    @Inject
    Logger logger;

    @Inject
    DatabaseHandler databaseHandler;

    @Inject
    Parsers parsers;

    @Inject
    Validators validators;

    public ReturnMessageBook getBooks(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM " + tableName);
        logger.info("querying SELECT * FROM " + tableName);
        return new ReturnMessageBook("OK", parsers.parseResultSetIntoBookList(result), true);
    }

    public ReturnMessage addBook(String tableName, Book newBook) {
        boolean areFieldsValid = validators.fieldsValidationBook(newBook);
        if (areFieldsValid) {
            PreparedStatement preparedStmt;
            try {
                String query = "INSERT INTO " + tableName + " (title, author, is_taken, taken_by, taken_date, return_date) VALUES (?, ?, ?, ?, ?, ?)";
                preparedStmt = connection.prepareStatement(query);
                parsers.prepareBook(preparedStmt, newBook).execute();
                logger.info("Querying " + parsers.getQuery(preparedStmt));
                return new ReturnMessage("Resource added correctly.", true);
            } catch (SQLException e) {
                logger.error(e.getMessage());
                return new ReturnMessage("Database error: " + e.getMessage() + ".\nCheck your input for typos.", false);
            }
        } else {
            logger.error("Invalid fields.");
            return new ReturnMessage("Fields error: Check your input for typos or forgotten parameters.", false);
        }
    }

    public ReturnMessageBook filterBook(String tableName, BookRequirements allRequirements, String logic) {
        try {
            HashMap<String, String[]> requirementsMap = parsers.parseBookRequirementsIntoHashMap(allRequirements);
            ResultSet result = databaseHandler.filterResource(tableName, logic, requirementsMap);
            return new ReturnMessageBook("OK", parsers.parseResultSetIntoBookList(result), true);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return new ReturnMessageBook("Database error: " + e.getMessage(), null, false);
        }
    }
}