package models;

public class UserRequirements {

    private String[] id_user;
    private String[] login;
    private String[] email;
    private String[] first_name;
    private String[] last_name;
    private String[] creation_date;

    public String[] getId_user() {
        return id_user;
    }

    public void setId_user(String[] id_user) {
        this.id_user = id_user;
    }

    public String[] getLogin() {
        return login;
    }

    public void setLogin(String[] login) {
        this.login = login;
    }

    public String[] getEmail() {
        return email;
    }

    public void setEmail(String[] email) {
        this.email = email;
    }

    public String[] getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String[] first_name) {
        this.first_name = first_name;
    }

    public String[] getLast_name() {
        return last_name;
    }

    public void setLast_name(String[] last_name) {
        this.last_name = last_name;
    }

    public String[] getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String[] creation_date) {
        this.creation_date = creation_date;
    }
}
