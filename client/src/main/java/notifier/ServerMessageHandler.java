package notifier;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import ui.BoardPrinter;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class ServerMessageHandler {
    private final Gson gson = new Gson();
    private final BoardPrinter boardPrinter = new BoardPrinter();
    private ChessGame newGame;

    public ServerMessageHandler(){}

    public void handle(String serverMessageJson, ServerMessage.ServerMessageType type, ChessGame currentGame, ChessGame.TeamColor playerColor) {
        // System.out.println("Handling message. Type: "+type); // Debug statement
        switch (type) {
            case LOAD_GAME -> {
                LoadGameMessage loadGameMessage = gson.fromJson(serverMessageJson, LoadGameMessage.class);
                handleLoadGameMessage(loadGameMessage,currentGame,playerColor);
            }
            case ERROR -> {
                ErrorMessage errorMessage = gson.fromJson(serverMessageJson,ErrorMessage.class);
                handleErrorMessage(errorMessage);
            }
            case NOTIFICATION -> {
                NotificationMessage notificationMessage = gson.fromJson(serverMessageJson,NotificationMessage.class);
                handleNotificationMessage(notificationMessage);
            }
        }
    }

    private void handleLoadGameMessage(LoadGameMessage message, ChessGame currentGame, ChessGame.TeamColor playerColor) {
        // System.out.println("Received a load game message"); // Debug statement
        ChessGame newGame = message.game;
        // System.out.print("Message null? "); // Debug statement
        // System.out.print(newGame == null); // Debug statement
        ChessMove move = message.move;
        String[][] printBoard;
        if (move != null) {
            printBoard = boardPrinter.highlightMove(newGame, move);
        } else {
            printBoard = boardPrinter.highlightLegalMoves(newGame, new ChessPosition(9, 9));
        }
        System.out.println();
        System.out.println();
        boardPrinter.renderBoard(printBoard, playerColor);
        currentGame.setTeamTurn(newGame.getTeamTurn());
        currentGame.setBoard(newGame.getBoard());
        currentGame.setResignationStatus(newGame.getResignationStatus());
    }

    private void handleErrorMessage(ErrorMessage message) {
        System.out.println(message.errorMessage);
    }

    private void handleNotificationMessage(NotificationMessage message) {
        System.out.println(message.message);
    }
}
