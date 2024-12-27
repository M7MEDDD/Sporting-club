package com.example.club_sporting_final.admin.module;

import javafx.beans.property.*;

public class Expense {
    private final IntegerProperty expenseID;
    private final StringProperty expenseType;
    private final DoubleProperty amount;
    private final StringProperty date;
    private final IntegerProperty teamID;

    public Expense(int expenseID, String expenseType, double amount, String date, int teamID) {
        this.expenseID = new SimpleIntegerProperty(expenseID);
        this.expenseType = new SimpleStringProperty(expenseType);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleStringProperty(date);
        this.teamID = new SimpleIntegerProperty(teamID);
    }

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

    public int getTeamID() {
        return teamID.get();
    }

    public IntegerProperty teamIDProperty() {
        return teamID;
    }
}
