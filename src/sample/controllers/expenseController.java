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
import sample.Expenses;
import sample.controllers.dataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class expenseController implements Initializable {
    private Stage expenseStage = new Stage();
    private ObservableList<Expenses> expenseList = FXCollections.observableArrayList();
    private Connection c = dataBase.getInstance().getConnection();
    private int ID;
    @FXML
    private TableView<Expenses> expenseTable;
    @FXML
    private TableColumn<Expenses, String> amountC;
    @FXML
    private TableColumn<Expenses, Integer> idC;
    @FXML
    private TextField amountField;

    public void displayExpenseInfo() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample/pages/expense.fxml"));
        expenseStage.setTitle("Išlaidos");
        expenseStage.setScene(new Scene(root));
        expenseStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.getData();
        idC.setCellValueFactory(new PropertyValueFactory<Expenses, Integer>("expenseID"));
        amountC.setCellValueFactory(new PropertyValueFactory<Expenses, String>("amount"));

        expenseTable.setItems(expenseList);
        expenseTable.setEditable(true);

    }

    public void getData() {
        try {
            Statement s = c.createStatement();
            String parentID = dataBase.getInstance().getParentID();
            ResultSet rs = s.executeQuery("SELECT  * FROM expenses");
            while (rs.next()) {
                if (rs.getString(2).equals(parentID)) {
                    expenseList.add(new Expenses(
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
    void addExpense(ActionEvent event) throws SQLException {
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
            String SQL = "INSERT INTO expenses VALUES(?,?,?)";
            Expenses expenses = new Expenses(ID, parentID, amount);
            expenseList.add(expenses);
            PreparedStatement ps = c.prepareStatement(SQL);
            ps.setInt(1, ID);
            ps.setString(2, parentID);
            ps.setInt(3, amount);
            ps.execute();
            ps.close();
        }
    }

    @FXML
    void deleteExpense(ActionEvent event) {
        try {
            Expenses selectedExpense = expenseTable.getSelectionModel().getSelectedItem();
            expenseTable.getItems().remove(selectedExpense);
            PreparedStatement st = c.prepareStatement("DELETE FROM expenses WHERE expenseID = ?");
            st.setInt(1, selectedExpense.getExpenseID());
            st.executeUpdate();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Klaida!");
            alert.setHeaderText("Nepasirinktas įrašas!");
            alert.setContentText("Patikrinkite ar pasirinkote norimą įrašą.");
            alert.showAndWait();
        }
    }
    public void editAmount(TableColumn.CellEditEvent<Expenses, String> editEvent) throws IOException, SQLException {
        Expenses selectedExpenses = expenseTable.getSelectionModel().getSelectedItem();
        selectedExpenses.setAmount(Integer.parseInt(editEvent.getNewValue()));
        PreparedStatement st = c.prepareStatement("UPDATE expenses SET amount = ? WHERE expenseID = ?" );
        st.setInt(1, selectedExpenses.getAmount());
        st.setInt(2, selectedExpenses.getExpenseID());
        st.execute();
    }
}
