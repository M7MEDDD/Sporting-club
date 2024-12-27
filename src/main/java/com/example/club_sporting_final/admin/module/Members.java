package com.example.club_sporting_final.admin.module;

import javafx.beans.property.*;

public class Members {

    private final IntegerProperty memberID;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty phoneNumber;
    private final BooleanProperty subscriptionStatus; // BooleanProperty for binding

    // Default constructor

    // Parameterized constructor


    public Members(IntegerProperty memberID, StringProperty name, StringProperty email, StringProperty phoneNumber, BooleanProperty subscriptionStatus) {
        this.memberID = memberID;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.subscriptionStatus = subscriptionStatus;
    }

    public Members(int memberID, String name, String email, String phoneNumber, boolean subscriptionStatus) {
        this.memberID = new SimpleIntegerProperty(memberID);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.subscriptionStatus = new SimpleBooleanProperty(subscriptionStatus);

    }


    // Getters and setters for memberID
    public int getMemberID() {
        return memberID.get();
    }

    public void setMemberID(int memberID) {
        this.memberID.set(memberID);
    }

    public IntegerProperty memberIDProperty() {
        return memberID;
    }

    // Getters and setters for name
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    // Getters and setters for email
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    // Getters and setters for phoneNumber
    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    // Getters and setters for subscriptionStatus
    public boolean isSubscriptionStatus() {
        return subscriptionStatus.get();
    }

    public void setSubscriptionStatus(boolean subscriptionStatus) {
        this.subscriptionStatus.set(subscriptionStatus);
    }

    public BooleanProperty subscriptionStatusProperty() {
        return subscriptionStatus;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberID=" + getMemberID() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", subscriptionStatus=" + (isSubscriptionStatus() ? "Active" : "Inactive") +
                '}';
    }


}
