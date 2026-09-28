package com.joysistvi.recordingapp.models;

import java.time.LocalDateTime;

public record Playlist(
        Integer id,
        Integer userId,
        LocalDateTime createdAt
) {
}
