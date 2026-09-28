package com.joysistvi.recordingapp.views;

import com.joysistvi.recordingapp.controller.AlbumController;
import com.joysistvi.recordingapp.controller.ArtistController;
import com.joysistvi.recordingapp.controller.PlaylistController;
import com.joysistvi.recordingapp.controller.SongController;
import com.joysistvi.recordingapp.models.User;
import com.joysistvi.recordingapp.repositories.AlbumRepository;
import com.joysistvi.recordingapp.repositories.ArtistRepository;
import com.joysistvi.recordingapp.repositories.PlaylistRepository;
import com.joysistvi.recordingapp.repositories.SongRepository;
import com.joysistvi.recordingapp.services.AlbumService;
import com.joysistvi.recordingapp.services.ArtistService;
import com.joysistvi.recordingapp.services.PlaylistService;
import com.joysistvi.recordingapp.services.SongService;
import com.joysistvi.recordingapp.utils.ConsoleUtils;
import com.joysistvi.recordingapp.views.dashboard.AlbumManagementView;
import com.joysistvi.recordingapp.views.dashboard.ArtistManagementView;
import com.joysistvi.recordingapp.views.dashboard.PlaylistManagementView;
import com.joysistvi.recordingapp.views.dashboard.SongManagementView;
import com.joysistvi.recordingapp.views.enums.EUserDashboardScreen;

import java.util.Scanner;

public class UserDashboardView {

    private final Scanner scanner;
    private final PlaylistManagementView playlistManagementView;

    private ArtistRepository artistRepository = new ArtistRepository();
    private ArtistService artistService = new ArtistService(artistRepository);
    private ArtistController artistController = new ArtistController(artistService);

    private AlbumRepository albumRepository = new AlbumRepository();
    private AlbumService albumService = new AlbumService(albumRepository);
    private AlbumController albumController = new AlbumController(albumService);

    private SongRepository songRepository = new SongRepository();
    private SongService songService = new SongService(songRepository);
    private SongController songController = new SongController(songService);

    private PlaylistRepository playlistRepository = new PlaylistRepository();
    private PlaylistService playlistService = new PlaylistService(playlistRepository);
    private PlaylistController playlistController = new PlaylistController(playlistService);

    private User currentUser;

    public UserDashboardView(Scanner scanner) {
        this.scanner = scanner;
        this.playlistManagementView = new PlaylistManagementView(playlistController, songController, scanner);
    }

    public void start(User user) {
        this.currentUser = user;
        boolean inDashboard = true;

        while (inDashboard) {
            displayMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();
            EUserDashboardScreen selected = select(choice);

            if (selected == null) {
                System.out.println("\n[!] Invalid option. Please enter a valid number (0-4).");
                ConsoleUtils.pressEnterToContinue(scanner);
                continue;
            }

            switch (selected) {
                case BROWSE_ARTISTS -> browseArtists();
                case BROWSE_ALBUMS -> browseAlbums();
                case BROWSE_SONGS -> browseSongs();
                case PLAYLIST_MANAGEMENT -> playlistManagementView.start(currentUser.id());
                case LOGOUT -> {
                    System.out.println("\nLogging out...");
                    inDashboard = false;
                }
            }
        }
    }

    private void displayMenu() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("USER DASHBOARD");
        System.out.println("Welcome, " + currentUser.username() + "!");
        System.out.println("1. Browse Artists");
        System.out.println("2. Browse Albums");
        System.out.println("3. Browse Songs");
        System.out.println("4. My Playlists");
        System.out.println("0. Logout");
    }

    private EUserDashboardScreen select(String key) {
        return switch (key) {
            case "1" -> EUserDashboardScreen.BROWSE_ARTISTS;
            case "2" -> EUserDashboardScreen.BROWSE_ALBUMS;
            case "3" -> EUserDashboardScreen.BROWSE_SONGS;
            case "4" -> EUserDashboardScreen.PLAYLIST_MANAGEMENT;
            case "0" -> EUserDashboardScreen.LOGOUT;
            default -> null;
        };
    }

    private void browseArtists() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("BROWSE ARTISTS");
        ArtistManagementView.printArtists(artistController.getAllArtists());
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void browseAlbums() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("BROWSE ALBUMS");
        AlbumManagementView.printAlbums(albumController.getAllAlbums());
        ConsoleUtils.pressEnterToContinue(scanner);
    }

    private void browseSongs() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("BROWSE SONGS");
        SongManagementView.printSongs(songController.getAllSongs());
        ConsoleUtils.pressEnterToContinue(scanner);
    }
}
