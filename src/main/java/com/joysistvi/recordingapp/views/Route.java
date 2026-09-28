package com.joysistvi.recordingapp.views;

import com.joysistvi.recordingapp.controller.UserController;
import com.joysistvi.recordingapp.repositories.UserRepository;
import com.joysistvi.recordingapp.services.UserService;
import com.joysistvi.recordingapp.utils.ConsoleUtils;
import com.joysistvi.recordingapp.views.enums.EAuthScreen;

import java.util.Scanner;

public class Route {

    private final Scanner scanner = new Scanner(System.in);

    private final UserRepository userRepository = new UserRepository();
    private final UserService userService = new UserService(userRepository);
    private final UserController userController = new UserController(userService);

    private final LoginView loginView = new LoginView(userController, scanner);
    private final RegisterView registerView = new RegisterView(userController, scanner);

    public void start(){
        boolean running = true;

        while (running) {
            displayMenu();

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            EAuthScreen eAuthScreen = selection(choice);

            if (eAuthScreen == null) {
                System.out.println("\n[!] Invalid option. Please try again.");
                ConsoleUtils.pressEnterToContinue(scanner);
                continue;
            }

            switch (eAuthScreen){
                case LOGIN -> loginView.authLogin();
                case REGISTER -> registerView.authRegister();
                case EXIT -> {
                    System.out.println("Exiting...");
                    running = false;
                }
            }
        }
    }

    private EAuthScreen selection(String choice){
        switch (choice) {

            case "1" -> { return EAuthScreen.LOGIN; }
            case "2" -> { return  EAuthScreen.REGISTER; }
            case "3" -> { return  EAuthScreen.EXIT; }
        }
        return null;
    }

    private void displayMenu() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.printHeader("RECORDING STUDIO APP");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
    }
}
