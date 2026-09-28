package com.joysistvi.recordingapp.views;

import com.joysistvi.recordingapp.controller.UserController;
import com.joysistvi.recordingapp.models.User;
import com.joysistvi.recordingapp.models.enums.ERole;
import com.joysistvi.recordingapp.utils.ConsoleUtils;
import com.joysistvi.recordingapp.utils.ValidationUtils;

import java.util.Scanner;

public class RegisterView {

    private final UserController userController;
    private final Scanner scanner;

    public RegisterView(UserController userController, Scanner scanner) {
        this.userController = userController;
        this.scanner = scanner;
    }

    public void authRegister() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("USER REGISTRATION");

        System.out.print("Enter username (3-50 letters, numbers, underscore): ");
        String username = scanner.nextLine().trim();

        if (!ValidationUtils.isValidUsername(username)) {
            System.out.println("[!] Username must be 3-50 characters (letters, numbers, underscore only).");
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        System.out.print("Enter password (min 8 characters): ");
        String password = scanner.nextLine();

        if (!ValidationUtils.isValidPassword(password)) {
            System.out.println("[!] Password must be at least 8 characters.");
            ConsoleUtils.pressEnterToContinue(scanner);
            return;
        }

        User user = new User(null, username, password, ERole.USER);
        boolean isSuccess = userController.register(user);

        if (isSuccess) {
            System.out.println("\n[✓] Registration successful! You can now log in.");
        } else {
            System.out.println("[!] Registration failed. Username may already be taken.");
        }

        ConsoleUtils.pressEnterToContinue(scanner);
    }

}
