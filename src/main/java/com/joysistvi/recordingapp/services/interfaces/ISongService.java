package com.joysistvi.recordingapp.services.interfaces;

import com.joysistvi.recordingapp.models.Song;

import java.util.List;
import java.util.Optional;

public interface ISongService {

    List<Song> getAllSong();

    Optional<Song> getSongById(int id);

    boolean saveSong(Song song);

    boolean updateSongById(Song song);

    boolean deleteSongById(int id);

    List<Song> searchSong(String key);
}
