package com.joysistvi.recordingapp.views.dashboard;

import com.joysistvi.recordingapp.controller.AlbumController;
import com.joysistvi.recordingapp.controller.ArtistController;
import com.joysistvi.recordingapp.models.Album;
import com.joysistvi.recordingapp.utils.ConsoleUtils;
import com.joysistvi.recordingapp.utils.ValidationUtils;
import com.joysistvi.recordingapp.views.enums.EAlbumManagementScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class AlbumManagementView {

    private final Scanner scanner;
    private final AlbumController albumController;
    private final ArtistController artistController;
    private static final Logger logger = LoggerFactory.getLogger(AlbumManagementView.class);

    public AlbumManagementView(AlbumController albumController, ArtistController artistController, Scanner scanner) {
        this.scanner = scanner;
        this.albumController = albumController;
        this.artistController = artistController;
    }

    public void start() {
        boolean inMenu = true;

        while (inMenu) {
            printMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            EAlbumManagementScreen selected = albumManagementScreen(choice);

            if (selected == null) {
                System.out.println("\n[!] Invalid option. Please enter a valid number (0-5).");
                ConsoleUtils.pressEnterToContinue(scanner);
                continue;
            }

            switch (selected) {
                case VIEW_ALL -> viewAllAlbums();
                case SEARCH -> searchAlbum();
                case ADD -> addAlbum();
                case UPDATE -> updateAlbum();
                case DELETE -> deleteAlbum();
                case BACK -> {
                    System.out.println("\nReturning to main dashboard...");
                    inMenu = false;
                }
            }
        }
    }

    private void printMenu() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("ALBUM MANAGEMENT");
        System.out.println("1. View All Albums");
        System.out.println("2. Search Album");
        System.out.println("3. Add Album");
        System.out.println("4. Update Album");
        System.out.println("5. Delete Album");
        System.out.println("0. Back");
    }

    private EAlbumManagementScreen albumManagementScreen(String key) {
        return switch (key) {
            case "1" -> EAlbumManagementScreen.VIEW_ALL;
            case "2" -> EAlbumManagementScreen.SEARCH;
            case "3" -> EAlbumManagementScreen.ADD;
            case "4" -> EAlbumManagementScreen.UPDATE;
            case "5" -> EAlbumManagementScreen.DELETE;
            case "0" -> EAlbumManagementScreen.BACK;
            default -> null;
        };
    }

    private void viewAllAlbums() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("VIEW ALL ALBUMS");
        List<Album> albums = albumController.getAllAlbums();
        printAlbums(albums);
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void searchAlbum() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("SEARCH ALBUMS");
        System.out.print("Enter name: ");
        String key = scanner.nextLine().trim();
        List<Album> albums = albumController.searchAlbum(key);
        printAlbums(albums);
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void addAlbum() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("ADD ALBUM");
        System.out.print("Enter album name: ");
        String name = scanner.nextLine().trim();

        if (!ValidationUtils.isValidLength(name, 255)) {
            System.out.println("[!] Album name must be 1-255 characters.");
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        System.out.print("Enter release year (YYYY, leave blank if unknown): ");
        String yearInput = scanner.nextLine().trim();

        if (!yearInput.isEmpty() && !ValidationUtils.isValidYear(yearInput)) {
            System.out.println("[!] Year must be a 4-digit number, e.g. 1995.");
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        Integer year = yearInput.isEmpty() ? null : Integer.parseInt(yearInput);

        ArtistManagementView.printArtists(artistController.getAllArtists());
        int artistId = ConsoleUtils.readPositiveInt(scanner, "Enter artist ID: ");
        if (artistId == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        Album album = new Album(null, name, year, artistId);

        boolean isSuccess = albumController.addAlbum(album);
        if (isSuccess) {
            System.out.println("\n[✓] Album added successfully!");
            printAlbums(albumController.getAllAlbums());
        } else {
            logger.error("Failed to add album: {}", name);
            System.out.println("[!] Failed to add album. Make sure the artist ID exists.");
        }
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void updateAlbum() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("UPDATE ALBUM");
        printAlbums(albumController.getAllAlbums());
        int id = ConsoleUtils.readPositiveInt(scanner, "Enter album ID to update: ");
        if (id == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        System.out.print("Enter new album name: ");
        String name = scanner.nextLine().trim();

        if (!ValidationUtils.isValidLength(name, 255)) {
            System.out.println("[!] Album name must be 1-255 characters.");
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        System.out.print("Enter new release year (YYYY, leave blank if unknown): ");
        String yearInput = scanner.nextLine().trim();

        if (!yearInput.isEmpty() && !ValidationUtils.isValidYear(yearInput)) {
            System.out.println("[!] Year must be a 4-digit number, e.g. 1995.");
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        Integer year = yearInput.isEmpty() ? null : Integer.parseInt(yearInput);

        ArtistManagementView.printArtists(artistController.getAllArtists());
        int artistId = ConsoleUtils.readPositiveInt(scanner, "Enter new artist ID: ");
        if (artistId == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        Album album = new Album(id, name, year, artistId);
        boolean isSuccess = albumController.updateAlbum(album);
        if (isSuccess) {
            System.out.println("\n[✓] Album updated successfully!");
        } else {
            logger.error("Failed to update album ID: {}", id);
            System.out.println("[!] Failed to update album or ID not found.");
        }
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void deleteAlbum() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("DELETE ALBUM");
        printAlbums(albumController.getAllAlbums());
        int id = ConsoleUtils.readPositiveInt(scanner, "Enter album ID to delete: ");
        if (id == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        boolean confirmed = ConsoleUtils.confirm(scanner, "Are you sure you want to delete ID " + id + "? (y/N): ");

        if (confirmed) {
            boolean isSuccess = albumController.deleteAlbum(id);
            if (isSuccess) {
                System.out.println("\n[✓] Album deleted!");
            } else {
                logger.error("Failed to delete album ID: {}", id);
                System.out.println("[!] Failed to delete album.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    public static void printAlbums(List<Album> albums) {
        if (albums == null || albums.isEmpty()) {
            System.out.println("No albums found.");
            return;
        }

        String border = "+" + "-".repeat(6) + "+" + "-".repeat(24) + "+" + "-".repeat(8) + "+" + "-".repeat(10) + "+";

        System.out.println(border);
        System.out.printf("| %-4s | %-22s | %-6s | %-8s |%n", "ID", "Name", "Year", "Artist ID");
        System.out.println(border);

        for (Album album : albums) {
            String name = album.name() != null ? album.name() : "";

            if (name.length() > 22) {
                name = name.substring(0, 19) + "...";
            }

            System.out.printf("| %-4s | %-22s | %-6s | %-8s |%n",
                    album.id(), name, album.year(), album.artistId());
        }

        System.out.println(border);
    }
}
