package com.joysistvi.recordingapp.database;

import com.joysistvi.recordingapp.config.DatabaseConfig;
import com.joysistvi.recordingapp.config.PropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseBootstrap {

    private final DatabaseConfig config;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseBootstrap.class);

    public DatabaseBootstrap() {
        PropertiesConfig properties = new PropertiesConfig();
        this.config = new DatabaseConfig(properties);
    }

    public void createDatabaseIfNotExists() {

        String databaseName = getDatabaseName();
        String serverUrl = getServerUrl();

        String sql = "CREATE DATABASE IF NOT EXISTS `" + databaseName + "` " + "DEFAULT CHARACTER SET utf8mb4 " + "COLLATE utf8mb4_0900_ai_ci";

        try (Connection connection = DriverManager.getConnection(serverUrl, config.username(), config.password())) {

            try (Statement statement = connection.createStatement()) {

                statement.executeUpdate(sql);

                logger.info("Database '" + databaseName + "' is ready.");
            }

        } catch (SQLException e) {

            logger.error("Failed to create database '" + databaseName + "'.", e);
        }
    }

    private String getDatabaseName() {

        String url = config.url();

        String withoutParams = url.split("\\?")[0];

        int lastSlash = withoutParams.lastIndexOf('/');

        if (lastSlash == -1 || lastSlash == withoutParams.length() - 1) {
            logger.error("Invalid database URL: " + url);
        }

        return withoutParams.substring(lastSlash + 1);
    }

    private String getServerUrl() {

        String url = config.url();

        String withoutParams = url.split("\\?")[0];

        int lastSlash = withoutParams.lastIndexOf('/');

        if (lastSlash == -1) {
            logger.error("Invalid database URL: " + url);
        }

        return withoutParams.substring(0, lastSlash);
    }
}
