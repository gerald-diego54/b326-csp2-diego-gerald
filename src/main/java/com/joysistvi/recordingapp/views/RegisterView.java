package com.joysistvi.recordingapp.views;

import com.joysistvi.recordingapp.controller.UserController;
import com.joysistvi.recordingapp.utils.ConsoleUtils;

import java.io.Console;
import java.util.Scanner;

public class RegisterView {

    private final UserController userController;
    private final Scanner scanner;
    private final Console console = System.console();

    public RegisterView(UserController userController, Scanner scanner) {
        this.userController = userController;
        this.scanner = scanner;
    }

    public void authRegister() {
        ConsoleUtils.printHeader("USER REGISTRATION");

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

    }

}