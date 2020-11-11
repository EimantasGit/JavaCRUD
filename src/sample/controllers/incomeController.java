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
import javafx.stage.Stage;
import sample.Company;
import sample.Income;
import sample.controllers.dataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class incomeController implements Initializable {
    private Stage incomeStage = new Stage();
    private ObservableList<Income> incomeList = FXCollections.observableArrayList();
    private Connection c = dataBase.getInstance().getConnection();
    private int ID;
    @FXML
    private TableView<Income> incomeTable;
    @FXML
    private TableColumn<Income, String> amountC;
    @FXML
    private TableColumn<Income, Integer> idC;
    @FXML
    private TextField amountField;

    public void displayIncomeInfo() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample/pages/income.fxml"));
        incomeStage.setTitle("Pajamos");
        incomeStage.setScene(new Scene(root));
        incomeStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.getData();

        idC.setCellValueFactory(new PropertyValueFactory<Income, Integer>("incomeID"));
        amountC.setCellValueFactory(new PropertyValueFactory<Income, String>("amount"));

        incomeTable.setItems(incomeList);
        incomeTable.setEditable(true);


    }

    public void getData() {
        try {
            Statement s = c.createStatement();
            String parentID = dataBase.getInstance().getParentID();
            ResultSet rs = s.executeQuery("SELECT  * FROM income");
            while (rs.next()) {
                if (rs.getString(2).equals(parentID)) {
                    incomeList.add(new Income(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getInt(3)
                    ));
                }
                ID = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    void addIncome(ActionEvent event) throws SQLException {
        int amount = Integer.parseInt(amountField.getText());
        if (amount == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Klaida!");
            alert.setHeaderText("Trūksta duomenų!");
            alert.setContentText("Patikrinkite ar įvedėte visus duomenis.");
            alert.showAndWait();
        } else {
            String parentID = dataBase.getInstance().getParentID();
            ID += 1;
            String SQL = "INSERT INTO income VALUES(?,?,?)";
            Income income = new Income(ID, parentID, amount);
            incomeList.add(income);
            PreparedStatement ps = c.prepareStatement(SQL);
            ps.setInt(1, ID);
            ps.setString(2, parentID);
            ps.setInt(3, amount);
            ps.execute();
            ps.close();
        }
    }

    @FXML
    void deleteIncome(ActionEvent event) {
        try {
            Income selectedIncome = incomeTable.getSelectionModel().getSelectedItem();
            incomeTable.getItems().remove(selectedIncome);
            PreparedStatement st = c.prepareStatement("DELETE FROM income WHERE incomeID = ?");
            st.setInt(1, selectedIncome.getIncomeID());
            st.executeUpdate();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Klaida!");
            alert.setHeaderText("Nepasirinktas įrašas!");
            alert.setContentText("Patikrinkite ar pasirinkote norimą įrašą.");
            alert.showAndWait();
        }
    }

    public void editAmount(TableColumn.CellEditEvent<Income, String> editEvent) throws IOException, SQLException {
        Income selectedIncome = incomeTable.getSelectionModel().getSelectedItem();
        selectedIncome.setAmount(Integer.parseInt(editEvent.getNewValue()));
        PreparedStatement st = c.prepareStatement("UPDATE income SET amount = ? WHERE incomeID = ?" );
        st.setInt(1, selectedIncome.getAmount());
        st.setInt(2, selectedIncome.getIncomeID());
        st.execute();
    }
}
