package com.joysistvi.recordingapp.database;

import com.joysistvi.recordingapp.config.DatabaseConfig;
import com.joysistvi.recordingapp.config.PropertiesConfig;
import org.flywaydb.core.Flyway;

public class DatabaseMigration {

    private final DatabaseConfig config;

    public DatabaseMigration() {
        PropertiesConfig properties = new PropertiesConfig();
        this.config = new DatabaseConfig(properties);
    }

    public void migrate() {

        Flyway flyway = Flyway.configure()
                .dataSource(
                        config.url(),
                        config.username(),
                        config.password()
                )
                .locations("classpath:db/migration")
                .load();

        flyway.migrate();

        System.out.println("Database migration completed.");
    }
}
