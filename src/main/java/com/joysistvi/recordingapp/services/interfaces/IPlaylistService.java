package com.joysistvi.recordingapp.services.interfaces;

import com.joysistvi.recordingapp.models.Playlist;
import com.joysistvi.recordingapp.models.Song;

import java.util.List;
import java.util.Optional;

public interface IPlaylistService {

    List<Playlist> getPlaylistsByUser(Integer userId);

    Optional<Playlist> getPlaylistById(int id);

    boolean createPlaylist(Integer userId);

    boolean deletePlaylistById(int id);

    boolean addSongToPlaylist(Integer playlistId, Integer songId);

    boolean removeSongFromPlaylist(Integer playlistId, Integer songId);

    List<Song> getSongsInPlaylist(Integer playlistId);
}
