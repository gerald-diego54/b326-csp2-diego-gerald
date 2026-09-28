package com.joysistvi.recordingapp.services.interfaces;

import com.joysistvi.recordingapp.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getAllUser();

    Optional<User> getUserById(int id);

    boolean registerUser(User user);

    Optional<User> authenticate(String username, String password);

    boolean updateUserById(User user);

    boolean deleteUserById(int id);
}
