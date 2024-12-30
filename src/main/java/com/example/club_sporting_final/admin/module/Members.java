package com.example.club_sporting_final.admin.module;

import javafx.beans.property.*;

public class Members {
    private final IntegerProperty memberID;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty phoneNumber;
    private final BooleanProperty subscriptionStatus;
    private final IntegerProperty teamID; // Add teamID property


    public Members(int memberID, String name, String email, String phoneNumber, boolean subscriptionStatus, int teamID) {
        this.memberID = new SimpleIntegerProperty(memberID);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.subscriptionStatus = new SimpleBooleanProperty(subscriptionStatus);
        this.teamID = new SimpleIntegerProperty(teamID); // Add this if missing
    }

    public Members(int memberID, String name, String email, String phoneNumber, boolean subscriptionStatus) {
        this(memberID, name, email, phoneNumber, subscriptionStatus, 0); // Default teamID to 0
    }



    // Getters
    public int getMemberID() {
        return memberID.get();
    }

    public IntegerProperty memberIDProperty() {
        return memberID;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public boolean isSubscriptionStatus() {
        return subscriptionStatus.get();
    }

    public BooleanProperty subscriptionStatusProperty() {
        return subscriptionStatus;
    }

    public int getTeamID() {
        return teamID.get(); // Implement getTeamID
    }

    public IntegerProperty teamIDProperty() {
        return teamID; // Allow binding to teamID
    }

    // Setters
    public void setMemberID(int memberID) {
        this.memberID.set(memberID);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public void setSubscriptionStatus(boolean subscriptionStatus) {
        this.subscriptionStatus.set(subscriptionStatus);
    }

    public void setTeamID(int teamID) {
        this.teamID.set(teamID); // Add setter for teamID
    }

    // Override toString for debugging
    @Override
    public String toString() {
        return name.get();
    }
}
