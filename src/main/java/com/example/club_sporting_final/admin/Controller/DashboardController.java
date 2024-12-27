package com.example.club_sporting_final.admin.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.io.IOException;

public class DashboardController {

    @FXML
    private void goToMemberManagement(ActionEvent event) {
        openNewPage(event, "/com/example/club_sporting_final/admin/MembersMangment.fxml", "Member Management");
    }

    @FXML
    private void goToTeamManagement(ActionEvent event) {
        openNewPage(event, "/com/example/club_sporting_final/admin/TeamManagementPage.fxml", "Team Management");
    }

    @FXML
    private void goToExpenseManagement(ActionEvent event) {
        openNewPage(event, "/com/example/club_sporting_final/admin/ExpenseManagement.fxml", "Expense Management");
    }

    @FXML
    private void goToSubscriptionsManagement(ActionEvent event) {
        openNewPage(event, "/com/example/club_sporting_final/admin/ExpenseManagement.fxml", "Expense Management");
    }

    @FXML
    private void goToReports(ActionEvent event) {
        openNewPage(event, "/com/example/club_sporting_final/admin/Reports.fxml", "Reports");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        openNewPage(event, "/com/example/club_sporting_final/admin/Login.fxml", "Login");
    }

    private void openNewPage(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (loader.getLocation() == null) {
                throw new IOException("FXML file not found: " + fxmlPath);
            }
            Scene newScene = new Scene(loader.load());
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
            currentStage.setTitle(title);
        } catch (IOException e) {
            showError("Navigation Error", "Could not load the requested page: " + fxmlPath);
            e.printStackTrace();
        }
    }


    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
