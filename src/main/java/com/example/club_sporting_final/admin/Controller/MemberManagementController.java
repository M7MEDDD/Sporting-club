package com.example.club_sporting_final.admin.Controller;

import com.example.club_sporting_final.admin.module.Members;
import com.example.club_sporting_final.utils.DatabaseConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberManagementController {

    @FXML
    private TextField textSearch;

    @FXML
    private Button btSearch;

    @FXML
    private TableView<Members> memberTable;

    @FXML
    private TableColumn<Members, Integer> idColumn;

    @FXML
    private TableColumn<Members, String> nameColumn;

    @FXML
    private TableColumn<Members, String> emailColumn;

    @FXML
    private TableColumn<Members, String> phoneColumn;

    @FXML
    private TableColumn<Members, String> statusColumn;

    @FXML
    private Button addButton, editButton, deleteButton, backButton;

    private ObservableList<Members> memberList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize TableView columns
        idColumn.setCellValueFactory(data -> data.getValue().memberIDProperty().asObject());
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        emailColumn.setCellValueFactory(data -> data.getValue().emailProperty());
        phoneColumn.setCellValueFactory(data -> data.getValue().phoneNumberProperty());
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().isSubscriptionStatus() ? "Active" : "Inactive"));

        // Load data into TableView
        loadMembers();

        // Set button actions
        addButton.setOnAction(e -> openAddMemberForm());
        editButton.setOnAction(e -> openEditMemberForm());
        deleteButton.setOnAction(e -> deleteMember());
        backButton.setOnAction(e -> returnToDashboard());
        btSearch.setOnAction(e -> searchMembers());
    }

    private void loadMembers() {
        memberList.clear();
        String query = "SELECT MemberID, Name, Email, PhoneNumber, SubscriptionStatus FROM Members";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                boolean subscriptionStatus = "active".equalsIgnoreCase(rs.getString("SubscriptionStatus"));
                memberList.add(new Members(
                        rs.getInt("MemberID"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        rs.getString("PhoneNumber"),
                        subscriptionStatus
                ));
            }
        } catch (SQLException e) {
            showError("Database Error", "Could not load members: " + e.getMessage());
        }
        memberTable.setItems(memberList);
    }

    @FXML
    private void searchMembers() {
        String searchQuery = textSearch.getText().trim();
        if (searchQuery.isEmpty()) {
            showError("Search Error", "Search field cannot be empty.");
            return;
        }

        memberList.clear();
        String query = "SELECT MemberID, Name, Email, PhoneNumber, SubscriptionStatus FROM Members WHERE MemberID = ? OR Name LIKE ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, searchQuery);
            stmt.setString(2, "%" + searchQuery + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                boolean subscriptionStatus = "active".equalsIgnoreCase(rs.getString("SubscriptionStatus"));
                memberList.add(new Members(
                        rs.getInt("MemberID"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        rs.getString("PhoneNumber"),
                        subscriptionStatus
                ));
            }

            if (memberList.isEmpty()) {
                showError("No Results", "No members found with the given ID or name.");
            }

        } catch (SQLException e) {
            showError("Database Error", "An error occurred while searching for members: " + e.getMessage());
        }

        memberTable.setItems(memberList);
    }

    private void openAddMemberForm() {
        openForm("/com/example/club_sporting_final/admin/AddMembersPage.fxml", "Add Member");
    }

    private void openEditMemberForm() {
        Members selectedMember = memberTable.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a member to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/club_sporting_final/admin/EditMember.fxml"));
            Parent root = loader.load();

            EditMemberController controller = loader.getController();
            controller.setMember(selectedMember);

            Stage stage = new Stage();
            stage.setTitle("Edit Member");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadMembers(); // Reload after editing
        } catch (IOException e) {
            showError("Navigation Error", "Could not open the Edit Member page.");
        }
    }

    private void deleteMember() {
        Members selectedMember = memberTable.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a member to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Delete");
        confirmationAlert.setHeaderText("Are you sure you want to delete this member?");
        confirmationAlert.setContentText("Member ID: " + selectedMember.getMemberID() + "\nName: " + selectedMember.getName());

        if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try (Connection connection = DatabaseConnection.getInstance().getConnection();
                 PreparedStatement stmt = connection.prepareStatement("DELETE FROM members WHERE MemberID = ?")) {

                stmt.setInt(1, selectedMember.getMemberID());
                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    memberList.remove(selectedMember); // Update the TableView
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Member deleted successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the member.");
                }
            } catch (SQLException e) {
                showError("Database Error", "An error occurred while deleting the member: " + e.getMessage());
            }
        }
    }

    @FXML
    private void returnToDashboard() {
        openForm("/com/example/club_sporting_final/admin/DashBoard.fxml", "Dashboard");
    }

    private void openForm(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadMembers();
        } catch (IOException e) {
            showError("Navigation Error", "Could not open the requested form.");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
