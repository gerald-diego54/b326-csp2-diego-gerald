package com.joysistvi.recordingapp.utils;

import java.io.IOException;
import java.util.Scanner;

public class ConsoleUtils {

    public static final int LINE_LENGTH = 33;

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("\n".repeat(50));
        }
    }

    public static void printHeader(String title) {
        String divider = "-".repeat(LINE_LENGTH);

        int padding = Math.max(0, (LINE_LENGTH - title.length()) / 2);
        String centeredTitle = " ".repeat(padding) + title;

        System.out.println("\n" + divider);
        System.out.println(centeredTitle);
        System.out.println(divider);
    }

    public static void pressEnterToContinue(Scanner scanner) {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public static int readPositiveInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        try {
            int value = Integer.parseInt(scanner.nextLine().trim());
            return value > 0 ? value : -1;
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid number format.");
            return -1;
        }
    }

    public static boolean confirm(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }
}