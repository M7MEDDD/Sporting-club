package com.example.club_sporting_final.admin.Controller;

import com.example.club_sporting_final.utils.DatabaseConnection;
import com.example.club_sporting_final.admin.module.Team;
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
                // Add only the team name to the teamList
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
        Team selectedTeam = teamChoiceBox.getValue(); // Get the selected team object
        if (selectedTeam == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a team.");
            return;
        }

        System.out.println("Selected Team ID: " + selectedTeam.getTeamID());
        System.out.println("Selected Team Name: " + selectedTeam.getTeamName());
    }


    @FXML
    private void handleCancel(ActionEvent event) {
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
