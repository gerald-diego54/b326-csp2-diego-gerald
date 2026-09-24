package com.joysistvi.recordingapp.config;

public record DatabaseConfig(
        String url,
        String username,
        String password,
        String driver
) {

    public DatabaseConfig(PropertiesConfig config) {
        this(
                config.get("db.url"),
                config.get("db.username"),
                config.get("db.password"),
                config.get("db.driver")
        );
    }
}
