package com.joysistvi.recordingapp.database;

import com.joysistvi.recordingapp.config.DatabaseConfig;
import com.joysistvi.recordingapp.config.PropertiesConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private final DatabaseConfig config;

    public DatabaseConnection() {

        PropertiesConfig properties = new PropertiesConfig();

        this.config = new DatabaseConfig(properties);
    }


    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(config.url(), config.username(), config.password());
    }

    public boolean isConnected() {

        try (Connection connection = getConnection()) {

            return connection != null && !connection.isClosed();

        } catch (SQLException e) {

            return false;
        }
    }

    public void testConnection() {

        try (Connection connection = getConnection()) {

            if (connection != null && !connection.isClosed()) {

                System.out.println("Database connected successfully.");

            } else {

                System.err.println("Database connection failed.");
            }

        } catch (SQLException e) {

            System.err.println("Database connection failed.");

            System.err.println("Reason: " + e.getMessage());
        }
    }
}
