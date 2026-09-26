package com.joysistvi.recordingapp.views;

import com.joysistvi.recordingapp.controller.UserController;
import com.joysistvi.recordingapp.utils.ConsoleUtils;
import com.joysistvi.recordingapp.views.enums.EAuthScreen;

import java.util.Scanner;

public class Route {

    private final Scanner scanner = new Scanner(System.in);

    UserController userController = new UserController();

    private final LoginView loginView = new LoginView(userController, scanner);
    private final RegisterView registerView = new RegisterView(userController, scanner);

    public void start(){

        displayMenu();

        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();
        EAuthScreen eAuthScreen = selection(choice);

        switch (eAuthScreen){
            case LOGIN -> loginView.authLogin();
            case REGISTER -> registerView.authRegister();
            case EXIT -> System.out.println("Exiting...");
            case null -> {}
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
        ConsoleUtils.printHeader("RECORDING STUDIO APP");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
    }
}
