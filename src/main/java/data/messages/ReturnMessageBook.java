package data.messages;

import models.Book;
import models.User;

import java.util.List;

public class ReturnMessageBook {
    private final String message;
    private final List<Book> result;
    private final boolean isValid;

    public String getMessage() {
        return message;
    }

    public List<Book> getResult() {
        return result;
    }

    public boolean isValid() {
        return isValid;
    }

    public ReturnMessageBook(String message, List<Book> result, boolean isValid) {
        this.message = message;
        this.result = result;
        this.isValid = isValid;
    }
}