package com.example.club_sporting_final.admin.module;

import javafx.beans.property.*;

public class Team {
    private final IntegerProperty teamID;
    private final StringProperty teamName;
    private  StringProperty coachName;
    private  StringProperty category;
    private  IntegerProperty memberCount;
    private  IntegerProperty teamLeaderID;

    public Team(int teamID, String teamName, String coachName, String category, int memberCount, Integer teamLeaderID) {
        this.teamID = new SimpleIntegerProperty(teamID);
        this.teamName = new SimpleStringProperty(teamName);
        this.coachName = new SimpleStringProperty(coachName);
        this.category = new SimpleStringProperty(category);
        this.memberCount = new SimpleIntegerProperty(memberCount);
        this.teamLeaderID = new SimpleIntegerProperty(teamLeaderID != null ? teamLeaderID : 0);
    }

    public Team(int teamID, String teamName, StringProperty coachName, StringProperty category, IntegerProperty memberCount, IntegerProperty teamLeaderID) {
        this.teamID = new SimpleIntegerProperty(teamID);
        this.teamName = new SimpleStringProperty(teamName);
        this.coachName = coachName;
        this.category = category;
        this.memberCount = memberCount;
        this.teamLeaderID = teamLeaderID;
    }

    public Team(int teamID, String teamName) {
        this.teamID = new SimpleIntegerProperty(teamID);
        this.teamName = new SimpleStringProperty(teamName);
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

    public Integer getTeamLeaderID() {
        return teamLeaderID.get() == 0 ? null : teamLeaderID.get();
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

    public void setTeamLeaderID(Integer teamLeaderID) {
        this.teamLeaderID.set(teamLeaderID != null ? teamLeaderID : 0);
    }

    // Override toString to display teamName in ChoiceBox
    @Override
    public String toString() {
        return getTeamName(); // Return the team name for display purposes
    }
}
