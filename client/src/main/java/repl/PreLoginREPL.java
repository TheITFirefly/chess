package repl;

import java.util.Scanner;

import client.response.RegisterResponse;
import facade.ServerFacade;
import client.request.LoginRequest;
import client.request.RegisterRequest;
import client.response.LoginResponse;

public class PreLoginREPL {
    private final ServerFacade facade;

    public PreLoginREPL(ServerFacade facade) {
        this.facade = facade;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Chess Client!");
        boolean running = true;

        while (running) {
            System.out.print("\nEnter a command (help, quit, login, register): ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "help" -> displayHelp();
                case "quit" -> {
                    System.out.println("Exiting the program. Goodbye!");
                    running = false;
                }
                case "login" -> handleLogin(scanner);
                case "register" -> handleRegister(scanner);
                default -> System.out.println("Invalid command. Type 'help' for a list of commands.");
            }
        }
    }

    private void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("  help     - Displays this help message.");
        System.out.println("  quit     - Exits the program.");
        System.out.println("  login    - Log in to an existing account.");
        System.out.println("  register - Register a new account.");
    }

    private void handleLogin(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResponse loginResponse = facade.login(loginRequest);

        if (loginResponse.success()) {
            System.out.println("Login successful! Welcome, " + username + ".");
            // Transition to PostLogin UI (not implemented yet)
            new PostLoginREPL(facade, loginResponse.authToken()).run();
        } else {
            System.out.println("Login failed: " + loginResponse.errorMessage());
        }
    }

    private void handleRegister(Scanner scanner) {
        System.out.print("Enter a username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter a password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Enter an email address: ");
        String email = scanner.nextLine().trim();


        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        RegisterResponse registerResponse = facade.register(registerRequest);

        if (registerResponse.success()) {
            System.out.println("Registration successful! Welcome, " + username + ".");
            // Transition to PostLogin UI (not implemented yet)
            new PostLoginREPL(facade, registerResponse.authToken()).run();
        } else {
            System.out.println("Registration failed: " + registerResponse.errorMessage());
        }
    }
}
