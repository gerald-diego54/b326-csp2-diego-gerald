package com.joysistvi.recordingapp.views.dashboard;

import com.joysistvi.recordingapp.controller.AlbumController;
import com.joysistvi.recordingapp.controller.SongController;
import com.joysistvi.recordingapp.models.Song;
import com.joysistvi.recordingapp.utils.ConsoleUtils;
import com.joysistvi.recordingapp.utils.ValidationUtils;
import com.joysistvi.recordingapp.views.enums.ESongManagementScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class SongManagementView {

    private final Scanner scanner;
    private final SongController songController;
    private final AlbumController albumController;
    private static final Logger logger = LoggerFactory.getLogger(SongManagementView.class);

    public SongManagementView(SongController songController, AlbumController albumController, Scanner scanner) {
        this.scanner = scanner;
        this.songController = songController;
        this.albumController = albumController;
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
                ConsoleUtils.pressEnterToContinue(scanner);
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
        ConsoleUtils.clearScreen();
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
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("VIEW ALL SONGS");
        List<Song> songs = songController.getAllSongs();
        printSongs(songs);
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void searchSong() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("SEARCH SONGS");
        System.out.print("Enter title: ");
        String key = scanner.nextLine().trim();
        List<Song> songs = songController.searchSong(key);
        printSongs(songs);
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void addSong() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("ADD SONG");
        System.out.print("Enter song title: ");
        String title = scanner.nextLine().trim();

        if (!ValidationUtils.isValidLength(title, 255)) {
            System.out.println("[!] Song title must be 1-255 characters.");
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        System.out.print("Enter song length (mm:ss, e.g. 3:45): ");
        String length = scanner.nextLine().trim();

        if (!ValidationUtils.isValidSongLength(length)) {
            System.out.println("[!] Song length must be in mm:ss format, e.g. 3:45.");
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        System.out.print("Enter genre: ");
        String genre = scanner.nextLine().trim();

        if (!ValidationUtils.isValidLength(genre, 45)) {
            System.out.println("[!] Genre must be 1-45 characters.");
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        AlbumManagementView.printAlbums(albumController.getAllAlbums());
        int albumId = ConsoleUtils.readPositiveInt(scanner, "Enter album ID: ");
        if (albumId == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        Song song = new Song(null, title, length, genre, albumId);

        boolean isSuccess = songController.addSong(song);
        if (isSuccess) {
            System.out.println("\n[✓] Song added successfully!");
            printSongs(songController.getAllSongs());
        } else {
            logger.error("Failed to add song: {}", title);
            System.out.println("[!] Failed to add song. Make sure the album ID exists.");
        }
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void updateSong() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("UPDATE SONG");
        printSongs(songController.getAllSongs());
        int id = ConsoleUtils.readPositiveInt(scanner, "Enter song ID to update: ");
        if (id == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        System.out.print("Enter new song title: ");
        String title = scanner.nextLine().trim();

        if (!ValidationUtils.isValidLength(title, 255)) {
            System.out.println("[!] Song title must be 1-255 characters.");
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        System.out.print("Enter new song length (mm:ss, e.g. 3:45): ");
        String length = scanner.nextLine().trim();

        if (!ValidationUtils.isValidSongLength(length)) {
            System.out.println("[!] Song length must be in mm:ss format, e.g. 3:45.");
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        System.out.print("Enter new genre: ");
        String genre = scanner.nextLine().trim();

        if (!ValidationUtils.isValidLength(genre, 45)) {
            System.out.println("[!] Genre must be 1-45 characters.");
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        AlbumManagementView.printAlbums(albumController.getAllAlbums());
        int albumId = ConsoleUtils.readPositiveInt(scanner, "Enter new album ID: ");
        if (albumId == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        Song song = new Song(id, title, length, genre, albumId);
        boolean isSuccess = songController.updateSong(song);
        if (isSuccess) {
            System.out.println("\n[✓] Song updated successfully!");
        } else {
            logger.error("Failed to update song ID: {}", id);
            System.out.println("[!] Failed to update song or ID not found.");
        }
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void deleteSong() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("DELETE SONG");
        printSongs(songController.getAllSongs());
        int id = ConsoleUtils.readPositiveInt(scanner, "Enter song ID to delete: ");
        if (id == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        boolean confirmed = ConsoleUtils.confirm(scanner, "Are you sure you want to delete ID " + id + "? (y/N): ");

        if (confirmed) {
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
        ConsoleUtils.pressEnterToContinue(scanner);
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
