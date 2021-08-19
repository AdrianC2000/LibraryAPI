package data;

import models.Book;
import models.BookRequirements;
import models.User;
import models.UserRequirements;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Parsers {

    /* Methods for the user */

    public static List<User> parseResultSetIntoUserList(ResultSet result) throws SQLException {
        List<User> listOfUsers = new ArrayList<>();
        while (result.next()) {
            Integer ID = result.getInt(1);
            String login = result.getString(2);
            String email = result.getString(3);
            String firstName = result.getString(4);
            String secondName = result.getString(5);
            Date date = result.getDate(6);

            User actualUser = new User(ID, login, email, firstName, secondName, date);
            listOfUsers.add(actualUser);
        }
        return listOfUsers;
    }

    public static PreparedStatement prepareUser(PreparedStatement preparedStmt, User newUser) throws SQLException {
        String login = newUser.getLogin();
        preparedStmt.setString(1, login);
        String email = newUser.getEmail();
        preparedStmt.setString(2, email);
        String firstName = newUser.getFirst_name();
        preparedStmt.setString(3, firstName);
        String secondName = newUser.getLast_name();
        preparedStmt.setString(4, secondName);
        Date date = newUser.getCreation_date();
        java.sql.Date dateSQL;
        if (date != null) {
            dateSQL = new java.sql.Date(date.getTime());
        } else {
            dateSQL = null;
        }

        preparedStmt.setDate(5, dateSQL);
        return preparedStmt;
    }

    public static HashMap<String, String[]> parseUserRequirementsIntoHashMap(UserRequirements requirements) {
        HashMap<String, String[]> requirementsMap = new HashMap<>();
        requirementsMap.put("ID_user", requirements.getId_user());
        requirementsMap.put("login", requirements.getLogin());
        requirementsMap.put("email", requirements.getEmail());
        requirementsMap.put("first_name", requirements.getFirst_name());
        requirementsMap.put("last_name", requirements.getLast_name());
        requirementsMap.put("creation_date", requirements.getCreation_date());
        return requirementsMap;
    }

    /* Methods for the books */

    public static List<Book> parseResultSetIntoBookList(ResultSet result) throws SQLException {
        List<Book> listOfBooks = new ArrayList<>();
        while (result.next()) {
            Integer ID = result.getInt(1);
            String title = result.getString(2);
            String author = result.getString(3);
            Integer isTaken = result.getInt(4);
            Integer takenBy = result.getInt(5);
            Date takenDate = result.getDate(6);
            Date returnDate = result.getDate(7);

            Book actualBook = new Book(ID, title, author, isTaken, takenBy, takenDate, returnDate);
            listOfBooks.add(actualBook);
        }
        return listOfBooks;
    }

    public static PreparedStatement prepareBook(PreparedStatement preparedStmt, Book newBook) throws SQLException {
        String title = newBook.getTitle();
        preparedStmt.setString(1, title);
        String author = newBook.getAuthor();
        preparedStmt.setString(2, author);
        int isTaken = newBook.getIs_taken() ? 1 : 0;
        preparedStmt.setInt(3, isTaken);
        int takenBy = newBook.getTaken_by();
        preparedStmt.setInt(4, takenBy);
        Date takenDate = newBook.getTaken_date();
        java.sql.Date takenDateSQL;
        if (takenDate != null) {
            takenDateSQL = new java.sql.Date(takenDate.getTime());
        } else {
            takenDateSQL = null;
        }
        preparedStmt.setDate(5, takenDateSQL);
        Date returnDate = newBook.getReturn_date();
        java.sql.Date returnDateSQL;
        if (returnDate != null) {
            returnDateSQL = new java.sql.Date(returnDate.getTime());
        } else {
            returnDateSQL = null;
        }

        preparedStmt.setDate(6, returnDateSQL);
        return preparedStmt;
    }

    public static HashMap<String, String[]> parseBookRequirementsIntoHashMap(BookRequirements requirements) {
        HashMap<String, String[]> requirementsMap = new HashMap<>();
        requirementsMap.put("ID_book", requirements.getID_book());
        requirementsMap.put("title", requirements.getTitle());
        requirementsMap.put("author", requirements.getAuthor());
        requirementsMap.put("is_taken", requirements.getIs_taken());
        requirementsMap.put("taken_by", requirements.getTaken_by());
        requirementsMap.put("taken_date", requirements.getTaken_date());
        requirementsMap.put("return_date", requirements.getReturn_date());

        return requirementsMap;
    }

    /* Universal methods */

    public static ArrayList<String> parseFieldsArrayIntoStringList(Field[] fields) {
        ArrayList<String> stringList = new ArrayList<>();
        for (Field field : fields) {
            stringList.add(field.getName());
        }
        return stringList;
    }

    public static String resourceName(String tableName) {
        // Example: tableName: users, resourceName = User
        String resource = tableName.substring(0, tableName.length() - 1);
        return resource.substring(0, 1).toUpperCase() + resource.substring(1);
    }
}