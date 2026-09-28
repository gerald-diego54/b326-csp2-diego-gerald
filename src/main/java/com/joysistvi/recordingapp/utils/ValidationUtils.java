package com.joysistvi.recordingapp.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,50}$");
    private static final Pattern SONG_LENGTH_PATTERN = Pattern.compile("^[0-9]{1,2}:[0-5][0-9]$");
    private static final Pattern YEAR_PATTERN = Pattern.compile("^[0-9]{4}$");

    private ValidationUtils() {
    }

    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidLength(String value, int maxLength) {
        return isNotBlank(value) && value.trim().length() <= maxLength;
    }

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }

    public static boolean isValidSongLength(String length) {
        return length != null && SONG_LENGTH_PATTERN.matcher(length).matches();
    }

    public static boolean isValidYear(String year) {
        return year != null && YEAR_PATTERN.matcher(year).matches();
    }
}
