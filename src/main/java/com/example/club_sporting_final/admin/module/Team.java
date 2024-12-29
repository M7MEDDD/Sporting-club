package com.example.club_sporting_final.admin.module;

import javafx.beans.property.*;

public class Team {
    private final IntegerProperty teamID;
    private final StringProperty teamName;
    private final StringProperty coachName;
    private final StringProperty category;
    private final IntegerProperty memberCount;
    private final IntegerProperty teamLeaderID;

    public Team(int teamID, String teamName, String coachName, String category, int memberCount, int teamLeaderID) {
        this.teamID = new SimpleIntegerProperty(teamID);
        this.teamName = new SimpleStringProperty(teamName);
        this.coachName = new SimpleStringProperty(coachName);
        this.category = new SimpleStringProperty(category);
        this.memberCount = new SimpleIntegerProperty(memberCount);
        this.teamLeaderID = new SimpleIntegerProperty(teamLeaderID);
    }

    // Getters
    public int getTeamID() {
        return teamID.get();
    }

    public IntegerProperty teamIDProperty() {
        return teamID;
    }

    public String getTeamName() {
        return teamName.get();
    }

    public StringProperty teamNameProperty() {
        return teamName;
    }

    public String getCoachName() {
        return coachName.get();
    }

    public StringProperty coachNameProperty() {
        return coachName;
    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public int getMemberCount() {
        return memberCount.get();
    }

    public IntegerProperty memberCountProperty() {
        return memberCount;
    }

    public int getTeamLeaderID() {
        return teamLeaderID.get();
    }

    public IntegerProperty teamLeaderIDProperty() {
        return teamLeaderID;
    }

    // Setters
    public void setTeamID(int teamID) {
        this.teamID.set(teamID);
    }

    public void setTeamName(String teamName) {
        this.teamName.set(teamName);
    }

    public void setCoachName(String coachName) {
        this.coachName.set(coachName);
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public void setMemberCount(int memberCount) {
        this.memberCount.set(memberCount);
    }

    public void setTeamLeaderID(int teamLeaderID) {
        this.teamLeaderID.set(teamLeaderID);
    }

    @Override
    public String toString() {
        return getTeamName();
    }
}
