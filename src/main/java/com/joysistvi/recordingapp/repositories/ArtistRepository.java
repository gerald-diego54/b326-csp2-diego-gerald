package com.joysistvi.recordingapp.repositories;

import com.joysistvi.recordingapp.database.DatabaseConnection;
import com.joysistvi.recordingapp.models.Artist;
import com.joysistvi.recordingapp.repositories.interfaces.IRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArtistRepository implements IRepository<Artist, Integer> {

    private final DatabaseConnection databaseConnection = new DatabaseConnection();
    private static final Logger logger = LoggerFactory.getLogger(ArtistRepository.class);
    private static final String ERROR_SQL_MESSAGE = "Failed to execute query";

    @Override
    public List<Artist> findAll() {

        List<Artist> artists = new ArrayList<>();

        String sql = "SELECT * FROM artists WHERE deleted_at IS NULL";

        try (

                Connection connection = databaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {
            while (resultSet.next()) {

                artists.add(new Artist(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException e) {
            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return artists;
    } // getAllArtists

    @Override
    public boolean save(Artist artist) {

        String sql = "INSERT INTO artists (name) VALUES (?)";
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, artist.name());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return false;
    } // createArtist

    @Override
    public Optional<Artist> findById(Integer id) {

        String sql = "SELECT * FROM artists WHERE id = ? AND deleted_at IS NULL";

        try (

                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    return Optional.of(new Artist(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    } // getArtistById

    @Override
    public boolean update(Artist artist) {

        String sql = "UPDATE artists SET name = ? WHERE id = ? AND deleted_at IS NULL";

        try (

                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setString(1, artist.name());
            preparedStatement.setInt(2, artist._id());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    } // updateArtist

    @Override
    public boolean deleteById(Integer id) {

        String sql = "UPDATE artists SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (

                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } // deletedArtist

    public List<Artist> searchArtist(String key) {

        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM artists WHERE name LIKE ? AND is_archive = 0";

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setString(1, key + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                artists.add(new Artist(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }

        } catch (SQLException e) {

            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return artists;

    }

    public boolean archiveArtist(Integer id) {

        String sql = "UPDATE artists SET is_archive = 1 WHERE id = ?";

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return false;
    }

    public boolean restoreArtist(Integer id) {

        String sql = "UPDATE artists SET is_archive = 0 WHERE id = ?";

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return false;
    }

    public List<Artist> getAllArchivedArtists() {

        String sql = "SELECT * FROM artists WHERE is_archive = 1";

        List<Artist> artists = new ArrayList<>();

        try(
                Connection connection = databaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {

            while (resultSet.next()){

                artists.add(new Artist(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }

        } catch (SQLException e) {
            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return artists;
    }


}