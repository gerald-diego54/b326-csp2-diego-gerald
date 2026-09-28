package com.joysistvi.recordingapp.models;

import com.joysistvi.recordingapp.models.enums.ERole;

public record User(
        Integer id,
        String username,
        String password,
        ERole role
) {
}
