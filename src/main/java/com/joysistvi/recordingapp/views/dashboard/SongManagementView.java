package com.joysistvi.recordingapp.views.dashboard;

import com.joysistvi.recordingapp.controller.SongController;
import com.joysistvi.recordingapp.models.Song;
import com.joysistvi.recordingapp.utils.ConsoleUtils;
import com.joysistvi.recordingapp.views.enums.ESongManagementScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class SongManagementView {

    private final Scanner scanner;
    private final SongController songController;
    private static final Logger logger = LoggerFactory.getLogger(SongManagementView.class);

    public SongManagementView(SongController songController, Scanner scanner) {
        this.scanner = scanner;
        this.songController = songController;
    }

    public void start() {
        boolean inMenu = true;

        while (inMenu) {
            printMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            ESongManagementScreen selected = songManagementScreen(choice);

            if (selected == null) {
                System.out.println("\n[!] Invalid option. Please enter a valid number (0-5).");
                continue;
            }

            switch (selected) {
                case VIEW_ALL -> viewAllSongs();
                case SEARCH -> searchSong();
                case ADD -> addSong();
                case UPDATE -> updateSong();
                case DELETE -> deleteSong();
                case BACK -> {
                    System.out.println("\nReturning to main dashboard...");
                    inMenu = false;
                }
            }
        }
    }

    private void printMenu() {
       ConsoleUtils.printHeader("SONGS MANAGEMENT");
        System.out.println("1. View All Songs");
        System.out.println("2. Search Song");
        System.out.println("3. Add Song");
        System.out.println("4. Update Song");
        System.out.println("5. Delete Song");
        System.out.println("0. Back");
    }

    private ESongManagementScreen songManagementScreen(String key) {
        return switch (key) {
            case "1" -> ESongManagementScreen.VIEW_ALL;
            case "2" -> ESongManagementScreen.SEARCH;
            case "3" -> ESongManagementScreen.ADD;
            case "4" -> ESongManagementScreen.UPDATE;
            case "5" -> ESongManagementScreen.DELETE;
            case "0" -> ESongManagementScreen.BACK;
            default -> null;
        };
    }

    private void viewAllSongs() {
        ConsoleUtils.printHeader("VIEW ALL SONGS");
        List<Song> songs = songController.getAllSongs();
        printSongs(songs);
    }

    private void searchSong() {
        ConsoleUtils.printHeader("SEARCH SONGS");
        System.out.print("Enter title: ");
        String key = scanner.nextLine().trim();
        List<Song> songs = songController.searchSong(key);
        printSongs(songs);
    }

    private void addSong() {
        ConsoleUtils.printHeader("ADD SONG");
        System.out.print("Enter song title: ");
        String title = scanner.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("[!] Song title cannot be empty.");
            return;
        }

        System.out.print("Enter song length (e.g. 3:45): ");
        String length = scanner.nextLine().trim();

        System.out.print("Enter genre: ");
        String genre = scanner.nextLine().trim();

        int albumId = parseIntegerInput("Enter album ID: ");
        if (albumId == -1) return;

        Song song = new Song(null, title, length, genre, albumId);

        boolean isSuccess = songController.addSong(song);
        if (isSuccess) {
            System.out.println("\n[✓] Song added successfully!");
            viewAllSongs();
        } else {
            logger.error("Failed to add song: {}", title);
            System.out.println("[!] Failed to add song. Make sure the album ID exists.");
        }
    }

    private void updateSong() {
        ConsoleUtils.printHeader("UPDATE SONG");
        int id = parseIntegerInput("Enter song ID to update: ");
        if (id == -1) return;

        System.out.print("Enter new song title: ");
        String title = scanner.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("[!] Song title cannot be empty.");
            return;
        }

        System.out.print("Enter new song length (e.g. 3:45): ");
        String length = scanner.nextLine().trim();

        System.out.print("Enter new genre: ");
        String genre = scanner.nextLine().trim();

        int albumId = parseIntegerInput("Enter new album ID: ");
        if (albumId == -1) return;

        Song song = new Song(id, title, length, genre, albumId);
        boolean isSuccess = songController.updateSong(song);
        if (isSuccess) {
            System.out.println("\n[✓] Song updated successfully!");
        } else {
            logger.error("Failed to update song ID: {}", id);
            System.out.println("[!] Failed to update song or ID not found.");
        }
    }

    private void deleteSong() {
        ConsoleUtils.printHeader("DELETE SONG");
        int id = parseIntegerInput("Enter song ID to delete: ");
        if (id == -1) return;

        System.out.print("Are you sure you want to delete ID " + id + "? (y/N): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            boolean isSuccess = songController.deleteSong(id);
            if (isSuccess) {
                System.out.println("\n[✓] Song deleted!");
            } else {
                logger.error("Failed to delete song ID: {}", id);
                System.out.println("[!] Failed to delete song.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private int parseIntegerInput(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid number format.");
            return -1;
        }
    }

    public static void printSongs(List<Song> songs) {
        if (songs == null || songs.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }

        String border = "+" + "-".repeat(6) + "+" + "-".repeat(24) + "+" + "-".repeat(10) + "+" + "-".repeat(14) + "+" + "-".repeat(10) + "+";

        System.out.println(border);
        System.out.printf("| %-4s | %-22s | %-8s | %-12s | %-8s |%n", "ID", "Title", "Length", "Genre", "Album ID");
        System.out.println(border);

        for (Song song : songs) {
            String title = song.title() != null ? song.title() : "";

            if (title.length() > 22) {
                title = title.substring(0, 19) + "...";
            }

            System.out.printf("| %-4s | %-22s | %-8s | %-12s | %-8s |%n",
                    song.id(), title, song.length(), song.genre(), song.albumId());
        }

        System.out.println(border);
    }
}
