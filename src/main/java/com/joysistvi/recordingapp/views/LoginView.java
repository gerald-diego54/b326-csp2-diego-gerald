package com.joysistvi.recordingapp.views;

import com.joysistvi.recordingapp.controller.UserController;
import com.joysistvi.recordingapp.models.User;
import com.joysistvi.recordingapp.models.enums.ERole;
import com.joysistvi.recordingapp.utils.ConsoleUtils;
import com.joysistvi.recordingapp.utils.ValidationUtils;

import java.util.Optional;
import java.util.Scanner;

public class LoginView {

    private final UserController userController;
    private final Scanner scanner;
    private final AdminDashboardView adminDashboardView;
    private final UserDashboardView userDashboardView;

    public LoginView(
            UserController userController,
            Scanner scanner
    ) {
        this.userController = userController;
        this.scanner = scanner;
        this.adminDashboardView = new AdminDashboardView(scanner);
        this.userDashboardView = new UserDashboardView(scanner);
    }

    public void authLogin() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("USER LOGIN");

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (!ValidationUtils.isNotBlank(username) || !ValidationUtils.isNotBlank(password)) {
            System.out.println("[!] Username and password cannot be empty.");
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        Optional<User> user = userController.login(username, password);

        if (user.isEmpty()) {
            System.out.println("[!] Invalid username or password.");
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        System.out.println("\n[✓] Login successful. Welcome, " + user.get().username() + "!");

        if (user.get().role() == ERole.ADMIN) {
            adminDashboardView.start(user.get());
        } else {
            userDashboardView.start(user.get());
        }
    }

}
