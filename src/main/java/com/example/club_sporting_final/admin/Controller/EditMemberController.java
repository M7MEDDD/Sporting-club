package com.example.club_sporting_final.admin.Controller;

import com.example.club_sporting_final.admin.module.Members;
import com.example.club_sporting_final.admin.module.Team;
import com.example.club_sporting_final.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditMemberController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private CheckBox subscriptionStatus;

    @FXML
    private ChoiceBox<Team> teamChoiceBox;



    private Members member;
    private ObservableList<Team> teamList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadTeams();
    }

    /**
     * Sets the member to be edited.
     *
     * @param member the member object to edit
     */
    public void setMember(Members member) {
        this.member = member;

        // Populate fields with the selected member's data
        nameField.setText(member.getName());
        emailField.setText(member.getEmail());
        phoneField.setText(member.getPhoneNumber());
        subscriptionStatus.setSelected(member.isSubscriptionStatus());

        // Select the current team in the ChoiceBox
        if (member.getTeamID() != 0) { // If TeamID is not null (assuming 0 is the default for no team)
            teamChoiceBox.getSelectionModel().select(getTeamById(member.getTeamID()));
        }
    }

    /**
     * Loads teams into the ChoiceBox.
     */
    private void loadTeams() {
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT TeamID, TeamName FROM teams")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                teamList.add(new Team(rs.getInt("TeamID"), rs.getString("TeamName"), null, null));
            }
            teamChoiceBox.setItems(teamList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load teams: " + e.getMessage());
        }
    }

    private Team getTeamById(int teamId) {
        for (Team team : teamList) {
            if (team.getTeamID() == teamId) {
                return team;
            }
        }
        return null;
    }

    /**
     * Handles the save action to update the member's details in the database.
     *
     * @param event the ActionEvent
     */
    @FXML
    void handleSave(ActionEvent event) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        boolean isSubscribed = subscriptionStatus.isSelected();
        Team selectedTeam = teamChoiceBox.getValue();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all required fields.");
            return;
        }

        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            // Update member details
            String updateQuery = "UPDATE members SET Name = ?, Email = ?, PhoneNumber = ?, SubscriptionStatus = ? WHERE MemberID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, phone);
                stmt.setBoolean(4, isSubscribed);
                stmt.setInt(5, member.getMemberID());
                stmt.executeUpdate();
            }

            // Update team association
            if (selectedTeam != null) {
                // Check if an entry already exists in `team_members`
                String checkQuery = "SELECT COUNT(*) FROM team_members WHERE MemberID = ?";
                try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                    checkStmt.setInt(1, member.getMemberID());
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        // Update existing entry
                        String updateTeamQuery = "UPDATE team_members SET TeamID = ? WHERE MemberID = ?";
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateTeamQuery)) {
                            updateStmt.setInt(1, selectedTeam.getTeamID());
                            updateStmt.setInt(2, member.getMemberID());
                            updateStmt.executeUpdate();
                        }
                    } else {
                        // Insert new entry
                        String insertTeamQuery = "INSERT INTO team_members (MemberID, TeamID) VALUES (?, ?)";
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertTeamQuery)) {
                            insertStmt.setInt(1, member.getMemberID());
                            insertStmt.setInt(2, selectedTeam.getTeamID());
                            insertStmt.executeUpdate();
                        }
                    }
                }
            }

            connection.commit(); // Commit transaction
            showAlert(Alert.AlertType.INFORMATION, "Success", "Member updated successfully.");
            closeWindow();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the member: " + e.getMessage());
        }
    }

    /**
     * Handles the cancel action to close the edit member window.
     *
     * @param event the ActionEvent
     */
    @FXML
    void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
