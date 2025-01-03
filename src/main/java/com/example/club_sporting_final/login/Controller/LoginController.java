package com.example.club_sporting_final.login.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import com.example.club_sporting_final.utils.DatabaseConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    /**
     * Handles the login action when the Login button is pressed.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Debugging: Print username and raw password
        System.out.println("Username: " + username);
        System.out.println("Password (raw): " + password);

        String role = validateCredentials(username, password);

        if (role != null) {
            System.out.println("Login successful. Role: " + role);
            if (role.equals("admin")) {
                loadAdminDashboard();
            } else if (role.equals("emp")) {
                loadEmployeeDashboard();
            }
        } else {
            System.out.println("Login failed.");
            errorLabel.setText("Invalid username or password!");
            errorLabel.setVisible(true);
        }
    }


    private String validateCredentials(String username, String password) {
        String query = "SELECT role FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            System.out.println("Executing query: " + stmt);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (Exception e) {
            System.err.println("Error validating credentials: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    private void loadAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/club_sporting_final/admin/DashBoard.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Admin Dashboard");
        } catch (Exception e) {
            System.err.println("Error loading admin dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void loadEmployeeDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/club_sporting_final/dashboard/EmployeeDashboard.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Employee Dashboard");
        } catch (Exception e) {
            System.err.println("Error loading employee dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
