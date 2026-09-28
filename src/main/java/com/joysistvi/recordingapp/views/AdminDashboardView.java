package com.joysistvi.recordingapp.views;

import com.joysistvi.recordingapp.controller.AlbumController;
import com.joysistvi.recordingapp.controller.ArtistController;
import com.joysistvi.recordingapp.controller.SongController;
import com.joysistvi.recordingapp.models.User;
import com.joysistvi.recordingapp.repositories.AlbumRepository;
import com.joysistvi.recordingapp.repositories.ArtistRepository;
import com.joysistvi.recordingapp.repositories.SongRepository;
import com.joysistvi.recordingapp.services.AlbumService;
import com.joysistvi.recordingapp.services.ArtistService;
import com.joysistvi.recordingapp.services.SongService;
import com.joysistvi.recordingapp.utils.ConsoleUtils;
import com.joysistvi.recordingapp.views.dashboard.AlbumManagementView;
import com.joysistvi.recordingapp.views.dashboard.ArtistManagementView;
import com.joysistvi.recordingapp.views.dashboard.SongManagementView;
import com.joysistvi.recordingapp.views.enums.EAdminDashboardScreen;

import java.util.Scanner;

public class AdminDashboardView {

    private final Scanner scanner;

    private final ArtistManagementView artistManagementView;
    private final SongManagementView songManagementView;
    private final AlbumManagementView albumManagementView;

    private ArtistRepository artistRepository = new ArtistRepository();
    private ArtistService artistService = new ArtistService(artistRepository);
    private ArtistController artistController = new ArtistController(artistService);

    private SongRepository songRepository = new SongRepository();
    private SongService songService = new SongService(songRepository);
    private SongController songController = new SongController(songService);

    private AlbumRepository albumRepository = new AlbumRepository();
    private AlbumService albumService = new AlbumService(albumRepository);
    private AlbumController albumController = new AlbumController(albumService);

    private User currentUser;

    public AdminDashboardView(Scanner scanner) {
        this.scanner = scanner;
        this.artistManagementView = new ArtistManagementView(artistController, scanner);
        this.songManagementView = new SongManagementView(songController, albumController, scanner);
        this.albumManagementView = new AlbumManagementView(albumController, artistController, scanner);
    }

    public void start(User user) {
        this.currentUser = user;
        boolean inDashboard = true;

        while (inDashboard) {
            displayMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();
            EAdminDashboardScreen selected = select(choice);

            if (selected == null) {
                System.out.println("\n[!] Invalid option. Please enter a valid number (0-3).");
                ConsoleUtils.pressEnterToContinue(scanner);
                continue;
            }

            switch (selected) {
                case ARTIST_MANAGEMENT -> artistManagementView.start();
                case SONG_MANAGEMENT -> songManagementView.start();
                case ALBUM_MANAGEMENT -> albumManagementView.start();
                case LOGOUT -> {
                    System.out.println("\nLogging out...");
                    inDashboard = false;
                }
            }
        }
    }

    private void displayMenu() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("ADMIN DASHBOARD");
        System.out.println("Welcome, " + currentUser.username() + "!");
        System.out.println("1. Artist Management");
        System.out.println("2. Song Management");
        System.out.println("3. Album Management");
        System.out.println("0. Logout");
    }

    private EAdminDashboardScreen select(String key) {
        return switch (key) {
            case "1" -> EAdminDashboardScreen.ARTIST_MANAGEMENT;
            case "2" -> EAdminDashboardScreen.SONG_MANAGEMENT;
            case "3" -> EAdminDashboardScreen.ALBUM_MANAGEMENT;
            case "0" -> EAdminDashboardScreen.LOGOUT;
            default -> null;
        };
    }
}
