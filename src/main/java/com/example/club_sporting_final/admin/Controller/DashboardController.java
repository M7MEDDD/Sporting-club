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

    // Constants for FXML Paths
    private static final String MEMBERS_MANAGEMENT_PATH = "/com/example/club_sporting_final/admin/MembersManagement.fxml";
    private static final String TEAM_MANAGEMENT_PATH = "/com/example/club_sporting_final/admin/TeamManagementPage.fxml";
    private static final String EXPENSE_MANAGEMENT_PATH = "/com/example/club_sporting_final/admin/ExpenseManagement.fxml";
    private static final String SUBSCRIPTIONS_MANAGEMENT_PATH = "/com/example/club_sporting_final/admin/SubscriptionsManagement.fxml";
    private static final String REPORTS_PATH = "/com/example/club_sporting_final/admin/Reports.fxml";
    private static final String LOGIN_PATH = "/com/example/club_sporting_final/login/Login.fxml";

    @FXML
    private void goToMemberManagement(ActionEvent event) {
        navigateToPage(event, MEMBERS_MANAGEMENT_PATH, "Member Management");
    }

    @FXML
    private void goToTeamManagement(ActionEvent event) {
        navigateToPage(event, TEAM_MANAGEMENT_PATH, "Team Management");
    }

    @FXML
    private void goToExpenseManagement(ActionEvent event) {
        navigateToPage(event, EXPENSE_MANAGEMENT_PATH, "Expense Management");
    }

    @FXML
    private void goToSubscriptionsManagement(ActionEvent event) {
        navigateToPage(event, SUBSCRIPTIONS_MANAGEMENT_PATH, "Subscriptions Management");
    }

    @FXML
    private void goToReports(ActionEvent event) {
        navigateToPage(event, REPORTS_PATH, "Reports");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        navigateToPage(event, LOGIN_PATH, "Login");
    }


    private void navigateToPage(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene newScene = new Scene(loader.load());
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
            currentStage.setTitle(title);
        } catch (IOException e) {
            showErrorAlert("Navigation Error", "Failed to load: " + title);
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
