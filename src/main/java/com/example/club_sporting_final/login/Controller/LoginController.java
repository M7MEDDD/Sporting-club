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
        String password = passwordField.getText().trim(); // Use raw password directly

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


    /**
     * Validates the user's credentials against the database and retrieves the user role.
     *
     * @param username The username entered by the user.
     * @param password The hashed password entered by the user.
     * @return The role of the user ("admin" or "emp") if valid, null otherwise.
     */
    private String validateCredentials(String username, String password) {
        String query = "SELECT role FROM login WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password); // Use raw password as provided by the user
            System.out.println("Executing query: " + stmt);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role"); // Return the role of the user
            }
        } catch (Exception e) {
            System.err.println("Error validating credentials: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Hashes a password using SHA-256.
     *
     * @param password The plain text password to hash.
     * @return The hashed password as a hexadecimal string.
     */


    /**
     * Loads the admin dashboard after successful login.
     */
    private void loadAdminDashboard() {
        try {
            System.out.println(getClass().getResource("/com/example/club_sporting_final/admin/DashBoard.fxml"));
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


    /**
     * Loads the employee dashboard after successful login.
     */
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
