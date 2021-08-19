package models;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    private Integer ID_user;

    @Pattern(regexp = "^\\S+$", message = "Format error: Login cannot contain spaces and cannot be empty.")
    private String login;

    @Pattern(regexp = "^[\\w]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Format error: Incorrect email format.")
    private String email;

    @Pattern(regexp = "^[\\p{L}]+$", message = "Format error: First name can only contain letters and cannot be empty.")
    private String first_name;


    @Pattern(regexp = "^[\\p{L}]+$", message = "Format error: Last name can only contain letters and cannot be empty.")
    private String last_name;

    @PastOrPresent (message = "Format error: Creation date must be past or present.")
    private Date creation_date;


    public User(Integer ID_user, String login, String email, String first_name, String last_name, Date creation_date) {
        this.ID_user = ID_user;
        this.login = login;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.creation_date = creation_date;
    }

    public Integer getID_user() {
        return ID_user;
    }

    public void setID_user(String ID_user) {
        this.ID_user = Integer.valueOf(ID_user);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Date getCreation_date() {
        return this.creation_date;
    }

    public void setCreation_date(String creation_date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            this.creation_date = formatter.parse(creation_date);
        } catch (ParseException | NullPointerException e) {
            // If incorrect format or null, set date to null
            this.creation_date = null;
        }
    }

    public String[] allRequiredData() {
        return new String[]{this.login, this.email, this.first_name, this.last_name};
    }
}
