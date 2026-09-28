package com.joysistvi.recordingapp.views.dashboard;

import com.joysistvi.recordingapp.controller.ArtistController;
import com.joysistvi.recordingapp.models.Artist;
import com.joysistvi.recordingapp.utils.ConsoleUtils;
import com.joysistvi.recordingapp.utils.ValidationUtils;
import com.joysistvi.recordingapp.views.enums.EArtistManagementScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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
                ConsoleUtils.pressEnterToContinue(scanner);
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
        ConsoleUtils.clearScreen();
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
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("VIEW ALL ARTISTS");
        List<Artist> artists = artistController.getAllArtists();
        printArtists(artists);
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void searchArtist() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("SEARCH ARTISTS");
        System.out.print("Enter name: ");
        String key = scanner.nextLine().trim();
        List<Artist> artists = artistController.searchArtist(key);
        printArtists(artists);
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void addArtists() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("ADD ARTIST");
        System.out.print("Enter artist name: ");
        String name = scanner.nextLine().trim();

        if (!ValidationUtils.isValidLength(name, 100)) {
            System.out.println("[!] Artist name must be 1-100 characters.");
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        Artist artist = new Artist(null, name);

        boolean isSuccess = artistController.addArtist(artist);
        if (isSuccess) {
            System.out.println("\n[✓] Artist added successfully!");
            printArtists(artistController.getAllArtists());
        } else {
            logger.error("Failed to add artist: {}", name);
            System.out.println("[!] Failed to add artist.");
        }
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void updateArtist() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("UPDATE ARTIST");
        printArtists(artistController.getAllArtists());
        int id = ConsoleUtils.readPositiveInt(scanner, "Enter artist ID to update: ");
        if (id == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        System.out.print("Enter new artist name: ");
        String newName = scanner.nextLine().trim();

        if (!ValidationUtils.isValidLength(newName, 100)) {
            System.out.println("[!] Artist name must be 1-100 characters.");
            ConsoleUtils.pressEnterToContinue(scanner);
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
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void archiveArtist() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("ARCHIVE ARTIST");
        printArtists(artistController.getAllArtists());
        int id = ConsoleUtils.readPositiveInt(scanner, "Enter artist ID to archive: ");
        if (id == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        boolean isSuccess = artistController.handleArchiveArtist(id);
        if (isSuccess) {
            System.out.println("\n[✓] Artist archived successfully!");
        } else {
            logger.error("Failed to archive artist ID: {}", id);
            System.out.println("[!] Failed to archive artist or ID not found.");
        }
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void restoreArtist() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("RESTORE ARTIST");
        printArtists(artistController.handleViewArchivedArtists());
        int id = ConsoleUtils.readPositiveInt(scanner, "Enter artist ID to restore: ");
        if (id == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        boolean isSuccess = artistController.handleRestoreArtist(id);
        if (isSuccess) {
            System.out.println("\n[✓] Artist restored successfully!");
        } else {
            logger.error("Failed to restore artist ID: {}", id);
            System.out.println("[!] Failed to restore artist or ID not found.");
        }
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void deleteArtist() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("DELETE ARTIST");
        printArtists(artistController.getAllArtists());
        int id = ConsoleUtils.readPositiveInt(scanner, "Enter artist ID for hard delete: ");
        if (id == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        boolean confirmed = ConsoleUtils.confirm(scanner, "Are you sure you want to permanently delete ID " + id + "? (y/N): ");

        if (confirmed) {
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
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void viewAllArchivedArtists() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("ARCHIVED ARTISTS");
        List<Artist> archived = artistController.handleViewArchivedArtists();
        printArtists(archived);
        ConsoleUtils.pressEnterToContinue(scanner);
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
