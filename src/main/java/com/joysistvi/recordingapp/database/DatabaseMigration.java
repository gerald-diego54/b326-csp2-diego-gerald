package com.joysistvi.recordingapp.database;

import com.joysistvi.recordingapp.config.DatabaseConfig;
import com.joysistvi.recordingapp.config.PropertiesConfig;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseMigration {

    private final DatabaseConfig config;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseMigration.class);

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

        logger.info("Database migration completed.");
    }
}
