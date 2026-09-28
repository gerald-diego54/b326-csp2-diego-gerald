package com.joysistvi.recordingapp.services;

import com.joysistvi.recordingapp.models.User;
import com.joysistvi.recordingapp.repositories.UserRepository;
import com.joysistvi.recordingapp.services.interfaces.IUserService;
import com.joysistvi.recordingapp.utils.PasswordUtils;

import java.util.List;
import java.util.Optional;

public class UserService implements IUserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    } // getAllUsers

    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean registerUser(User user) { // createUser
        if (userRepository.findByUsername(user.username()).isPresent()) {
            return false;
        }

        String hashedPassword = PasswordUtils.hash(user.password());
        User toSave = new User(null, user.username(), hashedPassword, user.role());

        return userRepository.save(toSave);
    }

    @Override
    public Optional<User> authenticate(String username, String password) {

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty() || !PasswordUtils.verify(user.get().password(), password)) {
            return Optional.empty();
        }

        return user;
    }

    @Override
    public boolean updateUserById(User user) {
        String hashedPassword = PasswordUtils.hash(user.password());
        User toUpdate = new User(user.id(), user.username(), hashedPassword, user.role());
        return userRepository.update(toUpdate);
    } // updateUser

    @Override
    public boolean deleteUserById(int id) {
        return userRepository.deleteById(id);
    } // deleteUser
}
