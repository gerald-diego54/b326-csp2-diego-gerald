package com.joysistvi.recordingapp.views;

import com.joysistvi.recordingapp.controller.ArtistController;
import com.joysistvi.recordingapp.controller.UserController;
import com.joysistvi.recordingapp.utils.ConsoleUtils;

import java.io.Console;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class LoginView {

    private final UserController userController;

    private final Scanner scanner;
    private final Console console = System.console();

    private final DashboardView dashboardView;

    public LoginView(
            UserController userController,
            Scanner scanner
    ) {
        this.userController = userController;
        this.scanner = scanner;
        this.dashboardView = new DashboardView(scanner);
    }

    public void authLogin() {
        ConsoleUtils.printHeader("USER LOGIN");

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        dashboardView.start();

    }

}