package com.example.club_sporting_final.admin.module;

import javafx.beans.property.*;

public class Subscription {
    private final IntegerProperty subscriptionID;
    private final IntegerProperty memberID;
    private final StringProperty planType;
    private final StringProperty startDate;
    private final StringProperty endDate;
    private final DoubleProperty amount;

    public Subscription(int subscriptionID, int memberID, String planType, String startDate, String endDate, double amount) {
        this.subscriptionID = new SimpleIntegerProperty(subscriptionID);
        this.memberID = new SimpleIntegerProperty(memberID);
        this.planType = new SimpleStringProperty(planType);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.amount = new SimpleDoubleProperty(amount);
    }

    // Getters and Setters

    // SubscriptionID
    public int getSubscriptionID() {
        return subscriptionID.get();
    }

    public void setSubscriptionID(int subscriptionID) {
        this.subscriptionID.set(subscriptionID);
    }

    public IntegerProperty subscriptionIDProperty() {
        return subscriptionID;
    }

    // MemberID
    public int getMemberID() {
        return memberID.get();
    }

    public void setMemberID(int memberID) {
        this.memberID.set(memberID);
    }

    public IntegerProperty memberIDProperty() {
        return memberID;
    }

    // PlanType
    public String getPlanType() {
        return planType.get();
    }

    public void setPlanType(String planType) {
        this.planType.set(planType);
    }

    public StringProperty planTypeProperty() {
        return planType;
    }

    // StartDate
    public String getStartDate() {
        return startDate.get();
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public StringProperty startDateProperty() {
        return startDate;
    }

    // EndDate
    public String getEndDate() {
        return endDate.get();
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }

    public StringProperty endDateProperty() {
        return endDate;
    }

    // Amount
    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionID=" + getSubscriptionID() +
                ", memberID=" + getMemberID() +
                ", planType='" + getPlanType() + '\'' +
                ", startDate='" + getStartDate() + '\'' +
                ", endDate='" + getEndDate() + '\'' +
                ", amount=" + getAmount() +
                '}';
    }
}
