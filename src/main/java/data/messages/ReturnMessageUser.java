package data.messages;

import models.User;

import java.util.List;

public class ReturnMessageUser {
    private final String message;
    private final List<User> result;
    private final boolean isValid;

    public String getMessage() {
        return message;
    }

    public List<User> getResult() {
        return result;
    }

    public boolean isValid() {
        return isValid;
    }

    public ReturnMessageUser(String message, List<User> result, boolean isValid) {
        this.message = message;
        this.result = result;
        this.isValid = isValid;
    }
}