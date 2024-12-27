package com.example.club_sporting_final.admin.Controller;

import com.example.club_sporting_final.admin.module.Members;
import com.example.club_sporting_final.utils.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Members member;

    @FXML
    public void initialize() {
        // Add initialization logic if needed
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

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return;
        }

        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String updateQuery = "UPDATE members SET Name = ?, Email = ?, PhoneNumber = ?, SubscriptionStatus = ? WHERE MemberID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, phone);
                stmt.setBoolean(4, isSubscribed);
                stmt.setInt(5, member.getMemberID());

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Member updated successfully.");
                    closeWindow();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update member.");
                }
            }
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
