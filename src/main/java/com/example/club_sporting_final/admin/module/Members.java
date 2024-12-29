package com.example.club_sporting_final.admin.module;

import javafx.beans.property.*;

public class Members {

    private final IntegerProperty memberID;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty phoneNumber;
    private final BooleanProperty subscriptionStatus; // BooleanProperty for binding
    private final IntegerProperty teamID;

    // Constructor for full initialization
    public Members(int memberID, String name, String email, String phoneNumber, boolean subscriptionStatus, int teamID) {
        this.memberID = new SimpleIntegerProperty(memberID);
        this.name = new SimpleStringProperty(name != null ? name : ""); // Ensure non-null default
        this.email = new SimpleStringProperty(email != null ? email : ""); // Ensure non-null default
        this.phoneNumber = new SimpleStringProperty(phoneNumber != null ? phoneNumber : ""); // Ensure non-null default
        this.subscriptionStatus = new SimpleBooleanProperty(subscriptionStatus);
        this.teamID = new SimpleIntegerProperty(teamID);
    }

    // Constructor without teamID (legacy support or when teamID is not required)
    public Members(int memberID, String name, String email, String phoneNumber, boolean subscriptionStatus) {
        this(memberID, name, email, phoneNumber, subscriptionStatus, 0); // Default teamID to 0 (No Team Assigned)
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
        this.name.set(name != null ? name : "");
    }

    public StringProperty nameProperty() {
        return name;
    }

    // Getters and setters for email
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email != null ? email : "");
    }

    public StringProperty emailProperty() {
        return email;
    }

    // Getters and setters for phoneNumber
    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber != null ? phoneNumber : "");
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

    // Getters and setters for teamID
    public int getTeamID() {
        return teamID.get();
    }

    public void setTeamID(int teamID) {
        this.teamID.set(teamID);
    }

    public IntegerProperty teamIDProperty() {
        return teamID;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberID=" + getMemberID() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", subscriptionStatus=" + (isSubscriptionStatus() ? "Active" : "Inactive") +
                ", teamID=" + (getTeamID() == 0 ? "No Team Assigned" : getTeamID()) +
                '}';
    }
}
