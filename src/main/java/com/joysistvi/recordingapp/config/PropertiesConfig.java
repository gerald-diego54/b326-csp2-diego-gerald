package com.joysistvi.recordingapp.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesConfig {

    private final Properties properties = new Properties();

    private final Dotenv dotenv = Dotenv.configure() // .env configuration instance
            .ignoreIfMissing() // remove exception when .env is missing
            .load(); // load the contents of .env file

    public PropertiesConfig() {
        load();
    }

    private void load() {

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) { // load the file application.properties

            if (input == null) {
                throw new IllegalStateException("application.properties not found");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new IllegalStateException("Unable to load application.properties");
        }
    }

    public String get(String key) {

        String value = properties.getProperty(key); // get the values by searching using key value

        if (value == null) {
            throw new IllegalStateException("Missing configuration: " + key);
        }

        return resolveEnvironmentVariable(value);
    }

    private String resolveEnvironmentVariable(String value) {

        if (!value.startsWith("${") || !value.endsWith("}")) return value;

        String environmentVariable = value.substring(2, value.length() - 1);

        // First check real OS environment variables.
        String environmentValue = System.getenv(environmentVariable);

        // If not found, check .env.
        if (environmentValue == null || environmentValue.isBlank()) {

            environmentValue = dotenv.get(environmentVariable);
        }

        if (environmentValue == null || environmentValue.isBlank()) {

            throw new IllegalStateException("Missing environment variable: " + environmentVariable);
        }

        return environmentValue;
    }
}
