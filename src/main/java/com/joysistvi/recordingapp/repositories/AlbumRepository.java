package com.joysistvi.recordingapp.repositories;

import com.joysistvi.recordingapp.database.DatabaseConnection;
import com.joysistvi.recordingapp.models.Album;
import com.joysistvi.recordingapp.repositories.interfaces.IRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlbumRepository implements IRepository<Album, Integer> {

    private final DatabaseConnection databaseConnection = new DatabaseConnection();
    private static final Logger logger = LoggerFactory.getLogger(AlbumRepository.class);
    private static final String ERROR_SQL_MESSAGE = "Failed to execute query";

    @Override
    public List<Album> findAll() {

        List<Album> albums = new ArrayList<>();

        String sql = "SELECT * FROM albums WHERE deleted_at IS NULL";

        try (

                Connection connection = databaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {
            while (resultSet.next()) {

                albums.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return albums;
    } // getAllAlbums

    @Override
    public boolean save(Album album) {

        String sql = "INSERT INTO albums (name, year, artist_id) VALUES (?, ?, ?)";
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, album.name());
            setYear(preparedStatement, 2, album.year());
            preparedStatement.setInt(3, album.artistId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return false;
    } // createAlbum

    @Override
    public Optional<Album> findById(Integer id) {

        String sql = "SELECT * FROM albums WHERE id = ? AND deleted_at IS NULL";

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
    } // getAlbumById

    @Override
    public boolean update(Album album) {

        String sql = "UPDATE albums SET name = ?, year = ?, artist_id = ? WHERE id = ? AND deleted_at IS NULL";

        try (

                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setString(1, album.name());
            setYear(preparedStatement, 2, album.year());
            preparedStatement.setInt(3, album.artistId());
            preparedStatement.setInt(4, album.id());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    } // updateAlbum

    @Override
    public boolean deleteById(Integer id) {

        String sql = "UPDATE albums SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (

                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } // deleteAlbum

    public List<Album> searchAlbum(String key) {

        List<Album> albums = new ArrayList<>();
        String sql = "SELECT * FROM albums WHERE name LIKE ? AND deleted_at IS NULL";

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setString(1, key + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                albums.add(mapRow(resultSet));
            }

        } catch (SQLException e) {

            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return albums;

    }

    private void setYear(PreparedStatement preparedStatement, int index, Integer year) throws SQLException {
        if (year == null) {
            preparedStatement.setNull(index, Types.DATE);
        } else {
            preparedStatement.setDate(index, Date.valueOf(year + "-01-01"));
        }
    }

    private Album mapRow(ResultSet resultSet) throws SQLException {

        Date year = resultSet.getDate("year");

        return new Album(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                year != null ? year.toLocalDate().getYear() : null,
                resultSet.getInt("artist_id")
        );
    }
}
