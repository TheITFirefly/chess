package repl;

import chess.*;
import facade.ServerFacade;
import websocket.commands.*;

import java.util.Scanner;

public class GameplayREPL {
    ServerFacade facade;
    String authToken;
    ChessGame.TeamColor playerColor = ChessGame.TeamColor.WHITE;
    Integer gameID;

    public GameplayREPL(ServerFacade facade, String authToken, Integer gameID, String playerColor) {
        this.facade = facade;
        this.authToken = authToken;
        if (playerColor != null) {
            this.playerColor = playerColor.equals("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        }
        this.gameID = gameID;
        facade.setPlayerColor(this.playerColor);
    }

    public void run() {
        facade.openWebsocket();
        UserGameCommand connectCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT,authToken,gameID);
        facade.sendMessage(connectCommand);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.print("Enter a command: ");
            String command = scanner.nextLine().trim().toUpperCase();

            switch (command) {
                case "HELP":
                    displayHelp();
                    break;
                case "REDRAW":
                    facade.printBoard(this.playerColor);
                    break;
                case "LEAVE":
                    facade.sendMessage(new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID));
                    running = false; // Exit the loop
                    break;
                case "MOVE":
                    System.out.print("Enter the position of the piece you would like to move (e.g., 'e2'): ");
                    String startPositionInput = scanner.nextLine().trim();
                    ChessPosition startPosition = parseChessPosition(startPositionInput);

                    System.out.print("Enter the position you would like to move it to (e.g., 'e8'): ");
                    String endPositionInput = scanner.nextLine().trim();
                    ChessPosition endPosition = parseChessPosition(endPositionInput);

                    // Ask for promotion piece
                    System.out.print("If promoting, enter the piece type (QUEEN, ROOK, BISHOP, KNIGHT). Press Enter to skip: ");
                    String promotionInput = scanner.nextLine().trim().toUpperCase();
                    ChessPiece.PieceType promotionPiece = null;

                    if (!promotionInput.isEmpty()) {
                        promotionPiece = parsePieceType(promotionInput);
                        if (promotionPiece == null) {
                            System.out.println("Invalid piece type. No promotion will be applied.");
                        }
                    }

                    // Create the ChessMove object
                    ChessMove move = new ChessMove(startPosition, endPosition, promotionPiece);

                    // Send the move command
                    facade.sendMessage(new MakeMoveCommand(authToken, gameID, move));
                    System.out.println("Move sent: " + move);
                    break;
                    case "RESIGN":
                    System.out.print("Are you sure you want to resign? (yes/no): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                        facade.sendMessage(new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID));
                        System.out.println("You have resigned.");
                        running = false;
                    }
                    break;
                case "HIGHLIGHT":
                    System.out.print("Enter the piece position to highlight (e.g. 'e2'): ");
                    String positionInput = scanner.nextLine().trim();
                    ChessPosition position = parseChessPosition(positionInput);
                    facade.highlightBoard(this.playerColor, position);
                    System.out.println("Highlighting moves for piece at: " + positionInput);
                    break;
                default:
                    System.out.println("Invalid command. Type HELP for a list of commands.");
            }
        }
    }

    private ChessPosition parseChessPosition(String input) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Check if the input has exactly two characters
            if (input.length() != 2) {
                System.out.print("Invalid input. Please enter a valid position (e.g. 'e2'): ");
                input = scanner.nextLine().trim();
                continue;
            }

            char file = input.charAt(0); // The letter (column)
            char rank = input.charAt(1); // The number (row)

            // Validate the file is between 'a' and 'h'
            if (file < 'a' || file > 'h') {
                System.out.print("Invalid file (must be a-h). Please enter a valid position (e.g. 'e2'): ");
                input = scanner.nextLine().trim();
                continue;
            }

            // Validate the rank is between '1' and '8'
            if (rank < '1' || rank > '8') {
                System.out.print("Invalid rank (must be 1-8). Please enter a valid position (e.g. 'e2'): ");
                input = scanner.nextLine().trim();
                continue;
            }

            // Convert file ('a' -> 1, ..., 'h' -> 8)
            int fileNumber = file - 'a' + 1;

            // Convert rank ('1' -> 1, ..., '8' -> 8)
            int rankNumber = rank - '1' + 1;

            return new ChessPosition(rankNumber, fileNumber);
        }
    }

    private ChessPiece.PieceType parsePieceType(String input){
        return switch (input.toLowerCase()) {
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "queen" -> ChessPiece.PieceType.QUEEN;
            default -> null;
        };
    }

    private void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("HELP - Displays this help text.");
        System.out.println("REDRAW - Redraws the chess board.");
        System.out.println("LEAVE - Leave the game.");
        System.out.println("MOVE - Make a move");
        System.out.println("RESIGN - Resign from the game.");
        System.out.println("HIGHLIGHT - Highlight legal moves for a piece");
    }
}
