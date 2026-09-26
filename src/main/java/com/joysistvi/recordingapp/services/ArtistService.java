package com.joysistvi.recordingapp.services;

import com.joysistvi.recordingapp.models.Artist;
import com.joysistvi.recordingapp.repositories.ArtistRepository;
import com.joysistvi.recordingapp.services.interfaces.IArtistService;

import java.util.List;
import java.util.Optional;

public class ArtistService implements IArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Artist> getAllArtist() {
        return artistRepository.findAll();
    } // getAllArtists

    @Override
    public Optional<Artist> getArtistById(int id) { // getArtistById
        return artistRepository.findById(id);
    }

    @Override
    public boolean saveArtist(Artist artist) { // createArtist
        return artistRepository.save(artist);
    }

    @Override
    public boolean updateArtistById(Artist artist) {
        return artistRepository.update(artist);
    } // updateArtist

    @Override
    public boolean deleteArtistById(int id) {
        return artistRepository.deleteById(id);
    } // deleteArtist

    public List<Artist> searchArtist(String key){
        return artistRepository.searchArtist(key);
    }

    public boolean archiveArtist(Integer id){
        return artistRepository.archiveArtist(id);
    }

    public boolean restoreArtist(Integer id){
        return artistRepository.restoreArtist(id);
    }

    public List<Artist> getAllArchivedArtists(){
        return artistRepository.getAllArchivedArtists();
    }
}
