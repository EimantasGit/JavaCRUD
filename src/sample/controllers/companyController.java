package sample.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import sample.Company;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class companyController implements Initializable {
    private Stage companyStage = new Stage();
    private dataBase db = dataBase.getInstance();
    private Connection c = dataBase.getInstance().getConnection();
    private ObservableList<Company> companyList = FXCollections.observableArrayList();
    private int ID = 0;
    @FXML
    private TableColumn<Company, String> nameC;
    @FXML
    private TableColumn<Company, String> repC;
    @FXML
    private TableColumn<Company, String> userC;
    @FXML
    private TableColumn<Company, String> pswC;
    @FXML
    private TableColumn<Company, Integer> idC;
    @FXML
    private TableView<Company> companyTable;
    @FXML
    private TextField nameField, repField, userField, passField;
    @FXML
    private Button createButton;

    @FXML
    void buttonClicked(ActionEvent event) {
        this.db = dataBase.getInstance();
        String name = nameField.getText();
        String rep = repField.getText();
        String user = userField.getText();
        String psw = passField.getText();
        if (name.isEmpty() == true || rep.isEmpty() == true || user.isEmpty() == true || psw.isEmpty() == true) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Klaida!");
            alert.setHeaderText("Trūksta duomenų!");
            alert.setContentText("Patikrinkite ar įvedėte visus duomenis.");
            alert.showAndWait();
        } else {
            try {
                ID += 1;
                Connection c = db.getConnection();
                String SQL = "INSERT INTO company VALUES(?, ?, ?, ?, ?)";
                Company company = new Company(ID, user, psw, name, rep);
                companyList.add(company);
                PreparedStatement ps = c.prepareStatement(SQL);
                ps.setInt(1, ID);
                ps.setString(2, name);
                ps.setString(3, rep);
                ps.setString(4, user);
                ps.setString(5, psw);
                ps.executeUpdate();
                ps.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void inputCompanyInfo() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample/pages/companyAccounts.fxml"));
        companyStage.setTitle("Kompanijų paskyros");
        companyStage.setScene(new Scene(root));
        companyStage.show();
    }

    public void deleteCompany(ActionEvent event)  {
        try {
            Company selectedCompany = companyTable.getSelectionModel().getSelectedItem();
            companyTable.getItems().remove(selectedCompany);
            PreparedStatement st = c.prepareStatement("DELETE FROM company WHERE companyID = ?");
            st.setInt(1, selectedCompany.getUserID());
            st.executeUpdate();
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Klaida!");
            alert.setHeaderText("Nepasirinktas įrašas!");
            alert.setContentText("Patikrinkite ar pasirinkote norimą įrašą.");
            alert.showAndWait();
        }
    }

    public void editName(TableColumn.CellEditEvent<Company, String> editEvent) throws IOException, SQLException {
        Company selectedCompany = companyTable.getSelectionModel().getSelectedItem();
        selectedCompany.setCompanyName(editEvent.getNewValue());
        PreparedStatement st = c.prepareStatement("UPDATE company SET companyName = ? WHERE companyID = ?" );
        st.setString(1, selectedCompany.getCompanyName());
        st.setInt(2, selectedCompany.getUserID());
        st.execute();
    }

    public void editRep(TableColumn.CellEditEvent<Company, String> editEvent) throws SQLException {
        Company selectedCompany = companyTable.getSelectionModel().getSelectedItem();
        selectedCompany.setCompanyRep(editEvent.getNewValue());
        PreparedStatement st = c.prepareStatement("UPDATE company SET companyRep = ? WHERE companyID = ?" );
        st.setString(1, selectedCompany.getCompanyRep());
        st.setInt(2, selectedCompany.getUserID());
        st.execute();
    }

    public void editUser(TableColumn.CellEditEvent<Company, String> editEvent) throws SQLException {
        Company selectedCompany = companyTable.getSelectionModel().getSelectedItem();
        selectedCompany.setUsername(editEvent.getNewValue());
        PreparedStatement st = c.prepareStatement("UPDATE company SET Username = ? WHERE companyID = ?" );
        st.setString(1, selectedCompany.getUsername());
        st.setInt(2, selectedCompany.getUserID());
        st.execute();
    }

    public void editPass(TableColumn.CellEditEvent<Company, String> editEvent) throws SQLException {
        Company selectedCompany = companyTable.getSelectionModel().getSelectedItem();
        selectedCompany.setPassword(editEvent.getNewValue());
        PreparedStatement st = c.prepareStatement("UPDATE company SET Password = ? WHERE companyID = ?" );
        st.setString(1, selectedCompany.getPassword());
        st.setInt(2, selectedCompany.getUserID());
        st.execute();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            this.getData();
            idC.setCellValueFactory(new PropertyValueFactory<Company, Integer>("userID"));
            nameC.setCellValueFactory(new PropertyValueFactory<Company, String>("companyName"));
            repC.setCellValueFactory(new PropertyValueFactory<Company, String>("companyRep"));
            userC.setCellValueFactory(new PropertyValueFactory<Company, String>("username"));
            pswC.setCellValueFactory(new PropertyValueFactory<Company, String>("password"));

            companyTable.setItems(companyList);
            companyTable.setEditable(true);

            nameC.setCellFactory(TextFieldTableCell.forTableColumn());
            repC.setCellFactory(TextFieldTableCell.forTableColumn());
            userC.setCellFactory(TextFieldTableCell.forTableColumn());
            pswC.setCellFactory(TextFieldTableCell.forTableColumn());

            nameField.clear();
            repField.clear();
            userField.clear();
            passField.clear();
    }
    public void getData() {
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT  * FROM company");
            while (rs.next()) {
                    companyList.add(new Company(
                            rs.getInt(1),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(2),
                            rs.getString(3)
                    ));
                    ID = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
