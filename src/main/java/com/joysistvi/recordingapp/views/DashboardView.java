package com.joysistvi.recordingapp.views;

import com.joysistvi.recordingapp.controller.ArtistController;
import com.joysistvi.recordingapp.controller.SongController;
import com.joysistvi.recordingapp.repositories.ArtistRepository;
import com.joysistvi.recordingapp.repositories.SongRepository;
import com.joysistvi.recordingapp.services.ArtistService;
import com.joysistvi.recordingapp.services.SongService;
import com.joysistvi.recordingapp.utils.ConsoleUtils;
import com.joysistvi.recordingapp.views.dashboard.ArtistManagementView;
import com.joysistvi.recordingapp.views.dashboard.SongManagementView;
import com.joysistvi.recordingapp.views.enums.EDashboardScreen;

import java.util.Scanner;

public class DashboardView {

    private final Scanner scanner;

    private final ArtistManagementView artistManagementView;
    private final SongManagementView songManagementView;

    private ArtistRepository artistRepository = new ArtistRepository();
    private ArtistService artistService = new ArtistService(artistRepository);
    private ArtistController artistController = new ArtistController(artistService);

    private SongRepository songRepository = new SongRepository();
    private SongService songService = new SongService(songRepository);
    private SongController songController = new SongController(songService);

    public DashboardView(Scanner scanner) {
        this.scanner = scanner;
        this.artistManagementView = new ArtistManagementView(artistController, scanner);
        this.songManagementView = new SongManagementView(songController, scanner);
    }

    public void start(){

        displayMenu();

        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();
        EDashboardScreen selected = select(choice);

        switch (selected) {

            case ARTIST_MANAGEMENT -> artistManagementView.start();
            case SONG_MANAGEMENT -> songManagementView.start();
        }

    }

    private void displayMenu() {
        ConsoleUtils.printHeader("DASHBOARD");
        System.out.println("1. Artist Management");
        System.out.println("2. Song Management");
    }


    private EDashboardScreen select(String key){

        switch (key){

            case "1" -> {
                return EDashboardScreen.ARTIST_MANAGEMENT;
            }
            case "2" -> {
                return EDashboardScreen.SONG_MANAGEMENT;
            }
        }

        return EDashboardScreen.ARTIST_MANAGEMENT;
    }
}
