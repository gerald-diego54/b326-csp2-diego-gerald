package com.joysistvi.recordingapp.database;

import com.joysistvi.recordingapp.models.Album;
import com.joysistvi.recordingapp.models.Artist;
import com.joysistvi.recordingapp.models.Playlist;
import com.joysistvi.recordingapp.models.Song;
import com.joysistvi.recordingapp.models.User;
import com.joysistvi.recordingapp.models.enums.ERole;
import com.joysistvi.recordingapp.repositories.AlbumRepository;
import com.joysistvi.recordingapp.repositories.ArtistRepository;
import com.joysistvi.recordingapp.repositories.PlaylistRepository;
import com.joysistvi.recordingapp.repositories.SongRepository;
import com.joysistvi.recordingapp.repositories.UserRepository;
import com.joysistvi.recordingapp.services.AlbumService;
import com.joysistvi.recordingapp.services.ArtistService;
import com.joysistvi.recordingapp.services.PlaylistService;
import com.joysistvi.recordingapp.services.SongService;
import com.joysistvi.recordingapp.services.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DatabaseSeeder {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    private static final String ADMIN_USERNAME = "studio_admin";
    private static final String ADMIN_PASSWORD = "AdminPass123";
    private static final String SEED_PASSWORD = "SeedPass123";

    private static final String[] USERNAMES = {
            "olivia_reyes", "marcus_bell", "sophia_tran", "liam_okafor", "isabella_cruz",
            "ethan_walsh", "amara_singh", "noah_fischer", "layla_moreno", "jaxon_hart"
    };

    private static final String[] ARTIST_NAMES = {
            "Aurora Wells", "Neon Drift", "Crimson Echo", "Velvet Static", "Midnight Harbor",
            "Solar Fable", "Glass Horizon", "Paper Wolves", "Electric Bloom", "Silver Static"
    };

    private static final String[] ALBUM_NAMES = {
            "Dawn Signals", "Static Bloom", "Hollow Lights", "Paper Moon", "Electric Fade",
            "Glass Tides", "Neon Echoes", "Velvet Skies", "Silver Rain", "Aurora Fields"
    };

    private static final String[] SONG_TITLES = {
            "Waves of Light", "Broken Static", "Falling Skies", "Glass Horizon", "Midnight Drive",
            "Electric Pulse", "Hollow Moon", "Silver Lining", "Fading Echo", "Aurora Rise"
    };

    private static final String[] GENRES = {
            "Pop", "Rock", "Electronic", "Jazz", "Indie",
            "Hip Hop", "Classical", "R&B", "Folk", "Ambient"
    };

    private final UserRepository userRepository = new UserRepository();
    private final UserService userService = new UserService(userRepository);

    private final ArtistRepository artistRepository = new ArtistRepository();
    private final ArtistService artistService = new ArtistService(artistRepository);

    private final AlbumRepository albumRepository = new AlbumRepository();
    private final AlbumService albumService = new AlbumService(albumRepository);

    private final SongRepository songRepository = new SongRepository();
    private final SongService songService = new SongService(songRepository);

    private final PlaylistRepository playlistRepository = new PlaylistRepository();
    private final PlaylistService playlistService = new PlaylistService(playlistRepository);

    public void seed() {
        ensureAdminAccountExists();
        seedSampleCatalogIfEmpty();
    }

    private void ensureAdminAccountExists() {

        boolean hasAdmin = userRepository.findAll().stream()
                .anyMatch(user -> user.role() == ERole.ADMIN);

        if (hasAdmin) {
            return;
        }

        boolean created = userService.registerUser(new User(null, ADMIN_USERNAME, ADMIN_PASSWORD, ERole.ADMIN));

        if (created) {
            logger.info("Seeded default admin account (username: {}).", ADMIN_USERNAME);
        }
    }

    private void seedSampleCatalogIfEmpty() {

        if (!artistRepository.findAll().isEmpty()) {
            logger.info("Sample catalog data already exists. Skipping catalog seed.");
            return;
        }

        logger.info("Seeding sample catalog data...");

        for (String username : USERNAMES) {
            userService.registerUser(new User(null, username, SEED_PASSWORD, ERole.USER));
        }
        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.role() == ERole.USER)
                .toList();

        for (String name : ARTIST_NAMES) {
            artistService.saveArtist(new Artist(null, name));
        }
        List<Artist> artists = artistService.getAllArtist();

        for (int i = 0; i < ALBUM_NAMES.length; i++) {
            Integer artistId = artists.get(i % artists.size())._id();
            albumService.saveAlbum(new Album(null, ALBUM_NAMES[i], 2014 + i, artistId));
        }
        List<Album> albums = albumService.getAllAlbum();

        for (int i = 0; i < SONG_TITLES.length; i++) {
            Integer albumId = albums.get(i % albums.size()).id();
            String length = (2 + i % 4) + ":" + String.format("%02d", (10 + i * 7) % 60);
            songService.saveSong(new Song(null, SONG_TITLES[i], length, GENRES[i], albumId));
        }
        List<Song> songs = songService.getAllSong();

        for (User user : users) {
            playlistService.createPlaylist(user.id());
        }
        List<Playlist> playlists = playlistRepository.findAll();

        for (int i = 0; i < playlists.size(); i++) {
            Integer playlistId = playlists.get(i).id();
            Integer songId = songs.get(i % songs.size()).id();
            playlistService.addSongToPlaylist(playlistId, songId);
        }

        logger.info("Seeding complete: {} users, {} artists, {} albums, {} songs, {} playlists.",
                users.size(), artists.size(), albums.size(), songs.size(), playlists.size());
    }
}
