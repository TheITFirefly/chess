package ws;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
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
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final Gson gson = new Gson();
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
        if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
            MakeMoveCommand moveCommand = gson.fromJson(commandJson, MakeMoveCommand.class);
            handleMakeMove(session, moveCommand);
            return;
        }
        switch (command.getCommandType()) {
            case CONNECT -> handleConnect(session, command);
            case LEAVE -> handleLeave(session, command);
            case RESIGN -> handleResign(session, command);
        }
    }

    private void handleConnect(Session session, UserGameCommand command) {
        String authToken = command.getAuthToken();
        Integer gameID = command.getGameID();
        try {
            SessionKey key = createSessionKey(authToken, gameID);
            ChessGame game = null;
            try {
                game = gameDAO.getGame(gameID).game();
            } catch (DataAccessException e) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Failed to fetch game");
                String errorMessageJson = gson.toJson(errorMessage);
                connectionManager.sendMessage(session, errorMessageJson);
                return;
            }
            if (game == null){
                ErrorMessage errorMessage = new ErrorMessage("Error: Failed to fetch game");
                String errorMessageJson = gson.toJson(errorMessage);
                connectionManager.sendMessage(session, errorMessageJson);
                return;
            }
            connectionManager.addConnection(key, session);
            LoadGameMessage loadGameMessage = new LoadGameMessage(game);
            String loadGameJson = gson.toJson(loadGameMessage);
            connectionManager.sendMessage(session, loadGameJson);

            // Send NOTIFICATION message to other clients
            broadcastNotification(gameID, key, key.getRole() + " player " + key.getUsername() + " connected.");

            System.out.println("Connected: " + key);
        } catch (AuthNotFoundException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error: Bad authentication");
            String errorMessageJson = gson.toJson(errorMessage);
            connectionManager.sendMessage(session, errorMessageJson);
        } catch (GameNotFoundException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error: Bad Game ID");
            String errorMessageJson = gson.toJson(errorMessage);
            connectionManager.sendMessage(session, errorMessageJson);
        }
    }

    private void handleMakeMove(Session session, MakeMoveCommand command) {
        String authToken = command.getAuthToken();
        Integer gameID = command.getGameID();
        ChessMove move = command.getMove();
        try {
            SessionKey key = createSessionKey(authToken, gameID);
            if (key.getRole() == SessionKey.Role.OBSERVER) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Observers can't make moves");
                String errorMessageJson = gson.toJson(errorMessage);
                connectionManager.sendMessage(session, errorMessageJson);
                return;
            }
            GameData gameData = null;
            ChessGame game = null;
            try {
                gameData = gameDAO.getGame(gameID);
                game = gameData.game();
            } catch (DataAccessException e) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Failed to fetch game");
                String errorMessageJson = gson.toJson(errorMessage);
                connectionManager.sendMessage(session, errorMessageJson);
                return;
            }
            if (game == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Failed to fetch game");
                String errorMessageJson = gson.toJson(errorMessage);
                connectionManager.sendMessage(session, errorMessageJson);
                return;
            }
            if (game.getResignationStatus()) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Cannot make move. A player has resigned");
                String errorMessageJson = gson.toJson(errorMessage);
                connectionManager.sendMessage(session, errorMessageJson);
                return;
            }
            ChessGame.TeamColor curTurn = game.getTeamTurn();
            SessionKey.Role role = key.getRole();
            if (curTurn == ChessGame.TeamColor.WHITE && role != SessionKey.Role.WHITE ||
                    curTurn == ChessGame.TeamColor.BLACK && role != SessionKey.Role.BLACK) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Not allowed to make move for other players");
                String errorMessageJson = gson.toJson(errorMessage);
                connectionManager.sendMessage(session, errorMessageJson);
                return;
            }
            game.makeMove(move);
            GameData updatedGame = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(),gameData.gameName(),game);
            gameDAO.updateGame(updatedGame);
            broadcastLoadGame(gameID,game);
            broadcastNotification(gameID,key,"A move was just made");
        } catch (AuthNotFoundException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error: Bad authentication");
            String errorMessageJson = gson.toJson(errorMessage);
            connectionManager.sendMessage(session, errorMessageJson);
        } catch (GameNotFoundException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error: Bad Game ID");
            String errorMessageJson = gson.toJson(errorMessage);
            connectionManager.sendMessage(session, errorMessageJson);
        } catch (InvalidMoveException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error:" + e.getMessage());
            String errorMessageJson = gson.toJson(errorMessage);
            connectionManager.sendMessage(session, errorMessageJson);
        } catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error:" + e.getMessage());
            String errorMessageJson = gson.toJson(errorMessage);
            connectionManager.sendMessage(session, errorMessageJson);
        }
    }


    private void handleLeave(Session session, UserGameCommand command) {
        String authToken = command.getAuthToken();
        Integer gameID = command.getGameID();
        try {
            // Create the session key to identify the user and their role in the game
            SessionKey key = createSessionKey(authToken, gameID);

            // Remove the connection from the ConnectionManager
            connectionManager.removeConnection(key);

            // Update the database if the leaving user is a player
            GameData gameData = gameDAO.getGame(gameID); // Fetch current game data
            String updatedWhitePlayer = gameData.whiteUsername();
            String updatedBlackPlayer = gameData.blackUsername();

            if (key.getRole() == SessionKey.Role.WHITE) {
                updatedWhitePlayer = null; // White player is leaving
            } else if (key.getRole() == SessionKey.Role.BLACK) {
                updatedBlackPlayer = null; // Black player is leaving
            }

            // Create a new GameData object with updated player info
            GameData updatedGameData = new GameData(
                    gameID,
                    updatedWhitePlayer,
                    updatedBlackPlayer,
                    gameData.gameName(),
                    gameData.game()
            );

            // Update the game in the database
            gameDAO.updateGame(updatedGameData);

            // Notify other clients in the game
            String leavingPlayer;
            if (key.getRole() == SessionKey.Role.WHITE) {
                leavingPlayer = "White player";
            } else if (key.getRole() == SessionKey.Role.BLACK) {
                leavingPlayer = "Black player";
            } else {
                leavingPlayer = "Observer";
            }

            String leaveMessage = leavingPlayer + " (" + authToken + ") has left the game.";
            broadcastNotification(gameID, key, leaveMessage);

            System.out.println("User left: " + key.getUsername());
        } catch (AuthNotFoundException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error: Bad authentication");
            String errorMessageJson = gson.toJson(errorMessage);
            connectionManager.sendMessage(session, errorMessageJson);
        } catch (GameNotFoundException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error: Bad Game ID");
            String errorMessageJson = gson.toJson(errorMessage);
            connectionManager.sendMessage(session, errorMessageJson);
        } catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error: Database access failed");
            String errorMessageJson = gson.toJson(errorMessage);
            connectionManager.sendMessage(session, errorMessageJson);
        }
    }

    private void handleResign(Session session, UserGameCommand command) {
        String authToken = command.getAuthToken();
        Integer gameID = command.getGameID();
        try {
            // Create a session key and identify the user's role in the game
            SessionKey key = createSessionKey(authToken, gameID);

            if (key.getRole() == SessionKey.Role.OBSERVER) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Observers can't resign");
                String errorMessageJson = gson.toJson(errorMessage);
                connectionManager.sendMessage(session, errorMessageJson);
                return;
            }

            // Fetch the game data from the database
            GameData gameData = gameDAO.getGame(gameID);
            ChessGame game = gameData.game();

            if (game == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Game not found");
                String errorMessageJson = gson.toJson(errorMessage);
                connectionManager.sendMessage(session, errorMessageJson);
                return;
            }

            // Check if the game is already over
            if (game.getResignationStatus()) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Game is already over. A player has resigned");
                String errorMessageJson = gson.toJson(errorMessage);
                connectionManager.sendMessage(session, errorMessageJson);
                return;
            }

            // Mark the resignation in the game state
            game.resign();
            // Persist the updated game state
            GameData updatedGameData = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
            gameDAO.updateGame(updatedGameData);

            // Notify resigning player
            NotificationMessage resignMessage = new NotificationMessage("You have resigned");
            String resignJson = gson.toJson(resignMessage);
            connectionManager.sendMessage(session,resignJson);
            String resigningPlayer = (key.getRole() == SessionKey.Role.WHITE) ? gameData.whiteUsername() : gameData.blackUsername();
            String resignationMessage = resigningPlayer + " has resigned.";
            broadcastNotification(gameID, key, resignationMessage);

        } catch (AuthNotFoundException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error: Bad authentication");
            String errorMessageJson = gson.toJson(errorMessage);
            connectionManager.sendMessage(session, errorMessageJson);
        } catch (GameNotFoundException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error: Game not found");
            String errorMessageJson = gson.toJson(errorMessage);
            connectionManager.sendMessage(session, errorMessageJson);
        } catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage("Error: Failed to update game state");
            String errorMessageJson = gson.toJson(errorMessage);
            connectionManager.sendMessage(session, errorMessageJson);
        }
    }

    private SessionKey createSessionKey(String authToken, Integer gameID) throws AuthNotFoundException, GameNotFoundException {
        String username;
        try {
            AuthData auth = authDAO.getAuth(authToken);
            if (auth == null) {
                throw new AuthNotFoundException("No auth exists");
            }
            username = auth.username(); // Use username instead of authToken
        } catch (DataAccessException e) {
            throw new AuthNotFoundException(e.getMessage());
        }

        GameData gameData;
        try {
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
        return new SessionKey(role, gameID, username); // Pass username instead of authToken
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

    private void broadcastLoadGame(Integer gameID, ChessGame messageContent) {
        LoadGameMessage loadGameMessage = new LoadGameMessage(messageContent);
        String notificationJson = gson.toJson(loadGameMessage);
        connectionManager.getConnections().forEach((key, session) -> {
            if (key.getGameID().equals(gameID)) {
                connectionManager.sendMessage(session, notificationJson);
            }
        });
    }
}
