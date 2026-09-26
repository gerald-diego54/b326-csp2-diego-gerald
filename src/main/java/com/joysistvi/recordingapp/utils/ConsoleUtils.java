package com.joysistvi.recordingapp.utils;

public class ConsoleUtils {

    public static final int LINE_LENGTH = 33;

    public static void printHeader(String title) {
        String divider = "-".repeat(LINE_LENGTH);

        int padding = Math.max(0, (LINE_LENGTH - title.length()) / 2);
        String centeredTitle = " ".repeat(padding) + title;

        System.out.println("\n" + divider);
        System.out.println(centeredTitle);
        System.out.println(divider);
    }
}