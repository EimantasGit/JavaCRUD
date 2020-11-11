package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.nio.charset.IllegalCharsetNameException;

public class Income {
    private StringProperty parentCategory;
    private Integer incomeID;
    private Integer amount;

    public Income(int ID, String parentCategory, int amount) {
        this.incomeID = ID;
        this.parentCategory = new SimpleStringProperty(parentCategory);
        this.amount = amount;
    }
    public int getAmount(){
        return this.amount;
    }
    public Integer getIncomeID(){
        return this.incomeID;
    }
    public void setAmount(int amount){
        this.amount = amount;
    }
    public String  getParentID(){
        return this.parentCategory.get();
    }
}
