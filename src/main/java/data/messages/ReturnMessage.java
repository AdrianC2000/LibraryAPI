package data.messages;

public class ReturnMessage {
    private String message;
    private boolean isValid;

    public String getMessage() {
        return message;
    }

    public boolean isValid() {
        return isValid;
    }

    public ReturnMessage(String message, boolean isValid) {
        this.message = message;
        this.isValid = isValid;
    }
}