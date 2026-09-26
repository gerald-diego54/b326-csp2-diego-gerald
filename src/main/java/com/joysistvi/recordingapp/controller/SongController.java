package com.joysistvi.recordingapp.controller;

import com.joysistvi.recordingapp.models.Song;
import com.joysistvi.recordingapp.services.SongService;

import java.util.List;
import java.util.Optional;

public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    public List<Song> getAllSongs() { // handleViewAllSongs
        return songService.getAllSong();
    }

    public Optional<Song> getSongById(int id) { // handleGetSongById
        return songService.getSongById(id);
    }

    public boolean addSong(Song song) { // handleCreateSong
        return songService.saveSong(song);
    }

    public boolean updateSong(Song song) { // handleUpdateSong
        return songService.updateSongById(song);
    }

    public boolean deleteSong(int id) { // handleDeleteSong
        return songService.deleteSongById(id);
    }

    public List<Song> searchSong(String key) {
        return songService.searchSong(key);
    }
}
