package com.joysistvi.recordingapp.repositories;

import com.joysistvi.recordingapp.database.DatabaseConnection;
import com.joysistvi.recordingapp.models.Artist;
import com.joysistvi.recordingapp.repositories.interfaces.IRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArtistIRepository implements IRepository<Artist, Integer> {

    private final DatabaseConnection databaseConnection = new DatabaseConnection();

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
            throw new RuntimeException(e);
        }

        return artists;
    }

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
            throw new RuntimeException(e);
        }
    }

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
    }

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
    }

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
    }
}