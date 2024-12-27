package com.example.club_sporting_final.admin.Controller;

import com.example.club_sporting_final.admin.module.Team;
import com.example.club_sporting_final.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddMemberController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private CheckBox subscriptionStatus;

    @FXML
    private ChoiceBox<Team> teamChoiceBox; // Stores Team objects

    @FXML
    private RadioButton monthlyPlan;

    @FXML
    private RadioButton quarterlyPlan;

    @FXML
    private RadioButton yearlyPlan;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private ToggleGroup planToggleGroup;

    private ObservableList<Team> teamList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize the toggle group for subscription plans
        planToggleGroup = new ToggleGroup();
        monthlyPlan.setToggleGroup(planToggleGroup);
        quarterlyPlan.setToggleGroup(planToggleGroup);
        yearlyPlan.setToggleGroup(planToggleGroup);

        // Load teams into the ChoiceBox
        loadTeams();
    }

    private void loadTeams() {
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT TeamID, TeamName FROM teams")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Add team data to the team list
                teamList.add(new Team(rs.getInt("TeamID"), rs.getString("TeamName"), null, null));
            }
            teamChoiceBox.setItems(FXCollections.observableArrayList(teamList));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load teams: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        String memberName = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        Team selectedTeam = teamChoiceBox.getValue();
        boolean isSubscribed = subscriptionStatus.isSelected();
        String planType = getSelectedPlanType();

        if (memberName.isEmpty() || email.isEmpty() || phone.isEmpty() || selectedTeam == null || planType == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields and select a team and subscription plan.");
            return;
        }

        String insertMemberQuery = "INSERT INTO members (Name, Email, PhoneNumber, SubscriptionStatus, SubscriptionPlan) VALUES (?, ?, ?, ?, ?)";
        String associateTeamQuery = "INSERT INTO team_members (MemberID, TeamID) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            // Step 1: Insert member into the `members` table
            int memberID;
            try (PreparedStatement memberStmt = connection.prepareStatement(insertMemberQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                memberStmt.setString(1, memberName);
                memberStmt.setString(2, email);
                memberStmt.setString(3, phone);
                memberStmt.setBoolean(4, isSubscribed);
                memberStmt.setString(5, planType);
                memberStmt.executeUpdate();

                ResultSet generatedKeys = memberStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    memberID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve generated MemberID.");
                }
            }

            // Step 2: Associate the member with the selected team
            try (PreparedStatement teamMemberStmt = connection.prepareStatement(associateTeamQuery)) {
                teamMemberStmt.setInt(1, memberID);
                teamMemberStmt.setInt(2, selectedTeam.getTeamID());
                teamMemberStmt.executeUpdate();
            }

            connection.commit(); // Commit transaction
            showAlert(Alert.AlertType.INFORMATION, "Success", "Member added and associated with the team successfully.");
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred: " + e.getMessage());
        }
    }

    private String getSelectedPlanType() {
        RadioButton selectedPlan = (RadioButton) planToggleGroup.getSelectedToggle();
        return selectedPlan != null ? selectedPlan.getText().toLowerCase() : null;
    }

    @FXML
    private void handleCancel() {
        // Close the current window
        Stage stage = (Stage) cancelButton.getScene().getWindow();
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
