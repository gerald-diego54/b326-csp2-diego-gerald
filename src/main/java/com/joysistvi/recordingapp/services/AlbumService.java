package com.joysistvi.recordingapp.services;

import com.joysistvi.recordingapp.models.Album;
import com.joysistvi.recordingapp.repositories.AlbumRepository;
import com.joysistvi.recordingapp.services.interfaces.IAlbumService;

import java.util.List;
import java.util.Optional;

public class AlbumService implements IAlbumService {

    private final AlbumRepository albumRepository;

    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Override
    public List<Album> getAllAlbum() {
        return albumRepository.findAll();
    } // getAllAlbums

    @Override
    public Optional<Album> getAlbumById(int id) {
        return albumRepository.findById(id);
    }

    @Override
    public boolean saveAlbum(Album album) { // createAlbum
        return albumRepository.save(album);
    }

    @Override
    public boolean updateAlbumById(Album album) {
        return albumRepository.update(album);
    } // updateAlbum

    @Override
    public boolean deleteAlbumById(int id) {
        return albumRepository.deleteById(id);
    } // deleteAlbum

    @Override
    public List<Album> searchAlbum(String key) {
        return albumRepository.searchAlbum(key);
    }
}
