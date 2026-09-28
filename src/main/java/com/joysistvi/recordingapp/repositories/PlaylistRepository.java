package com.joysistvi.recordingapp.repositories;

import com.joysistvi.recordingapp.database.DatabaseConnection;
import com.joysistvi.recordingapp.models.Playlist;
import com.joysistvi.recordingapp.models.Song;
import com.joysistvi.recordingapp.repositories.interfaces.IRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlaylistRepository implements IRepository<Playlist, Integer> {

    private final DatabaseConnection databaseConnection = new DatabaseConnection();
    private static final Logger logger = LoggerFactory.getLogger(PlaylistRepository.class);
    private static final String ERROR_SQL_MESSAGE = "Failed to execute query";

    @Override
    public List<Playlist> findAll() {

        List<Playlist> playlists = new ArrayList<>();

        String sql = "SELECT * FROM playlists WHERE deleted_at IS NULL";

        try (

                Connection connection = databaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {
            while (resultSet.next()) {

                playlists.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return playlists;
    } // getAllPlaylists

    @Override
    public boolean save(Playlist playlist) {

        String sql = "INSERT INTO playlists (user_id) VALUES (?)";
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, playlist.userId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return false;
    } // createPlaylist

    @Override
    public Optional<Playlist> findById(Integer id) {

        String sql = "SELECT * FROM playlists WHERE id = ? AND deleted_at IS NULL";

        try (

                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    } // getPlaylistById

    @Override
    public boolean update(Playlist playlist) {

        String sql = "UPDATE playlists SET user_id = ? WHERE id = ? AND deleted_at IS NULL";

        try (

                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setInt(1, playlist.userId());
            preparedStatement.setInt(2, playlist.id());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    } // updatePlaylist

    @Override
    public boolean deleteById(Integer id) {

        String sql = "UPDATE playlists SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (

                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } // deletePlaylist

    public List<Playlist> findAllByUser(Integer userId) {

        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT * FROM playlists WHERE user_id = ? AND deleted_at IS NULL";

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                playlists.add(mapRow(resultSet));
            }

        } catch (SQLException e) {

            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return playlists;
    }

    public boolean addSong(Integer playlistId, Integer songId) {

        String sql = "INSERT INTO playlist_songs (playlist_id, song_id) VALUES (?, ?)";

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, playlistId);
            preparedStatement.setInt(2, songId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return false;
    }

    public boolean removeSong(Integer playlistId, Integer songId) {

        String sql = "UPDATE playlist_songs SET deleted_at = CURRENT_TIMESTAMP " +
                "WHERE playlist_id = ? AND song_id = ? AND deleted_at IS NULL LIMIT 1";

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, playlistId);
            preparedStatement.setInt(2, songId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return false;
    }

    public List<Song> getSongsForPlaylist(Integer playlistId) {

        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.* FROM songs s " +
                "JOIN playlist_songs ps ON s.id = ps.song_id " +
                "WHERE ps.playlist_id = ? AND ps.deleted_at IS NULL AND s.deleted_at IS NULL";

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, playlistId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                songs.add(new Song(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("length"),
                        resultSet.getString("genre"),
                        resultSet.getInt("album_id")
                ));
            }

        } catch (SQLException e) {

            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return songs;
    }

    private Playlist mapRow(ResultSet resultSet) throws SQLException {
        return new Playlist(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
