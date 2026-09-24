package com.joysistvi.recordingapp.services;

import com.joysistvi.recordingapp.models.Artist;
import com.joysistvi.recordingapp.repositories.ArtistIRepository;
import com.joysistvi.recordingapp.services.interfaces.IArtistService;

import java.util.List;
import java.util.Optional;

public class ArtistService implements IArtistService {

    private final ArtistIRepository artistRepository;

    public ArtistService(ArtistIRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Artist> getAllArtist() {
        return artistRepository.findAll();
    }

    @Override
    public Optional<Artist> getArtistById(int id) {
        return artistRepository.findById(id);
    }

    @Override
    public boolean saveArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    @Override
    public boolean updateArtistById(Artist artist) {
        return artistRepository.update(artist);
    }

    @Override
    public boolean deleteArtistById(int id) {
        return artistRepository.deleteById(id);
    }
}
