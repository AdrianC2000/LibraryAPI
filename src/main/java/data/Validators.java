package data;

import com.google.gson.GsonBuilder;
import models.Book;
import models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.enterprise.context.ApplicationScoped;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class Validators {

    public static boolean resourceExistence(String table, Integer id, Connection connection) {
        // If SQLException appears, ID does not exist
        try {
            String IDname = "ID_" + table.substring(0, table.length() - 1);
            PreparedStatement preparedStmt = connection.prepareStatement("SELECT 1 FROM " + table + " WHERE " + IDname + " = ?");
            preparedStmt.setInt(1, id);
            ResultSet result = preparedStmt.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean fieldsValidationUser (User newUser) {
        Field[] fields = User.class.getDeclaredFields();
        ArrayList<String> fieldsList = Parsers.parseFieldsArrayIntoStringList(fields);

        Gson gson = new GsonBuilder().create();
        String tmp = gson.toJson(newUser);
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> map = gson.fromJson(tmp, type);
        Set<String> fieldSet = map.keySet();

        // Check if 4 or 5 field are set - if not, the input is invalid
        if (fieldSet.size() < fieldsList.size() - 2) {
            return false;
        }

        // Check if any of required parameter is not null - if it is, the input is invalid
        String[] requiredParameters = newUser.allRequiredData();
        for (String parameter : requiredParameters) {
            if (parameter == null) {
                return false;
            }
        }

        // Check if any of the field is not the ID field - if it is, the input is invalid
        for (String fieldName : fieldSet) {
            if (fieldName.contains("ID")) {
                return false;
            }
        }
        return true;
    }

    public static boolean fieldsValidationBook (Book newBook) {
        Field[] fields = Book.class.getDeclaredFields();
        ArrayList<String> fieldsList = Parsers.parseFieldsArrayIntoStringList(fields);

        Gson gson = new GsonBuilder().create();
        String tmp = gson.toJson(newBook);
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> map = gson.fromJson(tmp, type);
        Set<String> fieldSet = map.keySet();

        // Check if any of required parameter is not null - if it is, the input is invalid
        String[] requiredParameters = newBook.allRequiredData();
        for (String parameter : requiredParameters) {
            if (parameter == null) {
                return false;
            }
        }

        // Check if any of the field is not the ID field - if it is, the input is invalid
        for (String fieldName : fieldSet) {
            if (fieldName.contains("ID")) {
                return false;
            }
        }
        return true;
    }
}