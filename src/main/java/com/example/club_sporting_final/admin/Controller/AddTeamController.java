package com.example.club_sporting_final.admin.Controller;

import com.example.club_sporting_final.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddTeamController {

    @FXML
    private TextField teamNameField;

    @FXML
    private TextField coachNameField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private Button saveButton, cancelButton;

    private boolean teamAdded = false;

    @FXML
    public void initialize() {
        // Initialize categories
        ObservableList<String> categories = FXCollections.observableArrayList("Football", "Fitness", "Swimming");
        categoryComboBox.setItems(categories);
        categoryComboBox.setEditable(true); // Allow users to type a new category
        categoryComboBox.setValue("Football"); // Default value
    }

    @FXML
    private void handleSave() {
        String teamName = teamNameField.getText().trim();
        String coachName = coachNameField.getText().trim();
        String category = categoryComboBox.getValue().trim();

        // Validate inputs
        if (teamName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Team name cannot be empty.");
            return;
        }
        if (coachName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Coach name cannot be empty.");
            return;
        }
        if (category.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Category cannot be empty.");
            return;
        }

        // Save to the database
        String query = "INSERT INTO Teams (TeamName, CoachName, Category) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, teamName);
            stmt.setString(2, coachName);
            stmt.setString(3, category);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Team added successfully.");
                teamAdded = true;

                // Close the window
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add the team. Please try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while adding the team: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        // Close the window
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public boolean isTeamAdded() {
        return teamAdded;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
