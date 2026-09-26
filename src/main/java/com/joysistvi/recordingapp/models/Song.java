package com.joysistvi.recordingapp.models;

public record Song(
        Integer id,
        String title,
        String length,
        String genre,
        Integer albumId
) {
}
