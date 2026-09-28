package com.joysistvi.recordingapp.services;

import com.joysistvi.recordingapp.models.Playlist;
import com.joysistvi.recordingapp.models.Song;
import com.joysistvi.recordingapp.repositories.PlaylistRepository;
import com.joysistvi.recordingapp.services.interfaces.IPlaylistService;

import java.util.List;
import java.util.Optional;

public class PlaylistService implements IPlaylistService {

    private final PlaylistRepository playlistRepository;

    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    @Override
    public List<Playlist> getPlaylistsByUser(Integer userId) {
        return playlistRepository.findAllByUser(userId);
    }

    @Override
    public Optional<Playlist> getPlaylistById(int id) {
        return playlistRepository.findById(id);
    }

    @Override
    public boolean createPlaylist(Integer userId) { // createPlaylist
        return playlistRepository.save(new Playlist(null, userId, null));
    }

    @Override
    public boolean deletePlaylistById(int id) {
        return playlistRepository.deleteById(id);
    } // deletePlaylist

    @Override
    public boolean addSongToPlaylist(Integer playlistId, Integer songId) {
        return playlistRepository.addSong(playlistId, songId);
    }

    @Override
    public boolean removeSongFromPlaylist(Integer playlistId, Integer songId) {
        return playlistRepository.removeSong(playlistId, songId);
    }

    @Override
    public List<Song> getSongsInPlaylist(Integer playlistId) {
        return playlistRepository.getSongsForPlaylist(playlistId);
    }
}
