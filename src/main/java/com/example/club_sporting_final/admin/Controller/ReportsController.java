package com.example.club_sporting_final.admin.Controller;

import com.example.club_sporting_final.admin.module.ReportItem;
import com.example.club_sporting_final.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportsController {

    @FXML
    private Label totalIncomeLabel;

    @FXML
    private Label totalExpensesLabel;

    @FXML
    private Label netBalanceLabel;

    @FXML
    private ChoiceBox<String> filterChoiceBox;

    @FXML
    private TableView<ReportItem> reportsTable;

    @FXML
    private TableColumn<ReportItem, String> columnName;

    @FXML
    private TableColumn<ReportItem, String> columnCategory;

    @FXML
    private TableColumn<ReportItem, Double> columnAmount;

    @FXML
    private TableColumn<ReportItem, String> columnDate;

    @FXML
    private TableColumn<ReportItem, String> columnDetails;

    private ObservableList<ReportItem> reportItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up table columns
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnDetails.setCellValueFactory(new PropertyValueFactory<>("details"));

        // Load filters into ChoiceBox
        filterChoiceBox.setItems(FXCollections.observableArrayList("All", "Subscriptions", "Expenses", "Teams", "Members"));
        filterChoiceBox.setValue("All");

        // Load initial data
        loadReportsData();
        calculateSummary();
    }

    private void loadReportsData() {
        reportItems.clear();

        String query = "SELECT " +
                "    MemberID AS Name, " +
                "    'Income' AS Category, " +
                "    Amount, " +
                "    StartDate AS Date, " +
                "    'Subscription' AS Details " +
                "FROM subscriptions " +
                "UNION ALL " +
                "SELECT " +
                "    ExpenseID AS Name, " +
                "    'Expense' AS Category, " +
                "    Amount, " +
                "    Date, " +
                "    'Expense Details' AS Details " +
                "FROM expenses;";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reportItems.add(new ReportItem(
                        rs.getString("Name"),
                        rs.getString("Category"),
                        rs.getDouble("Amount"),
                        rs.getString("Date"),
                        rs.getString("Details")
                ));
            }
            reportsTable.setItems(reportItems);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load reports data: " + e.getMessage());
        }
    }

    private void calculateSummary() {
        double totalIncome = 0;
        double totalExpenses = 0;

        // Iterate through reportItems and calculate totals
        for (ReportItem item : reportItems) {
            if ("Income".equalsIgnoreCase(item.getCategory())) {
                totalIncome += item.getAmount();
            } else if ("Expense".equalsIgnoreCase(item.getCategory())) {
                totalExpenses += item.getAmount();
            }
        }

        // Update labels
        totalIncomeLabel.setText(String.format("%.2f", totalIncome));
        totalExpensesLabel.setText(String.format("%.2f", totalExpenses));
        netBalanceLabel.setText(String.format("%.2f", totalIncome - totalExpenses));
    }

    @FXML
    private void handleApplyFilter(ActionEvent event) {
        String selectedFilter = filterChoiceBox.getValue();
        ObservableList<ReportItem> filteredItems = FXCollections.observableArrayList();

        for (ReportItem item : reportItems) {
            if (selectedFilter.equals("All") || item.getCategory().equalsIgnoreCase(selectedFilter)) {
                filteredItems.add(item);
            }
        }

        reportsTable.setItems(filteredItems);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
