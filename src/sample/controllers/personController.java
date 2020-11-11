package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import sample.Company;
import sample.Person;
import sample.controllers.dataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class personController implements Initializable {
    private Stage personStage = new Stage();
    private Connection c = dataBase.getInstance().getConnection();
    private ObservableList<Person> personList = FXCollections.observableArrayList();
    private int ID;
    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> nameC;

    @FXML
    private TableColumn<Person, String> lastC;

    @FXML
    private TableColumn<Person, String> phoneC;

    @FXML
    private TableColumn<Person, String> mailC;
    @FXML
    private TableColumn<Company, Integer> idC;
    @FXML
    private TableColumn<Person, String> userC;

    @FXML
    private TableColumn<Person, String> passC;

    @FXML
    private TextField nameField;

    @FXML
    private TextField lastField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField mailField;

    @FXML
    private TextField userField;

    @FXML
    private TextField passField;

    public void inputPersonInfo() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample/pages/personAccounts.fxml"));
        personStage.setTitle("Asmenų paskyros");
        personStage.setScene(new Scene(root));
        personStage.show();
    }

    @FXML
    void createPerson(ActionEvent event) throws SQLException {
        String name = nameField.getText();
        String lastName = lastField.getText();
        String phone = phoneField.getText();
        String mail = mailField.getText();
        String user = userField.getText();
        String pass = passField.getText();
        if (name.isEmpty() == true || lastName.isEmpty() == true || phone.isEmpty() == true || mail.isEmpty() == true || user.isEmpty() == true || pass.isEmpty() == true) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Klaida!");
            alert.setHeaderText("Trūksta duomenų!");
            alert.setContentText("Patikrinkite ar įvedėte visus duomenis.");
            alert.showAndWait();
        } else {
            ID += 1;
            String SQL = "INSERT INTO person VALUES(?,?,?,?,?,?,?)";
            Person person = new Person(ID, user, pass, name, lastName, phone, mail);
            personList.add(person);
            PreparedStatement ps = c.prepareStatement(SQL);
            ps.setInt(1, ID);
            ps.setString(2, name);
            ps.setString(3, lastName);
            ps.setString(4, phone);
            ps.setString(5, mail);
            ps.setString(6, user);
            ps.setString(7, pass);
            ps.executeUpdate();
            ps.close();
        }
    }

    @FXML
    void deletePerson(ActionEvent event) {
        try {
            Person personSelected = personTable.getSelectionModel().getSelectedItem();
            personTable.getItems().remove(personSelected);
            PreparedStatement st = c.prepareStatement("DELETE FROM person WHERE personID = ?");
            st.setInt(1, personSelected.getUserID());
            st.executeUpdate();
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Klaida!");
            alert.setHeaderText("Nepasirinktas įrašas!");
            alert.setContentText("Patikrinkite ar pasirinkote norimą įrašą.");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.getData();
        idC.setCellValueFactory(new PropertyValueFactory<Company, Integer>("userID"));
        nameC.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        lastC.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        phoneC.setCellValueFactory(new PropertyValueFactory<Person, String>("phoneNumber"));
        mailC.setCellValueFactory(new PropertyValueFactory<Person, String>("mail"));
        userC.setCellValueFactory(new PropertyValueFactory<Person, String>("username"));
        passC.setCellValueFactory(new PropertyValueFactory<Person, String>("password"));

        personTable.setItems(personList);
        personTable.setEditable(true);

        nameC.setCellFactory(TextFieldTableCell.forTableColumn());
        lastC.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneC.setCellFactory(TextFieldTableCell.forTableColumn());
        mailC.setCellFactory(TextFieldTableCell.forTableColumn());
        userC.setCellFactory(TextFieldTableCell.forTableColumn());
        passC.setCellFactory(TextFieldTableCell.forTableColumn());

        nameField.clear();
        lastField.clear();
        phoneField.clear();
        mailField.clear();
        userField.clear();
        passField.clear();
    }

    @FXML
    public void editName(TableColumn.CellEditEvent<Company, String> editEvent) throws IOException, SQLException {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        selectedPerson.setName(editEvent.getNewValue());
        PreparedStatement st = c.prepareStatement("UPDATE person SET firstName = ? WHERE personID = ?" );
        st.setString(1, selectedPerson.getName());
        st.setInt(2, selectedPerson.getUserID());
        st.execute();
    }

    public void editLast(TableColumn.CellEditEvent<Company, String> editEvent) throws SQLException {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        selectedPerson.setLastName(editEvent.getNewValue());
        PreparedStatement st = c.prepareStatement("UPDATE person SET lastName = ? WHERE personID = ?" );
        st.setString(1, selectedPerson.getLastName());
        st.setInt(2, selectedPerson.getUserID());
        st.execute();
    }

    public void editMail(TableColumn.CellEditEvent<Company, String> editEvent) throws SQLException {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        selectedPerson.setMail(editEvent.getNewValue());
        PreparedStatement st = c.prepareStatement("UPDATE person SET email = ? WHERE personID = ?" );
        st.setString(1, selectedPerson.getMail());
        st.setInt(2, selectedPerson.getUserID());
        st.execute();
    }

    public void editPhone(TableColumn.CellEditEvent<Company, String> editEvent) throws SQLException {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        selectedPerson.setPhoneNumber(editEvent.getNewValue());
        PreparedStatement st = c.prepareStatement("UPDATE person SET phoneNumber = ? WHERE personID = ?" );
        st.setString(1, selectedPerson.getPhoneNumber());
        st.setInt(2, selectedPerson.getUserID());
        st.execute();
    }

    public void editUser(TableColumn.CellEditEvent<Company, String> editEvent) throws SQLException {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        selectedPerson.setUsername(editEvent.getNewValue());
        PreparedStatement st = c.prepareStatement("UPDATE person SET username = ? WHERE personID = ?" );
        st.setString(1, selectedPerson.getUsername());
        st.setInt(2, selectedPerson.getUserID());
        st.execute();
    }

    public void editPass(TableColumn.CellEditEvent<Company, String> editEvent) throws SQLException {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        selectedPerson.setPassword(editEvent.getNewValue());
        PreparedStatement st = c.prepareStatement("UPDATE person SET password = ? WHERE personID = ?" );
        st.setString(1, selectedPerson.getPassword());
        st.setInt(2, selectedPerson.getUserID());
        st.execute();
    }

    public void getData() {
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT  * FROM person");
            while (rs.next()) {
                personList.add(new Person(
                        rs.getInt(1),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)
                ));
                ID = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
