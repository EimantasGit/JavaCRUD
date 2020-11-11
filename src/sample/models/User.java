package sample.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class User {
    //private static int counter = 0;
    protected Integer userID;
    protected StringProperty username;
    protected StringProperty password;

    public User() {

    }

    public User(String username, String password, int ID) {
        this.userID = ID;
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
    }
    public String getUsername(){
        return this.getUsername();
    }
    public String getPassword(){
        return this.getPassword();
    }
    public Integer getUserID(){
        return this.userID;
    }
}
