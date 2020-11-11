package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Expenses {
    private StringProperty parentCategory;
    private Integer expenseID;
    private Integer amount;

    public Expenses(int ID, String parentCategory, int amount) {
        this.expenseID = ID;
        this.parentCategory = new SimpleStringProperty(parentCategory);
        this.amount = amount;
    }
    public int getAmount(){
        return this.amount;
    }
    public Integer getExpenseID(){
        return this.expenseID;
    }
    public void setAmount(int amount){
        this.amount = amount;
    }
    public String getParentID(){ return this.parentCategory.get();
    }
}
