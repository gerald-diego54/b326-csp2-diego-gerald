package com.joysistvi.recordingapp.repositories;

import com.joysistvi.recordingapp.database.DatabaseConnection;
import com.joysistvi.recordingapp.models.Song;
import com.joysistvi.recordingapp.repositories.interfaces.IRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SongRepository implements IRepository<Song, Integer> {

    private final DatabaseConnection databaseConnection = new DatabaseConnection();
    private static final Logger logger = LoggerFactory.getLogger(SongRepository.class);
    private static final String ERROR_SQL_MESSAGE = "Failed to execute query";

    @Override
    public List<Song> findAll() {

        List<Song> songs = new ArrayList<>();

        String sql = "SELECT * FROM songs WHERE deleted_at IS NULL";

        try (

                Connection connection = databaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {
            while (resultSet.next()) {

                songs.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return songs;
    } // getAllSongs

    @Override
    public boolean save(Song song) {

        String sql = "INSERT INTO songs (title, length, genre, album_id) VALUES (?, ?, ?, ?)";
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, song.title());
            preparedStatement.setString(2, song.length());
            preparedStatement.setString(3, song.genre());
            preparedStatement.setInt(4, song.albumId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return false;
    } // createSong

    @Override
    public Optional<Song> findById(Integer id) {

        String sql = "SELECT * FROM songs WHERE id = ? AND deleted_at IS NULL";

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
    } // getSongById

    @Override
    public boolean update(Song song) {

        String sql = "UPDATE songs SET title = ?, length = ?, genre = ?, album_id = ? WHERE id = ? AND deleted_at IS NULL";

        try (

                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setString(1, song.title());
            preparedStatement.setString(2, song.length());
            preparedStatement.setString(3, song.genre());
            preparedStatement.setInt(4, song.albumId());
            preparedStatement.setInt(5, song.id());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    } // updateSong

    @Override
    public boolean deleteById(Integer id) {

        String sql = "UPDATE songs SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (

                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } // deleteSong

    public List<Song> searchSong(String key) {

        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM songs WHERE title LIKE ? AND deleted_at IS NULL";

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setString(1, key + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                songs.add(mapRow(resultSet));
            }

        } catch (SQLException e) {

            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return songs;

    }

    private Song mapRow(ResultSet resultSet) throws SQLException {
        return new Song(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("length"),
                resultSet.getString("genre"),
                resultSet.getInt("album_id")
        );
    }
}
