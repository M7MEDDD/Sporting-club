package com.example.club_sporting_final.admin.Controller;

import com.example.club_sporting_final.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardController {

    // Constants for FXML Paths
    private static final String MEMBERS_MANAGEMENT_PATH = "/com/example/club_sporting_final/admin/MembersManagement.fxml";
    private static final String TEAM_MANAGEMENT_PATH = "/com/example/club_sporting_final/admin/TeamManagementPage.fxml";
    private static final String EXPENSE_MANAGEMENT_PATH = "/com/example/club_sporting_final/admin/ExpenseManagement.fxml";
    private static final String SUBSCRIPTIONS_MANAGEMENT_PATH = "/com/example/club_sporting_final/admin/SubscriptionsManagement.fxml";
    private static final String REPORTS_PATH = "/com/example/club_sporting_final/admin/Reports.fxml";
    private static final String LOGIN_PATH = "/com/example/club_sporting_final/login/Login.fxml";

    // FXML Components
    @FXML
    private PieChart membersChart;

    @FXML
    private BarChart<String, Number> incomeChart;

    @FXML
    private CategoryAxis incomeXAxis;

    @FXML
    private NumberAxis incomeYAxis;

    // Navigation methods
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

    // Initialize charts
    @FXML
    private void initialize() {
        loadMembersStatistics();
        loadIncomeStatistics();
    }

    private void loadMembersStatistics() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        String query = "SELECT subscriptionStatus, COUNT(*) AS count " +
                "FROM members " +
                "GROUP BY subscriptionStatus";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String status = rs.getString("subscriptionStatus");
                int count = rs.getInt("count");
                data.add(new PieChart.Data(status + " (" + count + ")", count));
            }

            membersChart.setData(data);
            membersChart.setTitle("Membership Distribution");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadIncomeStatistics() {
        ObservableList<BarChart.Data<String, Number>> incomeData = FXCollections.observableArrayList();

        String query = "SELECT WEEK(startDate) AS week, SUM(amount) AS totalIncome " +
                "FROM subscriptions " +
                "WHERE MONTH(startDate) = MONTH(CURDATE()) AND YEAR(startDate) = YEAR(CURDATE()) " +
                "GROUP BY WEEK(startDate)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            javafx.scene.chart.XYChart.Series<String, Number> series = new javafx.scene.chart.XYChart.Series<>();
            series.setName("Weekly Income");

            while (rs.next()) {
                String week = "Week " + rs.getInt("week");
                double totalIncome = rs.getDouble("totalIncome");
                series.getData().add(new javafx.scene.chart.XYChart.Data<>(week, totalIncome));
            }

            incomeChart.getData().clear();
            incomeChart.getXAxis().setLabel("Weeks");
            incomeChart.getYAxis().setLabel("Income ($)");
            incomeChart.getData().add(series);
            incomeChart.setTitle("Income This Month");

        } catch (SQLException e) {
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
