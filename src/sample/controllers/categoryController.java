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
import sample.models.Category;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class categoryController implements Initializable {
    private Stage categoryScene = new Stage();
    private subcategoryController subcategory = new subcategoryController();
    private incomeController income = new incomeController();
    private expenseController expense = new expenseController();
    private ObservableList<Category> categoryList = FXCollections.observableArrayList();
    private Connection c = dataBase.getInstance().getConnection();
    private int ID;
    private String parentID;
    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, String> parentC;
    @FXML
    private TableColumn<Category, String> nameC;
    @FXML
    private TableColumn<Category, Integer> idC;
    @FXML
    private TextField nameField;

    @FXML
    void buttonClicked(ActionEvent event) throws SQLException {
        String name = nameField.getText();

        if (name.isEmpty() == true) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Klaida!");
            alert.setHeaderText("Trūksta duomenų!");
            alert.setContentText("Patikrinkite ar įvedėte visus duomenis.");
            alert.showAndWait();
        } else {
            ID += 1;
            Category category = new Category(name, "-", ID);
            categoryList.add(category);
            String SQL = "INSERT INTO category values(?,?,?)";
            PreparedStatement ps = c.prepareStatement(SQL);
            ps.setInt(1, ID);
            ps.setString(2, "-");
            ps.setString(3, name);
            ps.executeUpdate();
            ps.close();
        }
    }

    public void inputCategoryInfo() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample//pages/category.fxml"));
        categoryScene.setTitle("Kategorijos");
        categoryScene.setScene(new Scene(root));
        categoryScene.show();
    }

    public void deleteCategory(ActionEvent event) {
        try {
            Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
            categoryTable.getItems().remove(selectedCategory);
            PreparedStatement st = c.prepareStatement("DELETE FROM category WHERE categoryID = ?");
            st.setInt(1, selectedCategory.getCategoryID());
            st.executeUpdate();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Klaida!");
            alert.setHeaderText("Nepasirinktas įrašas!");
            alert.setContentText("Patikrinkite ar pasirinkote norimą įrašą.");
            alert.showAndWait();
        }
    }

    public void editName(TableColumn.CellEditEvent<Category, String> editEvent) throws IOException, SQLException {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        selectedCategory.setCategoryName(editEvent.getNewValue());
        PreparedStatement st = c.prepareStatement("UPDATE category SET categoryName = ? WHERE categoryID = ?" );
        st.setString(1, selectedCategory.getCategoryName());
        st.setInt(2, selectedCategory.getCategoryID());
        st.execute();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.getData();
        idC.setCellValueFactory(new PropertyValueFactory<Category, Integer>("categoryID"));
        nameC.setCellValueFactory(new PropertyValueFactory<Category, String>("categoryName"));
        parentC.setCellValueFactory(new PropertyValueFactory<Category, String>("parentID"));


        categoryTable.setItems(categoryList);
        categoryTable.setEditable(true);

        nameC.setCellFactory(TextFieldTableCell.forTableColumn());
        nameField.clear();
    }

    public void getData() {
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT  * FROM category");
            while (rs.next()) {
                categoryList.add(new Category(
                        rs.getString(3),
                        rs.getString(2),
                        rs.getInt(1)
                ));
               ID = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void subcategories(ActionEvent actionEvent) throws IOException {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Klaida!");
            alert.setHeaderText("Nepasirinkta kategorija!");
            alert.setContentText("Pasirinkite kategoriją ir bandykite dar kartą.");
            alert.showAndWait();
        } else {
            dataBase.getInstance().setParentID(selectedCategory.getCategoryID().toString());
            subcategory.displaySubcategory();
        }
    }

    @FXML
    void showIncome(ActionEvent event) throws IOException {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Klaida!");
            alert.setHeaderText("Nepasirinkta kategorija!");
            alert.setContentText("Pasirinkite kategoriją ir bandykite dar kartą.");
            alert.showAndWait();
        } else {
            dataBase.getInstance().setParentID(selectedCategory.getCategoryID().toString());
            income.displayIncomeInfo();
        }
    }

    @FXML
    void showExpense(ActionEvent event) throws IOException {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Klaida!");
            alert.setHeaderText("Nepasirinkta kategorija!");
            alert.setContentText("Pasirinkite kategoriją ir bandykite dar kartą.");
            alert.showAndWait();
        } else {
            dataBase.getInstance().setParentID(selectedCategory.getCategoryID().toString());
            expense.displayExpenseInfo();
        }
    }
}
