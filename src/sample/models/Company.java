package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sample.models.User;

public class Company extends User {
    private final StringProperty companyName;
    private final StringProperty companyRep;

    public Company(int ID, String username, String password, String companyName, String companyRep) {
        super(username, password, ID);
        this.companyName = new SimpleStringProperty(companyName);
        this.companyRep = new SimpleStringProperty(companyRep);
    }

    public String getCompanyName() { return this.companyName.get(); }
    public String getCompanyRep() {
        return this.companyRep.get();
    }
    public String getUsername() { return super.username.get(); }
    public String getPassword() { return super.password.get(); }
    public Integer getUserID(){ return super.userID; }

    public void setCompanyName(String name){
        this.companyName.set(name);
    }
    public void setCompanyRep(String rep){
        this.companyRep.set(rep);
    }
    public void setUsername(String user){
        super.username.set(user);
    }
    public void setPassword(String pass){
        super.password.set(pass);
    }
}
