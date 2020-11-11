package sample.controllers;

import java.sql.Connection;
import java.sql.DriverManager;

public class dataBase {
    private static dataBase db;
    private Connection con;
    private String parentID;
    private String loginType;
    private String loginID;
    private String loginName;

    private dataBase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/4Lab", "Lab", "Lab");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static dataBase getInstance() {
        if (db == null) {
            db = new dataBase();
        }
        return db;
    }

    public Connection getConnection() {
        return con;
    }

    public String setParentID(String ID) {
        return this.parentID = ID;
    }

    public String getParentID() {
        return parentID;
    }

    public void setLoginType(String type) {
        this.loginType = type;
    }

    public void setLoginID(int loginID) {
        String ID = Integer.toString(loginID);
        this.loginID = ID;
    }

    public void setLoginName(String name) {
        this.loginName = name;
    }

    public String getLoginID() {
        return this.loginID;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public String getLoginType() {
        return this.loginType;
    }
}
