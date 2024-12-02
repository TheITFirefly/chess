package ws;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import errors.AuthNotFoundException;
import errors.DataAccessException;
import errors.GameNotFoundException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.model.AuthData;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private static final Gson gson = new Gson();
    private final ConnectionManager connectionManager = new ConnectionManager();
    AuthDAO authDAO;
    UserDAO userDAO;
    GameDAO gameDAO;

    public WebSocketHandler(DatabaseAuthDAO authDAO, DatabaseUserDAO userDAO, DatabaseGameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("WebSocket connected: " + session.getRemoteAddress());
    }

    @OnWebSocketMessage
    public void handleMessage(Session session, String commandJson) {
        UserGameCommand command = gson.fromJson(commandJson, UserGameCommand.class);
        try {
            switch (command.getCommandType()) {
                case CONNECT -> handleConnect(session, command);
//                case MAKE_MOVE -> handleMakeMove(session, command);
                case LEAVE -> handleLeave(session, command);
                case RESIGN -> handleResign(session, command);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally send error response to the client
        }
    }

    private void handleConnect(Session session, UserGameCommand command) {
        String authToken = command.getAuthToken();
        Integer gameID = command.getGameID();

        try {
            // Create the session key
            SessionKey key = createSessionKey(authToken, gameID);
            connectionManager.addConnection(key, session);

            // Send LOAD_GAME message to the root client
            ChessGame game = null;
            try {
                game = gameDAO.getGame(gameID).game();
            } catch (DataAccessException e) {
                // Need to send an ERROR message instead
                throw new RuntimeException(e.getMessage());
            }
            if (game == null){
                // Need to send an ERROR message instead
                throw new RuntimeException("Error occurred fetching game");
            }
            LoadGameMessage loadGameMessage = new LoadGameMessage(game);
            String loadGameJson = gson.toJson(loadGameMessage);
            connectionManager.sendMessage(session, loadGameJson);

            // Send NOTIFICATION message to other clients
            broadcastNotification(gameID, key, key.getRole() + " player " + key.getAuthToken() + " connected.");

            System.out.println("Connected: " + key);
        } catch (AuthNotFoundException | GameNotFoundException e) {
            // Send error message to root client
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            String errorJson = gson.toJson(errorMessage);
            connectionManager.sendMessage(session, errorJson);

            System.out.println("Error handling connect: " + e.getMessage());
        }
    }

    private void handleLeave(Session session, UserGameCommand command) {
        throw new RuntimeException("not implemented");
    }

    private void handleResign(Session session, UserGameCommand command) {
        throw new RuntimeException("not implemented");
    }

    private SessionKey createSessionKey(String authToken, Integer gameID) throws AuthNotFoundException, GameNotFoundException {
        String username;
        try {
            AuthData auth = authDAO.getAuth(authToken);
            username = auth.username();
        } catch (DataAccessException e) {
            throw new AuthNotFoundException(e.getMessage());
        }
        System.out.println("Username: "+username);

        GameData gameData;
        try{
            gameData = gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            throw new GameNotFoundException(e.getMessage());
        }
        if (gameData == null) {
            throw new GameNotFoundException("Error: couldn't find game");
        }
        SessionKey.Role role;
        if (Objects.equals(username, gameData.whiteUsername())) {
            role = SessionKey.Role.WHITE;
        } else if (Objects.equals(username, gameData.blackUsername())) {
            role = SessionKey.Role.BLACK;
        } else {
            role = SessionKey.Role.OBSERVER;
        }
        return new SessionKey(role,gameID,authToken);
    }

    private void broadcastNotification(Integer gameID, SessionKey senderKey, String messageContent) {
        // Construct the NOTIFICATION message
        NotificationMessage notificationMessage = new NotificationMessage(messageContent);
        String notificationJson = gson.toJson(notificationMessage);

        // Iterate over all connections in the ConnectionManager
        connectionManager.getConnections().forEach((key, session) -> {
            if (key.getGameID().equals(gameID) && !key.equals(senderKey)) {
                connectionManager.sendMessage(session, notificationJson);
            }
        });
    }
}
