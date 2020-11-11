package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sample.models.User;

public class Person extends User {
    private StringProperty name;
    private StringProperty lastName;
    private StringProperty phoneNumber;
    private StringProperty mail;

    public Person(int userID, String user, String pass, String name, String lastName, String phoneNumber, String mail){
        super(user, pass, userID);
        this.name = new SimpleStringProperty(name);
        this.lastName = new SimpleStringProperty(lastName);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.mail = new SimpleStringProperty(mail);
    }

    public String getName() { return name.get(); }
    public String getLastName() { return lastName.get(); }
    public String getPhoneNumber() { return phoneNumber.get(); }
    public String getMail() { return mail.get(); }
    public String getUsername(){ return super.username.get(); }
    public String getPassword(){ return super.password.get(); }
    public Integer getUserID(){ return super.userID; }

    public void setName(String name) { this.name.set(name); }
    public void setLastName(String lastName) { this.lastName.set(lastName); }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber.set(phoneNumber); }
    public void setMail(String mail) { this.mail.set(mail); }
    public void setUsername(String user) { super.username.set(user); }
    public void setPassword(String pass) { super.password.set(pass); }

}
