package ui;

import client.request.CreateGameRequest;
import client.request.JoinGameRequest;
import client.request.ListGamesRequest;
import client.request.LogoutRequest;
import client.response.CreateGameResponse;
import client.response.JoinGameResponse;
import client.response.ListGamesResponse;
import client.response.LogoutResponse;
import facade.ServerFacade;
import model.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PostLoginREPL {
    private final ServerFacade facade;
    private final String authToken;
    private final List<GameData> gamesList = new ArrayList<>();

    public PostLoginREPL(ServerFacade facade, String authToken) {
        this.facade = facade;
        this.authToken = authToken;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nEnter a command (help, logout, create, list, play, observe): ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "help" -> displayHelp();
                case "logout" -> running = handleLogout();
                case "create" -> handleCreateGame(scanner);
                case "list" -> handleListGames();
                case "play" -> handlePlayGame(scanner);
                case "observe" -> handleObserveGame(scanner); // Placeholder for future implementation
                default -> System.out.println("Invalid command. Type 'help' for a list of commands.");
            }
        }
    }

    private void displayHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("  help     - Displays this help message.");
        System.out.println("  logout   - Logs out and returns to the login screen.");
        System.out.println("  create   - Creates a new game on the server.");
        System.out.println("  list     - Lists all available games.");
        System.out.println("  play     - Join a game as a player.");
        System.out.println("  observe  - Observe a game (functionality pending).");
    }

    private boolean handleLogout() {
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        LogoutResponse logoutResponse = facade.logout(logoutRequest);

        if (logoutResponse.success()) {
            System.out.println("Successfully logged out.");
            return false; // Stop the loop
        } else {
            System.out.println("Logout failed: " + logoutResponse.errorMessage());
            return true; // Keep the loop running
        }
    }

    private void handleCreateGame(Scanner scanner) {
        System.out.print("Enter a name for the new game: ");
        String gameName = scanner.nextLine().trim();
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);
        CreateGameResponse createGameResponse = facade.createGame(createGameRequest);

        if (createGameResponse.success()) {
            System.out.println("Game '" + gameName + "' created successfully");
        } else {
            System.out.println("Failed to create game: " + createGameResponse.errorMessage());
        }
    }

    private void handleListGames() {
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ListGamesResponse listGamesResponse = facade.listGames(listGamesRequest);

        if (listGamesResponse.success()) {
            gamesList.clear();
            gamesList.addAll(listGamesResponse.games());

            if (gamesList.isEmpty()) {
                System.out.println("No games available.");
            } else {
                System.out.println("\nAvailable Games:");
                int index = 1;
                for (GameData game : gamesList) {
                    System.out.printf("%d. Game Name: %s, White player: %s, Black player: %s\n",
                            index++,
                            game.gameName(),
                            game.whiteUsername() != null ? game.whiteUsername() : "None",
                            game.blackUsername() != null ? game.blackUsername() : "None");
                }
            }
        } else {
            System.out.println("Failed to retrieve game list: " + listGamesResponse.errorMessage());
        }
    }

    private void handlePlayGame(Scanner scanner) {
        if (gamesList.isEmpty()) {
            System.out.println("No games listed. Use the 'list' command first.");
            return;
        }

        System.out.print("Enter the number of the game to join: ");
        int gameNumber;
        try {
            gameNumber = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            gameNumber = 0;
        }
        if (gameNumber < 1 || gameNumber > gamesList.size()) {
            System.out.println("Invalid game number.");
            return;
        }

        GameData selectedGame = gamesList.get(gameNumber - 1);
        System.out.print("Enter the color to play as (WHITE/BLACK): ");
        String color = scanner.nextLine().trim().toUpperCase();

        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, color, selectedGame.gameID());
        JoinGameResponse joinGameResponse = facade.joinGame(joinGameRequest);

        if (joinGameResponse.success()) {
            System.out.println("Successfully joined game " + selectedGame.gameName() + " as " + color + ".");
            new GameplayREPL(facade,authToken,selectedGame.game(),color).run();
        } else {
            System.out.println("Failed to join game: " + joinGameResponse.errorMessage());
        }
    }

    private void handleObserveGame(Scanner scanner) {
        System.out.println("Observe functionality is not yet implemented.");
        // Placeholder for future implementation
    }
}
