package facade.communicator;

import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
import notifier.ServerMessageHandler;
import ui.BoardPrinter;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketCommunicator extends Endpoint {
    Session session;
    ServerMessageHandler messageHandler = new ServerMessageHandler();
    ChessGame currentGame = null;
    private final BoardPrinter boardPrinter = new BoardPrinter();
    private final Gson gson = new Gson();

    public WebSocketCommunicator(String serverAddress, int port, ChessGame.TeamColor playerColor) {
        currentGame = new ChessGame();
        try {
            URI uri = new URI("ws", null, serverAddress, port, "/ws", null, null);

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this,uri);
            this.session.addMessageHandler(new MessageHandler.Whole<String>(){
                @Override
                public void onMessage(String messageJson) {
                    //System.out.println("Received a message:"+messageJson); //Debug statement
                    ServerMessage serverMessage = gson.fromJson(messageJson, ServerMessage.class);
                    var type = serverMessage.getServerMessageType();
                    messageHandler.handle(messageJson,type,currentGame,playerColor);
                }
            });
        } catch (URISyntaxException | DeploymentException | IOException e) {
            System.out.println("Failed to communicate with server over websocket");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println("Opened a websocket connection");
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Closed a websocket connection");
    }

    public void send(UserGameCommand command) {
        if (session != null && session.isOpen()) {
            try {
                // Convert the command to JSON
                String messageJson = gson.toJson(command);

                // Send the JSON message through the WebSocket session
                session.getBasicRemote().sendText(messageJson);
            } catch (IOException e) {
                System.err.println("Error sending message through WebSocket: " + e.getMessage());
                throw new RuntimeException("Failed to send message", e);
            }
        } else {
            System.err.println("WebSocket session is not open. Cannot send message.");
            throw new IllegalStateException("WebSocket session is not open.");
        }
    }

    public void highlightBoard(ChessGame.TeamColor playerColor, ChessPosition position) {
        System.out.println();
        if (currentGame == null) {
            var game = new ChessGame();
            if (position != null) {
                var boardRep = boardPrinter.highlightLegalMoves(game,position);
                boardPrinter.renderBoard(boardRep,playerColor);
                return;
            }
            var boardRep = boardPrinter.highlightLegalMoves(game,new ChessPosition(9,9));
            boardPrinter.renderBoard(boardRep, playerColor);
        } else {
            if (position != null) {
                var boardRep = boardPrinter.highlightLegalMoves(currentGame,position);
                boardPrinter.renderBoard(boardRep,playerColor);
                return;
            }
            var boardRep = boardPrinter.highlightLegalMoves(currentGame,new ChessPosition(9,9));
            boardPrinter.renderBoard(boardRep, playerColor);
        }
    }
}
