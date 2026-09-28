package com.joysistvi.recordingapp.services.interfaces;

import com.joysistvi.recordingapp.models.Album;

import java.util.List;
import java.util.Optional;

public interface IAlbumService {

    List<Album> getAllAlbum();

    Optional<Album> getAlbumById(int id);

    boolean saveAlbum(Album album);

    boolean updateAlbumById(Album album);

    boolean deleteAlbumById(int id);

    List<Album> searchAlbum(String key);
}
