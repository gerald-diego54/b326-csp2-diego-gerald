package com.joysistvi.recordingapp.controller;

import com.joysistvi.recordingapp.models.User;
import com.joysistvi.recordingapp.services.UserService;

import java.util.List;
import java.util.Optional;

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public List<User> getAllUsers() { // handleViewAllUsers
        return userService.getAllUser();
    }

    public Optional<User> getUserById(int id) { // handleGetUserById
        return userService.getUserById(id);
    }

    public boolean register(User user) { // handleRegisterUser
        return userService.registerUser(user);
    }

    public Optional<User> login(String username, String password) { // handleLoginUser
        return userService.authenticate(username, password);
    }

    public boolean updateUser(User user) { // handleUpdateUser
        return userService.updateUserById(user);
    }

    public boolean deleteUser(int id) { // handleDeleteUser
        return userService.deleteUserById(id);
    }
}
