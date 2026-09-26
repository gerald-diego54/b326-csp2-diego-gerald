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

    public List<Artist> getAllArtists() { // handleViewAllArtists
        return artistService.getAllArtist();
    }

    public Optional<Artist> getArtistById(int id) { // handleGetArtistById
        return artistService.getArtistById(id);
    }

    public boolean addArtist(Artist artist) { // handleCreateArtist
        return artistService.saveArtist(artist);
    }

    public boolean updateArtist(Artist artist) { // handleUpdateArtist
        return artistService.updateArtistById(artist);
    }

    public boolean deleteArtist(int id) { // handleDeleteArtist
        return artistService.deleteArtistById(id);
    }

    public List<Artist> searchArtist(String key){
        return artistService.searchArtist(key);
    }

    public List<Artist> handleViewArchivedArtists(){
        return artistService.getAllArchivedArtists();
    }

    public boolean handleArchiveArtist(Integer id){
        return artistService.archiveArtist(id);
    }

    public boolean handleRestoreArtist(Integer id) {
        return artistService.restoreArtist(id);
    }



}
