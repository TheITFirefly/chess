import java.util.Scanner;

import chess.*;
import facade.ServerFacade;
import repl.PreLoginREPL;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Do you want to use a custom server address? (yes/no) Default (no): ");
        String useCustomServer = scanner.nextLine().trim().toLowerCase();

        // Default to localhost and 8080
        ServerFacade facade = new ServerFacade();

        if (useCustomServer.equals("yes")) {
            // Ask for custom server address and port
            System.out.print("Enter server address (or press Enter to use default (localhost)): ");
            String serverAddress = scanner.nextLine().trim();

            System.out.print("Enter port number (or press Enter to use default (8080)): ");
            String portInput = scanner.nextLine().trim();

            // Determine which constructor to use based on user input
            if (!serverAddress.isEmpty() && !portInput.isEmpty()) {
                // Server address and port provided
                int port;
                try {
                    port = Integer.parseInt(portInput);
                } catch (NumberFormatException e) {
                    System.out.println("Port was not a number. Using default");
                }

            } else if (!serverAddress.isEmpty()) {
                facade.setServerAddress(serverAddress);
            } else if (!portInput.isEmpty()) {
                // Only port provided
                int port = Integer.parseInt(portInput);
                facade.setServerPort(port);
            }
        }

        // Confirm server connection details
        System.out.println("Client will connect to server at " + facade.getServerAddress() + " on port " + facade.getPort());
        PreLoginREPL preLogin = new PreLoginREPL(facade);
        preLogin.run();
        // User has exited all repl loops
        System.out.println("Exited");
    }
}