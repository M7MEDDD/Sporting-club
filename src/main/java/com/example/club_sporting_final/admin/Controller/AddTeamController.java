package com.example.club_sporting_final.admin.Controller;

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
        ObservableList<String> categories = loadCategoriesFromDatabase();
        categoryComboBox.setItems(categories);
        categoryComboBox.setEditable(true); // Allow users to type a new category
        if (!categories.isEmpty()) {
            categoryComboBox.setValue(categories.get(0)); // Default value to the first category
        }
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

        // Validate if the team name already exists
        if (isTeamNameDuplicate(teamName)) {
            showAlert(Alert.AlertType.ERROR, "Duplicate Error", "Team name already exists. Please choose a different name.");
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

    private ObservableList<String> loadCategoriesFromDatabase() {
        ObservableList<String> categories = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT Category FROM Teams";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("Category"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load categories: " + e.getMessage());
        }
        return categories;
    }

    private boolean isTeamNameDuplicate(String teamName) {
        String query = "SELECT COUNT(*) FROM Teams WHERE TeamName = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, teamName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if a duplicate exists
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not validate team name: " + e.getMessage());
        }
        return false;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public boolean isTeamAdded() {
        return teamAdded;
    }
}
