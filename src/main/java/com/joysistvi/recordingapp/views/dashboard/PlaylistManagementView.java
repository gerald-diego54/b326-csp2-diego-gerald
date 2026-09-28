package com.joysistvi.recordingapp.views.dashboard;

import com.joysistvi.recordingapp.controller.PlaylistController;
import com.joysistvi.recordingapp.controller.SongController;
import com.joysistvi.recordingapp.models.Playlist;
import com.joysistvi.recordingapp.models.Song;
import com.joysistvi.recordingapp.utils.ConsoleUtils;
import com.joysistvi.recordingapp.views.enums.EPlaylistManagementScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class PlaylistManagementView {

    private final Scanner scanner;
    private final PlaylistController playlistController;
    private final SongController songController;
    private static final Logger logger = LoggerFactory.getLogger(PlaylistManagementView.class);

    private Integer currentUserId;

    public PlaylistManagementView(PlaylistController playlistController, SongController songController, Scanner scanner) {
        this.scanner = scanner;
        this.playlistController = playlistController;
        this.songController = songController;
    }

    public void start(Integer userId) {
        this.currentUserId = userId;
        boolean inMenu = true;

        while (inMenu) {
            printMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            EPlaylistManagementScreen selected = playlistManagementScreen(choice);

            if (selected == null) {
                System.out.println("\n[!] Invalid option. Please enter a valid number (0-6).");
                ConsoleUtils.pressEnterToContinue(scanner);
                continue;
            }

            switch (selected) {
                case VIEW_ALL -> viewAllPlaylists();
                case CREATE -> createPlaylist();
                case DELETE -> deletePlaylist();
                case ADD_SONG -> addSongToPlaylist();
                case REMOVE_SONG -> removeSongFromPlaylist();
                case VIEW_SONGS -> viewSongsInPlaylist();
                case BACK -> {
                    System.out.println("\nReturning to main dashboard...");
                    inMenu = false;
                }
            }
        }
    }

    private void printMenu() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("PLAYLIST MANAGEMENT");
        System.out.println("1. View My Playlists");
        System.out.println("2. Create Playlist");
        System.out.println("3. Delete Playlist");
        System.out.println("4. Add Song to Playlist");
        System.out.println("5. Remove Song from Playlist");
        System.out.println("6. View Songs in Playlist");
        System.out.println("0. Back");
    }

    private EPlaylistManagementScreen playlistManagementScreen(String key) {
        return switch (key) {
            case "1" -> EPlaylistManagementScreen.VIEW_ALL;
            case "2" -> EPlaylistManagementScreen.CREATE;
            case "3" -> EPlaylistManagementScreen.DELETE;
            case "4" -> EPlaylistManagementScreen.ADD_SONG;
            case "5" -> EPlaylistManagementScreen.REMOVE_SONG;
            case "6" -> EPlaylistManagementScreen.VIEW_SONGS;
            case "0" -> EPlaylistManagementScreen.BACK;
            default -> null;
        };
    }

    private void viewAllPlaylists() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("MY PLAYLISTS");
        List<Playlist> playlists = playlistController.getPlaylistsByUser(currentUserId);
        printPlaylists(playlists);
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void createPlaylist() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("CREATE PLAYLIST");

        boolean isSuccess = playlistController.createPlaylist(currentUserId);
        if (isSuccess) {
            System.out.println("\n[✓] Playlist created successfully!");
            printPlaylists(playlistController.getPlaylistsByUser(currentUserId));
        } else {
            logger.error("Failed to create playlist for user ID: {}", currentUserId);
            System.out.println("[!] Failed to create playlist.");
        }
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void deletePlaylist() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("DELETE PLAYLIST");
        printPlaylists(playlistController.getPlaylistsByUser(currentUserId));
        int id = ConsoleUtils.readPositiveInt(scanner, "Enter playlist ID to delete: ");
        if (id == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        if (!isOwnedByCurrentUser(id)) return;

        boolean confirmed = ConsoleUtils.confirm(scanner, "Are you sure you want to delete playlist " + id + "? (y/N): ");

        if (confirmed) {
            boolean isSuccess = playlistController.deletePlaylist(id);
            if (isSuccess) {
                System.out.println("\n[✓] Playlist deleted!");
            } else {
                logger.error("Failed to delete playlist ID: {}", id);
                System.out.println("[!] Failed to delete playlist.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void addSongToPlaylist() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("ADD SONG TO PLAYLIST");
        printPlaylists(playlistController.getPlaylistsByUser(currentUserId));
        int playlistId = ConsoleUtils.readPositiveInt(scanner, "Enter playlist ID: ");
        if (playlistId == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        if (!isOwnedByCurrentUser(playlistId)) return;

        SongManagementView.printSongs(songController.getAllSongs());

        int songId = ConsoleUtils.readPositiveInt(scanner, "Enter song ID to add: ");
        if (songId == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        boolean isSuccess = playlistController.addSongToPlaylist(playlistId, songId);
        if (isSuccess) {
            System.out.println("\n[✓] Song added to playlist!");
        } else {
            logger.error("Failed to add song {} to playlist {}", songId, playlistId);
            System.out.println("[!] Failed to add song. Make sure the song ID exists.");
        }
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void removeSongFromPlaylist() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("REMOVE SONG FROM PLAYLIST");
        printPlaylists(playlistController.getPlaylistsByUser(currentUserId));
        int playlistId = ConsoleUtils.readPositiveInt(scanner, "Enter playlist ID: ");
        if (playlistId == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        if (!isOwnedByCurrentUser(playlistId)) return;

        SongManagementView.printSongs(playlistController.getSongsInPlaylist(playlistId));
        int songId = ConsoleUtils.readPositiveInt(scanner, "Enter song ID to remove: ");
        if (songId == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        boolean isSuccess = playlistController.removeSongFromPlaylist(playlistId, songId);
        if (isSuccess) {
            System.out.println("\n[✓] Song removed from playlist!");
        } else {
            logger.error("Failed to remove song {} from playlist {}", songId, playlistId);
            System.out.println("[!] Failed to remove song or it wasn't in the playlist.");
        }
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void viewSongsInPlaylist() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("SONGS IN PLAYLIST");
        printPlaylists(playlistController.getPlaylistsByUser(currentUserId));
        int playlistId = ConsoleUtils.readPositiveInt(scanner, "Enter playlist ID: ");
        if (playlistId == -1) {
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        if (!isOwnedByCurrentUser(playlistId)) return;

        List<Song> songs = playlistController.getSongsInPlaylist(playlistId);
        SongManagementView.printSongs(songs);
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private boolean isOwnedByCurrentUser(int playlistId) {
        Optional<Playlist> playlist = playlistController.getPlaylistById(playlistId);

        if (playlist.isEmpty() || !playlist.get().userId().equals(currentUserId)) {
            System.out.println("[!] Playlist not found.");
            ConsoleUtils.pressEnterToContinue(scanner);
            return false;
        }

        return true;
    }

    private static final DateTimeFormatter CREATED_AT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void printPlaylists(List<Playlist> playlists) {
        if (playlists == null || playlists.isEmpty()) {
            System.out.println("No playlists found.");
            return;
        }

        String border = "+" + "-".repeat(6) + "+" + "-".repeat(18) + "+";

        System.out.println(border);
        System.out.printf("| %-4s | %-16s |%n", "ID", "Created At");
        System.out.println(border);

        for (Playlist playlist : playlists) {
            String createdAt = playlist.createdAt() != null ? playlist.createdAt().format(CREATED_AT_FORMAT) : "";
            System.out.printf("| %-4s | %-16s |%n", playlist.id(), createdAt);
        }

        System.out.println(border);
    }
}
