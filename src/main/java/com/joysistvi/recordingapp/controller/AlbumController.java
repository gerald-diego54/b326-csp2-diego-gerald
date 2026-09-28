package com.joysistvi.recordingapp.controller;

import com.joysistvi.recordingapp.models.Album;
import com.joysistvi.recordingapp.services.AlbumService;

import java.util.List;
import java.util.Optional;

public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    public List<Album> getAllAlbums() { // handleViewAllAlbums
        return albumService.getAllAlbum();
    }

    public Optional<Album> getAlbumById(int id) { // handleGetAlbumById
        return albumService.getAlbumById(id);
    }

    public boolean addAlbum(Album album) { // handleCreateAlbum
        return albumService.saveAlbum(album);
    }

    public boolean updateAlbum(Album album) { // handleUpdateAlbum
        return albumService.updateAlbumById(album);
    }

    public boolean deleteAlbum(int id) { // handleDeleteAlbum
        return albumService.deleteAlbumById(id);
    }

    public List<Album> searchAlbum(String key) {
        return albumService.searchAlbum(key);
    }
}
