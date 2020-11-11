package sample.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Expenses;
import sample.Income;

public class Category {
    private Integer categoryID;
    private StringProperty categoryName;
    private StringProperty parentID;
    private ObservableList<Category> subcategories = FXCollections.observableArrayList();
    private ObservableList<Income> incomeList = FXCollections.observableArrayList();
    private ObservableList<Expenses> expenseList = FXCollections.observableArrayList();

    public Category(String name, String parentID, int ID) {
        this.categoryID = ID;
        this.parentID = new SimpleStringProperty(parentID);
        this.categoryName = new SimpleStringProperty(name);
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName.get();
    }

    public void setCategoryName(String categoryName) {
        this.categoryName.set(categoryName);
    }
    public void setSubcategories(Category category){
        this.subcategories.add(category);
    }
    public ObservableList<Category> getSubcategories(){
        return this.subcategories;
    }
    public void setIncome(Income income){
        this.incomeList.add(income);
    }
    public ObservableList<Income> getIncomeList(){
        return this.incomeList;
    }
    public void setExpense(Expenses expense){
        this.expenseList.add(expense);
    }
    public ObservableList<Expenses> getExpenseList(){
        return this.expenseList;
    }
    public String getParentID(){
        return this.parentID.get();
    }
}
