package com.example.club_sporting_final.admin.Controller;

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
import java.sql.SQLException;

public class EditTeamController {

    @FXML
    private TextField teamNameField;

    @FXML
    private TextField teamLeaderIDField;

    @FXML
    private TextField coachNameField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Team team;

    @FXML
    public void initialize() {
        // Load categories into ComboBox
        ObservableList<String> categories = FXCollections.observableArrayList("Football", "Fitness", "Swimming");
        categoryComboBox.setItems(categories);
    }

    /**
     * Sets the team to be edited.
     *
     * @param team the team object to edit
     */
    public void setTeam(Team team) {
        this.team = team;

        // Populate fields with the selected team's data
        teamNameField.setText(team.getTeamName());
        teamLeaderIDField.setText(String.valueOf(team.getTeamID())); // Assuming TeamLeaderID is an integer
        coachNameField.setText(team.getCoachName());
        categoryComboBox.setValue(team.getCategory());
    }

    /**
     * Handles the save action to update the team's details in the database.
     *
     * @param event the ActionEvent
     */
    @FXML
    void handleSave(ActionEvent event) {
        String teamName = teamNameField.getText().trim();
        String teamLeaderID = teamLeaderIDField.getText().trim();
        String coachName = coachNameField.getText().trim();
        String category = categoryComboBox.getValue();

        if (teamName.isEmpty() || teamLeaderID.isEmpty() || coachName.isEmpty() || category == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return;
        }

        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String updateQuery = "UPDATE teams SET TeamName = ?, TeamLeaderID = ?, CoachName = ?, Category = ? WHERE TeamID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                stmt.setString(1, teamName);
                stmt.setInt(2, Integer.parseInt(teamLeaderID)); // Assuming TeamLeaderID is an integer
                stmt.setString(3, coachName);
                stmt.setString(4, category);
                stmt.setInt(5, team.getTeamID());

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Team updated successfully.");
                    closeWindow();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update team.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the team: " + e.getMessage());
        }
    }

    /**
     * Handles the cancel action to close the edit team window.
     *
     * @param event the ActionEvent
     */
    @FXML
    void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) teamNameField.getScene().getWindow();
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
