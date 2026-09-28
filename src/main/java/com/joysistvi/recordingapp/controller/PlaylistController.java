package com.joysistvi.recordingapp.controller;

import com.joysistvi.recordingapp.models.Playlist;
import com.joysistvi.recordingapp.models.Song;
import com.joysistvi.recordingapp.services.PlaylistService;

import java.util.List;
import java.util.Optional;

public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    public List<Playlist> getPlaylistsByUser(Integer userId) { // handleViewPlaylistsByUser
        return playlistService.getPlaylistsByUser(userId);
    }

    public Optional<Playlist> getPlaylistById(int id) { // handleGetPlaylistById
        return playlistService.getPlaylistById(id);
    }

    public boolean createPlaylist(Integer userId) { // handleCreatePlaylist
        return playlistService.createPlaylist(userId);
    }

    public boolean deletePlaylist(int id) { // handleDeletePlaylist
        return playlistService.deletePlaylistById(id);
    }

    public boolean addSongToPlaylist(Integer playlistId, Integer songId) { // handleAddSongToPlaylist
        return playlistService.addSongToPlaylist(playlistId, songId);
    }

    public boolean removeSongFromPlaylist(Integer playlistId, Integer songId) { // handleRemoveSongFromPlaylist
        return playlistService.removeSongFromPlaylist(playlistId, songId);
    }

    public List<Song> getSongsInPlaylist(Integer playlistId) { // handleViewSongsInPlaylist
        return playlistService.getSongsInPlaylist(playlistId);
    }
}
