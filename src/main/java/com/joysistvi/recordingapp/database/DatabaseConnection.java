package com.joysistvi.recordingapp.database;

import com.joysistvi.recordingapp.config.DatabaseConfig;
import com.joysistvi.recordingapp.config.PropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private final DatabaseConfig config;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);

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

            logger.error(String.valueOf(e));
            return false;
        }
    }

    public void testConnection() {

        try (Connection connection = getConnection()) {

            if (connection != null && !connection.isClosed()) {

                logger.info("Database connected successfully.");

            } else {

                logger.error("Database connection failed.");
            }

        } catch (SQLException e) {

            logger.error("Database connection failed.");

            logger.error("Reason: " + e.getMessage());
        }
    }
}
