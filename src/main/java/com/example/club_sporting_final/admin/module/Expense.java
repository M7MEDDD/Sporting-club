package com.example.club_sporting_final.admin.module;

import javafx.beans.property.*;

public class Expense {
    private final IntegerProperty expenseID;
    private final StringProperty expenseType;
    private final DoubleProperty amount;
    private final StringProperty date;

    public Expense(int expenseID, String expenseType, double amount, String date) {
        this.expenseID = new SimpleIntegerProperty(expenseID);
        this.expenseType = new SimpleStringProperty(expenseType);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleStringProperty(date);
    }

    // Getters
    public int getExpenseID() {
        return expenseID.get();
    }

    public IntegerProperty expenseIDProperty() {
        return expenseID;
    }

    public String getExpenseType() {
        return expenseType.get();
    }

    public StringProperty expenseTypeProperty() {
        return expenseType;
    }

    public double getAmount() {
        return amount.get();
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    // Setters
    public void setExpenseType(String expenseType) {
        this.expenseType.set(expenseType);
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public void setDate(String date) {
        this.date.set(date);
    }
}
