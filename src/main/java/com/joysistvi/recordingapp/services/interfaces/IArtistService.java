package com.joysistvi.recordingapp.services.interfaces;

import com.joysistvi.recordingapp.models.Artist;

import java.util.List;
import java.util.Optional;

public interface IArtistService {

    List<Artist> getAllArtist();

    Optional<Artist> getArtistById(int id);

    boolean saveArtist(Artist artist);

    boolean updateArtistById(Artist artist);

    boolean deleteArtistById(int id);

    List<Artist> searchArtist(String key);

    boolean archiveArtist(Integer id);

    boolean restoreArtist(Integer id);

    List<Artist> getAllArchivedArtists();
}