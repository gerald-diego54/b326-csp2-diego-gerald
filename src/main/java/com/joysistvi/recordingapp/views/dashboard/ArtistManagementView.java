package com.joysistvi.recordingapp.views.dashboard;

import com.joysistvi.recordingapp.controller.ArtistController;
import com.joysistvi.recordingapp.models.Artist;
import com.joysistvi.recordingapp.utils.ConsoleUtils;
import com.joysistvi.recordingapp.views.enums.EArtistManagementScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ArtistManagementView {

    private final Scanner scanner;
    private final ArtistController artistController;
    private static final Logger logger = LoggerFactory.getLogger(ArtistManagementView.class);

    public ArtistManagementView(ArtistController artistController, Scanner scanner) {
        this.scanner = scanner;
        this.artistController = artistController;
    }

    public void start() {
        boolean inMenu = true;

        while (inMenu) {
            printMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            EArtistManagementScreen selected = artistManagementScreen(choice);

            if (selected == null) {
                System.out.println("\n[!] Invalid option. Please enter a valid number (0-8).");
                continue;
            }

            switch (selected) {
                case VIEW_ALL -> viewAllArtist();
                case SEARCH -> searchArtist();
                case ADD -> addArtists();
                case UPDATE -> updateArtist();
                case ARCHIVE -> archiveArtist();
                case RESTORE -> restoreArtist();
                case DELETE -> deleteArtist();
                case VIEW_ARCHIVED -> viewAllArchivedArtists();
                case BACK -> {
                    System.out.println("\nReturning to main dashboard...");
                    inMenu = false;
                }
            }
        }
    }

    private void printMenu() {
        ConsoleUtils.printHeader("ARTIST MANAGEMENT");
        System.out.println("1. View All Artists");
        System.out.println("2. Search Artist");
        System.out.println("3. Add Artist");
        System.out.println("4. Update Artist");
        System.out.println("5. Archive Artist");
        System.out.println("6. Restore Artist");
        System.out.println("7. Delete Artist");
        System.out.println("8. View All Archived Artists");
        System.out.println("0. Back");
    }

    private EArtistManagementScreen artistManagementScreen(String key) {
        return switch (key) {
            case "1" -> EArtistManagementScreen.VIEW_ALL;
            case "2" -> EArtistManagementScreen.SEARCH;
            case "3" -> EArtistManagementScreen.ADD;
            case "4" -> EArtistManagementScreen.UPDATE;
            case "5" -> EArtistManagementScreen.ARCHIVE;
            case "6" -> EArtistManagementScreen.RESTORE;
            case "7" -> EArtistManagementScreen.DELETE;
            case "8" -> EArtistManagementScreen.VIEW_ARCHIVED;
            case "0" -> EArtistManagementScreen.BACK;
            default -> null;
        };
    }

    private void viewAllArtist() {
        ConsoleUtils.printHeader("VIEW ALL ARTISTS");
        List<Artist> artists = artistController.getAllArtists();
        printArtists(artists);
    }

    private void searchArtist() {
        ConsoleUtils.printHeader("SEARCH ARTISTS");
        System.out.print("Enter name: ");
        String key = scanner.nextLine().trim();
        List<Artist> artists = artistController.searchArtist(key);
        printArtists(artists);
    }

    private void addArtists() {
        ConsoleUtils.printHeader("ADD ARTIST");
        System.out.print("Enter artist name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("[!] Artist name cannot be empty.");
            return;
        }

        Artist artist = new Artist(null, name);

        boolean isSuccess = artistController.addArtist(artist);
        if (isSuccess) {
            System.out.println("\n[✓] Artist added successfully!");
            viewAllArtist();
        } else {
            logger.error("Failed to add artist: {}", name);
            System.out.println("[!] Failed to add artist.");
        }
    }

    private void updateArtist() {
        ConsoleUtils.printHeader("UPDATE ARTIST");
        int id = parseIntegerInput("Enter artist ID to update: ");
        if (id == -1) return;

        System.out.print("Enter new artist name: ");
        String newName = scanner.nextLine().trim();

        if (newName.isEmpty()) {
            System.out.println("[!] Artist name cannot be empty.");
            return;
        }

        Artist artist = new Artist(id, newName);
        boolean isSuccess = artistController.updateArtist(artist);
        if (isSuccess) {
            System.out.println("\n[✓] Artist updated successfully!");
        } else {
            logger.error("Failed to update artist ID: {}", id);
            System.out.println("[!] Failed to update artist or ID not found.");
        }
    }

    private void archiveArtist() {
        ConsoleUtils.printHeader("ARCHIVE ARTIST");
        int id = parseIntegerInput("Enter artist ID to archive: ");
        if (id == -1) return;

        boolean isSuccess = artistController.handleArchiveArtist(id);
        if (isSuccess) {
            System.out.println("\n[✓] Artist archived successfully!");
        } else {
            logger.error("Failed to archive artist ID: {}", id);
            System.out.println("[!] Failed to archive artist or ID not found.");
        }
    }

    private void restoreArtist() {
        ConsoleUtils.printHeader("RESTORE ARTIST");
        int id = parseIntegerInput("Enter artist ID to restore: ");
        if (id == -1) return;

        boolean isSuccess = artistController.handleRestoreArtist(id);
        if (isSuccess) {
            System.out.println("\n[✓] Artist restored successfully!");
        } else {
            logger.error("Failed to restore artist ID: {}", id);
            System.out.println("[!] Failed to restore artist or ID not found.");
        }
    }

    private void deleteArtist() {
        ConsoleUtils.printHeader("DELETE ARTIST");
        int id = parseIntegerInput("Enter artist ID for hard delete: ");
        if (id == -1) return;

        System.out.print("Are you sure you want to permanently delete ID " + id + "? (y/N): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            boolean isSuccess = artistController.deleteArtist(id);
            if (isSuccess) {
                System.out.println("\n[✓] Artist permanently deleted!");
            } else {
                logger.error("Failed to delete artist ID: {}", id);
                System.out.println("[!] Failed to delete artist.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void viewAllArchivedArtists() {
        ConsoleUtils.printHeader("ARCHIVED ARTISTS");
        List<Artist> archived = artistController.handleViewArchivedArtists();
        printArtists(archived);
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

    public static void printArtists(List<Artist> artists) {
        if (artists == null || artists.isEmpty()) {
            System.out.println("No artists found.");
            return;
        }

        String border = "+" + "-".repeat(6) + "+" + "-".repeat(24) + "+";

        System.out.println(border);
        System.out.printf("| %-4s | %-22s |%n", "ID", "Name");
        System.out.println(border);

        for (Artist artist : artists) {
            String name = artist.name() != null ? artist.name() : "";

            if (name.length() > 22) {
                name = name.substring(0, 19) + "...";
            }

            System.out.printf("| %-4s | %-22s |%n", artist._id(), name);
        }

        System.out.println(border);
    }
}