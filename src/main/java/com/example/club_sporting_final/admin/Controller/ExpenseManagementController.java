package com.example.club_sporting_final.admin.Controller;

import com.example.club_sporting_final.admin.module.Expense;
import com.example.club_sporting_final.utils.DatabaseConnection;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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

public class ExpenseManagementController {

    @FXML
    private TableView<Expense> expenseTable;

    @FXML
    private TableColumn<Expense, Integer> expenseIDColumn;

    @FXML
    private TableColumn<Expense, String> expenseTypeColumn;

    @FXML
    private TableColumn<Expense, Double> amountColumn;

    @FXML
    private TableColumn<Expense, String> dateColumn;

    @FXML
    private TableColumn<Expense, Integer> teamIDColumn;

    @FXML
    private TextField expenseTypeField;

    @FXML
    private TextField amountField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField teamIDField;

    private ObservableList<Expense> expenseList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns
        expenseIDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getExpenseID()).asObject());
        expenseTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExpenseType()));
        amountColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        teamIDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTeamID()).asObject());


        // Load expenses into the table
        loadExpenses();
    }

    private void loadExpenses() {
        expenseList.clear();
        String query = "SELECT * FROM expenses";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                expenseList.add(new Expense(
                        rs.getInt("ExpenseID"),
                        rs.getString("ExpenseType"),
                        rs.getDouble("Amount"),
                        rs.getString("Date"),
                        rs.getInt("TeamID")
                ));
            }
            expenseTable.setItems(expenseList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load expenses: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddExpense(ActionEvent event) {
        String expenseType = expenseTypeField.getText().trim();
        String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
        int teamID;
        double amount;

        try {
            amount = Double.parseDouble(amountField.getText().trim());
            teamID = Integer.parseInt(teamIDField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Amount and Team ID must be valid numbers.");
            return;
        }

        if (expenseType.isEmpty() || date.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return;
        }

        String query = "INSERT INTO expenses (ExpenseType, Amount, Date, TeamID) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, expenseType);
            stmt.setDouble(2, amount);
            stmt.setString(3, date);
            stmt.setInt(4, teamID);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                loadExpenses(); // Reload the table
                showAlert(Alert.AlertType.INFORMATION, "Success", "Expense added successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not add expense: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditExpense(ActionEvent event) {
        Expense selectedExpense = expenseTable.getSelectionModel().getSelectedItem();
        if (selectedExpense == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select an expense to edit.");
            return;
        }

        String expenseType = expenseTypeField.getText().trim();
        String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
        int teamID;
        double amount;

        try {
            amount = Double.parseDouble(amountField.getText().trim());
            teamID = Integer.parseInt(teamIDField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Amount and Team ID must be valid numbers.");
            return;
        }

        if (expenseType.isEmpty() || date.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return;
        }

        String query = "UPDATE expenses SET ExpenseType = ?, Amount = ?, Date = ?, TeamID = ? WHERE ExpenseID = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, expenseType);
            stmt.setDouble(2, amount);
            stmt.setString(3, date);
            stmt.setInt(4, teamID);
            stmt.setInt(5, selectedExpense.getExpenseID());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                loadExpenses(); // Reload the table
                showAlert(Alert.AlertType.INFORMATION, "Success", "Expense updated successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not update expense: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteExpense(ActionEvent event) {
        Expense selectedExpense = expenseTable.getSelectionModel().getSelectedItem();
        if (selectedExpense == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select an expense to delete.");
            return;
        }

        String query = "DELETE FROM expenses WHERE ExpenseID = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, selectedExpense.getExpenseID());

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                loadExpenses(); // Reload the table
                showAlert(Alert.AlertType.INFORMATION, "Success", "Expense deleted successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not delete expense: " + e.getMessage());
        }
    }

    @FXML
    private void handleClearFields(ActionEvent event) {
        expenseTypeField.clear();
        amountField.clear();
        datePicker.setValue(null);
        teamIDField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
