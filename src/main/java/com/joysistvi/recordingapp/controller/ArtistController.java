package com.joysistvi.recordingapp.controller;

import com.joysistvi.recordingapp.models.Artist;
import com.joysistvi.recordingapp.services.ArtistService;

import java.util.List;
import java.util.Optional;

public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    public List<Artist> getAllArtists() {
        return artistService.getAllArtist();
    }

    public Optional<Artist> getArtistById(int id) {
        return artistService.getArtistById(id);
    }

    public boolean addArtist(Artist artist) {
        return artistService.saveArtist(artist);
    }

    public void updateArtist(Artist artist) {
        artistService.updateArtistById(artist);
    }

    public boolean deleteArtist(int id) {
        return artistService.deleteArtistById(id);
    }

}
