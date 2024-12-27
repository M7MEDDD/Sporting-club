package com.example.club_sporting_final.admin.Controller;

import com.example.club_sporting_final.admin.module.Team;
import com.example.club_sporting_final.utils.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditTeamController {
    @FXML
    private TextField teamNameField;

    @FXML
    private TextField coachNameField;

    @FXML
    private TextField teamLeaderIDField;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    private Team team;

    public void setTeam(Team team) {
        this.team = team;

        if (team != null) {
            teamNameField.setText(team.getTeamName());
            coachNameField.setText(team.getCoachName());
            categoryChoiceBox.setValue(team.getCategory());
        }
    }


    @FXML
    private void handleSave() {
        if (team != null) {
            // Update the team details
            team.setTeamName(teamNameField.getText());
            team.setCoachName(coachNameField.getText());
            team.setCategory(categoryChoiceBox.getValue());

            // Update the database with the new details
            updateTeamInDatabase(team);
        }
    }

    private void updateTeamInDatabase(Team team) {
        String query = "UPDATE Teams SET TeamName = ?, CoachName = ?, Category = ? WHERE TeamID = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, team.getTeamName());
            stmt.setString(2, team.getCoachName());
            stmt.setString(3, team.getCategory());
            stmt.setInt(4, team.getTeamID());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Team updated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update the team.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not update the team: " + e.getMessage());
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
