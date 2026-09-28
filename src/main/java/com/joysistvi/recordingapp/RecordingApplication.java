package com.joysistvi.recordingapp;

import com.joysistvi.recordingapp.database.DatabaseBootstrap;
import com.joysistvi.recordingapp.database.DatabaseConnection;
import com.joysistvi.recordingapp.database.DatabaseMigration;
import com.joysistvi.recordingapp.database.DatabaseSeeder;
import com.joysistvi.recordingapp.views.Route;

public class RecordingApplication {

    public static void main(String[] args) {

        DatabaseBootstrap bootstrap = new DatabaseBootstrap(); // Create database upon running the application
        DatabaseMigration migration = new DatabaseMigration(); // Create tables upon running the application
        DatabaseConnection databaseConnection = new DatabaseConnection();
        DatabaseSeeder seeder = new DatabaseSeeder(); // Populate sample data if the database is empty

        bootstrap.createDatabaseIfNotExists();
        migration.migrate();
        databaseConnection.testConnection();
        seeder.seed();

        Route router = new Route();
        router.start();

    }
}
