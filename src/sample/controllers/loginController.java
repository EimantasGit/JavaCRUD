package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.controllers.dataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class loginController implements Initializable {
    private Connection c = dataBase.getInstance().getConnection();
    private Stage main = new Stage();
    private String accountType;
    @FXML
    TextField user;
    @FXML
    TextField pass;
    @FXML
    ChoiceBox<String> paskyros;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paskyros.getItems().removeAll(paskyros.getItems());
        paskyros.getItems().addAll("Asmens paskyra", "Įmonės paskyra");
    }

    @FXML
    void getSelected(ActionEvent event) {
        accountType = paskyros.getValue();
    }

    @FXML
    void login(ActionEvent event) throws IOException {
        try {
            if (accountType.equals("Įmonės paskyra")) {
                if (this.loginCompany() == false) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Klaida!");
                    alert.setHeaderText("Nepavyko prisijungteeei!");
                    alert.setContentText("Patikrinkite ar įvedėte teisingus duomenis.");
                    alert.showAndWait();
                } else {
                    this.connect();
                }
            } else if (accountType.equals("Asmens paskyra")) {
                if (this.loginPerson() == false) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Klaida!");
                    alert.setHeaderText("Nepavyko prisijungti!dxdxd");
                    alert.setContentText("Patikrinkite ar įvedėte teisingus duomenis.");
                    alert.showAndWait();
                } else {
                    this.connect();
                }
            }
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Klaida!");
            alert.setHeaderText("Nepavyko prisijungti");
            alert.setContentText("Patikrinkite ar įvedėte teisingus duomenis.");
            alert.showAndWait();
        }
    }

    public boolean loginCompany() {
        try {
            dataBase dataBase = sample.controllers.dataBase.getInstance();
            String username = user.getText();
            String password = pass.getText();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT  * FROM company");
            while (rs.next()) {
                if (rs.getString(4).equals(username) && rs.getString(5).equals(password)) {
                    dataBase.setLoginID(rs.getInt(1));
                    dataBase.setLoginName(rs.getString(2));
                    dataBase.setLoginType(accountType);
                    return true;
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Klaida!");
            alert.setHeaderText("Trūksta duomenų!");
            alert.setContentText("Patikrinkite ar įvedėte visus duomenis.");
            alert.showAndWait();
        }
        return false;
    }
    public boolean loginPerson() {
        try {
            dataBase dataBase = sample.controllers.dataBase.getInstance();
            String username = user.getText();
            String password = pass.getText();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT  * FROM person");
            while (rs.next()) {
                if (rs.getString(6).equals(username) && rs.getString(7).equals(password)) {
                    dataBase.setLoginID(rs.getInt(1));
                    dataBase.setLoginName(rs.getString(2));
                    dataBase.setLoginType(accountType);
                    return true;
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Klaida!");
            alert.setHeaderText("Trūksta duomenų!");
            alert.setContentText("Patikrinkite ar įvedėte visus duomenis.");
            alert.showAndWait();
        }
        return false;
    }

    public void connect() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample/pages/sample.fxml"));
        main.setTitle("Apskaitos aplikacija");
        main.setScene(new Scene(root));
        main.show();
    }

}
