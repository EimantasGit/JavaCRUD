package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import sample.controllers.categoryController;
import sample.controllers.companyController;
import sample.controllers.dataBase;
import sample.controllers.personController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private categoryController category = new categoryController();
    private companyController company = new companyController();
    private personController person = new personController();
    //private importFile file = new importFile();
   // private exportFile export = new exportFile();

    @FXML
    private TextField loggedID, loggedName, loggedType;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        dataBase dataBase = sample.controllers.dataBase.getInstance();
        loggedID.setText(dataBase.getLoginID());
        loggedName.setText(dataBase.getLoginName());
        loggedType.setText(dataBase.getLoginType());
    }
    @FXML
    void companyCreator(ActionEvent event) throws IOException {
         company.inputCompanyInfo();
    }
    @FXML
    void personCreator(ActionEvent event) throws IOException {
        person.inputPersonInfo();
    }
    @FXML
    void categoryCreator(ActionEvent event) throws IOException {
        category.inputCategoryInfo();
    }
}
