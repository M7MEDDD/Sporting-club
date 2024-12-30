package com.example.club_sporting_final.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Singleton instance
    private static DatabaseConnection instance;


    // Database credentials
//<<<<<<< HEAD
    private static final String URL = "jdbc:mysql://localhost:3306/sportingclub"; // Update the URL if needed
    private static final String USER = "root"; // Update with your username
    private static final String PASSWORD = ""; // Update with your password
//=======

//>>>>>>> be5da411db56b315be0e1d8e010d71902cce5756

    // Connection object
    private Connection connection;

    // Private constructor for singleton
    private DatabaseConnection() {
        try {
            System.out.println("Initializing database connection...");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
//<<<<<<< HEAD
            connection.setAutoCommit(true); // Enable auto-commit for transactions
//=======
            connection.setAutoCommit(true);
//>>>>>>> be5da411db56b315be0e1d8e010d71902cce5756
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
            if (connection != null && !connection.isClosed()) {
                System.out.println("Database connection test passed.");
                dbConnection.closeConnection();
            } else {
                System.err.println("Database connection test failed: Connection is null or closed.");
            }
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
