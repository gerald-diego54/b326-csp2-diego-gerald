package com.joysistvi.recordingapp.models;

public record Album(
        Integer id,
        String name,
        Integer year,
        Integer artistId
) {
}
