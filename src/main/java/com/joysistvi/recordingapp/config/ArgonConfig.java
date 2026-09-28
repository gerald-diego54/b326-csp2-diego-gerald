package com.joysistvi.recordingapp.config;

public record ArgonConfig(
        int iterations,
        int memory,
        int parallelism
) {

    public ArgonConfig(PropertiesConfig config) {
        this(
                Integer.parseInt(config.get("argon.iterations")),
                Integer.parseInt(config.get("argon.memory")),
                Integer.parseInt(config.get("argon.parallelism"))
        );
    }
}
