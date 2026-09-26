package com.joysistvi.recordingapp.services;

import com.joysistvi.recordingapp.models.Song;
import com.joysistvi.recordingapp.repositories.SongRepository;
import com.joysistvi.recordingapp.services.interfaces.ISongService;

import java.util.List;
import java.util.Optional;

public class SongService implements ISongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public List<Song> getAllSong() {
        return songRepository.findAll();
    } // getAllSongs

    @Override
    public Optional<Song> getSongById(int id) { // getSongById
        return songRepository.findById(id);
    }

    @Override
    public boolean saveSong(Song song) { // createSong
        return songRepository.save(song);
    }

    @Override
    public boolean updateSongById(Song song) {
        return songRepository.update(song);
    } // updateSong

    @Override
    public boolean deleteSongById(int id) {
        return songRepository.deleteById(id);
    } // deleteSong

    public List<Song> searchSong(String key) {
        return songRepository.searchSong(key);
    }
}
