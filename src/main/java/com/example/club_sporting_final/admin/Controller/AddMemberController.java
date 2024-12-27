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
    private ChoiceBox<Team> teamChoiceBox;

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
        // Initialize toggle group
        planToggleGroup = new ToggleGroup();
        monthlyPlan.setToggleGroup(planToggleGroup);
        quarterlyPlan.setToggleGroup(planToggleGroup);
        yearlyPlan.setToggleGroup(planToggleGroup);

        // Load teams
        loadTeams();
    }

    private void loadTeams() {
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT TeamID, TeamName FROM teams")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                teamList.add(new Team(rs.getInt("TeamID"), rs.getString("TeamName"), null, null, rs.getInt("MemberCount")));
            }
            teamChoiceBox.setItems(FXCollections.observableArrayList(teamList));
        } catch (SQLException e) {
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

        if (!validateInputs(memberName, email, phone, selectedTeam, planType)) return;

        String insertMemberQuery = "INSERT INTO members (Name, Email, PhoneNumber) VALUES (?, ?, ?)";
        String associateTeamQuery = "INSERT INTO team_members (MemberID, TeamID) VALUES (?, ?)";
        String insertSubscriptionQuery = "INSERT INTO subscriptions (MemberID, PlanType, StartDate, EndDate, Amount) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false);

            int memberID;

            // Step 1: Insert member
            try (PreparedStatement memberStmt = connection.prepareStatement(insertMemberQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                memberStmt.setString(1, memberName);
                memberStmt.setString(2, email);
                memberStmt.setString(3, phone);
                memberStmt.executeUpdate();

                ResultSet generatedKeys = memberStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    memberID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve MemberID.");
                }
            }

            // Step 2: Associate with team
            try (PreparedStatement teamStmt = connection.prepareStatement(associateTeamQuery)) {
                teamStmt.setInt(1, memberID);
                teamStmt.setInt(2, selectedTeam.getTeamID());
                teamStmt.executeUpdate();
            }

            // Step 3: Insert subscription
            if (isSubscribed) {
                String startDate = java.time.LocalDate.now().toString();
                String endDate = calculateEndDate(startDate, planType);
                double amount = calculateSubscriptionAmount(planType);

                try (PreparedStatement subStmt = connection.prepareStatement(insertSubscriptionQuery)) {
                    subStmt.setInt(1, memberID);
                    subStmt.setString(2, planType);
                    subStmt.setString(3, startDate);
                    subStmt.setString(4, endDate);
                    subStmt.setDouble(5, amount);
                    subStmt.executeUpdate();
                }
            }

            connection.commit();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Member and subscription added successfully.");
            closeWindow();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred: " + e.getMessage());
        }
    }

    private boolean validateInputs(String name, String email, String phone, Team team, String planType) {
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || team == null || planType == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled.");
            return false;
        }
        if (!email.matches("^\\S+@\\S+\\.\\S+$")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Enter a valid email address.");
            return false;
        }
        if (!phone.matches("\\d{10}")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Enter a valid 10-digit phone number.");
            return false;
        }
        return true;
    }

    private String getSelectedPlanType() {
        RadioButton selectedPlan = (RadioButton) planToggleGroup.getSelectedToggle();
        return selectedPlan != null ? selectedPlan.getText().toLowerCase() : null;
    }

    private String calculateEndDate(String startDate, String planType) {
        java.time.LocalDate start = java.time.LocalDate.parse(startDate);
        return switch (planType) {
            case "monthly" -> start.plusMonths(1).toString();
            case "quarterly" -> start.plusMonths(3).toString();
            case "yearly" -> start.plusYears(1).toString();
            default -> throw new IllegalArgumentException("Invalid plan type: " + planType);
        };
    }

    private double calculateSubscriptionAmount(String planType) {
        return switch (planType) {
            case "monthly" -> 50.0;
            case "quarterly" -> 150.0;
            case "yearly" -> 500.0;
            default -> throw new IllegalArgumentException("Invalid plan type: " + planType);
        };
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
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
