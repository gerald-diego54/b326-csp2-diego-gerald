package com.joysistvi.recordingapp;

import com.joysistvi.recordingapp.database.DatabaseBootstrap;
import com.joysistvi.recordingapp.database.DatabaseConnection;
import com.joysistvi.recordingapp.database.DatabaseMigration;

public class RecordingApplication {

    public static void main(String[] args) {

        DatabaseBootstrap bootstrap = new DatabaseBootstrap(); // Create database upon running the application
        DatabaseMigration migration = new DatabaseMigration(); // Create tables upon running the application
        DatabaseConnection databaseConnection = new DatabaseConnection();

        bootstrap.createDatabaseIfNotExists();
        migration.migrate();
        databaseConnection.testConnection();

        // setup here screens for the recording app using switch statement

    }
}
