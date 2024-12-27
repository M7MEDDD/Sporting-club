package com.example.club_sporting_final.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Singleton instance
    private static DatabaseConnection instance;

    // Database credentials
    private final String URL = "jdbc:mysql://localhost:3306/sportingclub"; // Update the URL if needed
    private final String USER = "root"; // Update with your username
    private final String PASSWORD = ""; // Update with your password

    // Connection object
    private Connection connection;

    // Private constructor for singleton
    private DatabaseConnection() {
        try {
            System.out.println("Initializing database connection...");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(true); // Ensure auto-commit is enabled
            System.out.println("Database connection established successfully.");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Get the singleton instance
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Get the connection
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            System.out.println("Reinitializing database connection...");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(true); // Ensure auto-commit is enabled
            System.out.println("Database connection reestablished.");
        }
        return connection;
    }

    // Close the connection
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Test the database connection
    public static void main(String[] args) {
        try {
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            Connection connection = dbConnection.getConnection();
            System.out.println("Database connection test passed.");
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
