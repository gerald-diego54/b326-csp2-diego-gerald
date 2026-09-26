package com.joysistvi.recordingapp;

import com.joysistvi.recordingapp.controller.ArtistController;
import com.joysistvi.recordingapp.database.DatabaseBootstrap;
import com.joysistvi.recordingapp.database.DatabaseConnection;
import com.joysistvi.recordingapp.database.DatabaseMigration;
import com.joysistvi.recordingapp.models.Artist;
import com.joysistvi.recordingapp.repositories.ArtistRepository;
import com.joysistvi.recordingapp.services.ArtistService;
import com.joysistvi.recordingapp.views.Route;

import java.util.Scanner;

public class RecordingApplication {

    public static void main(String[] args) {

        DatabaseBootstrap bootstrap = new DatabaseBootstrap(); // Create database upon running the application
        DatabaseMigration migration = new DatabaseMigration(); // Create tables upon running the application
        DatabaseConnection databaseConnection = new DatabaseConnection();

        bootstrap.createDatabaseIfNotExists();
        migration.migrate();
        databaseConnection.testConnection();

        Route router = new Route();
        router.start();

    }
}
