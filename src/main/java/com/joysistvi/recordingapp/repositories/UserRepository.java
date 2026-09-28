package com.joysistvi.recordingapp.repositories;

import com.joysistvi.recordingapp.database.DatabaseConnection;
import com.joysistvi.recordingapp.models.User;
import com.joysistvi.recordingapp.models.enums.ERole;
import com.joysistvi.recordingapp.repositories.interfaces.IRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IRepository<User, Integer> {

    private final DatabaseConnection databaseConnection = new DatabaseConnection();
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private static final String ERROR_SQL_MESSAGE = "Failed to execute query";

    @Override
    public List<User> findAll() {

        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users WHERE deleted_at IS NULL";

        try (

                Connection connection = databaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {
            while (resultSet.next()) {

                users.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return users;
    } // getAllUsers

    @Override
    public boolean save(User user) {

        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, user.username());
            preparedStatement.setString(2, user.password());
            preparedStatement.setString(3, user.role().name().toLowerCase());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            logger.error("Username already taken: {}", user.username());
        } catch (SQLException e) {
            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return false;
    } // createUser

    @Override
    public Optional<User> findById(Integer id) {

        String sql = "SELECT * FROM users WHERE id = ? AND deleted_at IS NULL";

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
    } // getUserById

    @Override
    public boolean update(User user) {

        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ? AND deleted_at IS NULL";

        try (

                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setString(1, user.username());
            preparedStatement.setString(2, user.password());
            preparedStatement.setString(3, user.role().name().toLowerCase());
            preparedStatement.setInt(4, user.id());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    } // updateUser

    @Override
    public boolean deleteById(Integer id) {

        String sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (

                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } // deleteUser

    public Optional<User> findByUsername(String username) {

        String sql = "SELECT * FROM users WHERE username = ? AND deleted_at IS NULL";

        try (

                Connection connection = databaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.error(ERROR_SQL_MESSAGE, e);
        }

        return Optional.empty();
    }

    private User mapRow(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                ERole.valueOf(resultSet.getString("role").toUpperCase())
        );
    }
}
