package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import sample.models.Category;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class subcategoryController implements Initializable {

    private Stage subcategoryScene = new Stage();
    private String parentID;
    private int ID;
    private Connection c = dataBase.getInstance().getConnection();

    private ObservableList<Category> subcategories = FXCollections.observableArrayList();
    @FXML
    private TextField subName;
    @FXML
    private TableView<Category> subTable;
    @FXML
    private TableColumn<Category, String> subC;
    @FXML
    private TableColumn<Category, Integer> idC;

    public void displaySubcategory() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample/pages/subcategories.fxml"));
        subcategoryScene.setTitle("Kategorijos");
        subcategoryScene.setScene(new Scene(root));
        subcategoryScene.show();
    }

    @FXML
    public void addSubcategory() throws SQLException {
        String name = subName.getText();
        if (name.isEmpty() == true) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Klaida!");
            alert.setHeaderText("Trūksta duomenų!");
            alert.setContentText("Patikrinkite ar įvedėte visus duomenis.");
            alert.showAndWait();
        } else {
            ID += 1;
            String SQL = "INSERT INTO category VALUES(?,?,?)";
            Category category = new Category(name, parentID, ID);
            subcategories.add(category);
            PreparedStatement ps = c.prepareStatement(SQL);
            ps.setInt(1, ID);
            ps.setString(2, parentID);
            ps.setString(3, name);
            ps.execute();
            ps.close();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.getData();
        idC.setCellValueFactory(new PropertyValueFactory<Category, Integer>("categoryID"));
        subC.setCellValueFactory(new PropertyValueFactory<Category, String>("categoryName"));

        subTable.setItems(subcategories);
        subTable.setEditable(true);

        subC.setCellFactory(TextFieldTableCell.forTableColumn());
        subName.clear();
    }

    public void getData() {
        try {
            Statement s = c.createStatement();
            parentID = dataBase.getInstance().getParentID();
            ResultSet rs = s.executeQuery("SELECT  * FROM category");
            while (rs.next()) {
                if (rs.getString(2).equals(parentID)) {
                    subcategories.add(new Category(
                            rs.getString(3),
                            rs.getString(2),
                            rs.getInt(1)
                    ));
                }
                ID = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
