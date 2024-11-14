import java.util.Scanner;

import chess.*;
import facade.ServerFacade;

public class Main {


    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Do you want to use a custom server address? (yes/no): ");
        String useCustomServer = scanner.nextLine().trim().toLowerCase();

        ServerFacade facade;

        if (useCustomServer.equals("yes")) {
            // Ask for custom server address and port
            System.out.print("Enter server address (or press Enter to use default): ");
            String serverAddress = scanner.nextLine().trim();

            System.out.print("Enter port number (or press Enter to use default): ");
            String portInput = scanner.nextLine().trim();

            // Determine which constructor to use based on user input
            if (!serverAddress.isEmpty() && !portInput.isEmpty()) {
                // Server address and port provided
                int port = Integer.parseInt(portInput);
                facade = new ServerFacade(serverAddress, port);
            } else if (!serverAddress.isEmpty()) {
                // Only server address provided
                facade = new ServerFacade(serverAddress);
            } else if (!portInput.isEmpty()) {
                // Only port provided
                int port = Integer.parseInt(portInput);
                facade = new ServerFacade(port);
            } else {
                // Neither provided; use defaults
                facade = new ServerFacade();
            }
        } else {
            // Use default server and port
            facade = new ServerFacade();
        }

        // Confirm server connection details
        System.out.println("Client will connect to server at " + facade.getServerAddress() + " on port " + facade.getPort());
        // TODO: start repl loop. Write repl class for each layer of the loop
        // User has exited all repl loops
        System.out.println("Exited");
    }
}