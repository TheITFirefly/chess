package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import errors.DataAccessException;
import dataaccess.GameDAO;
import request.*;
import model.AuthData;
import model.GameData;
import response.ErrorResponse;
import response.JoinGameResponse;
import response.TakenErrorResponse;

import java.util.Objects;

public class JoinGameService {
    AuthDAO authDAO;
    GameDAO gameDAO;

    public JoinGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public DataTransfer<?> joinGame(JoinGameRequest request) {
        try {
            AuthData authData = authDAO.getAuth(request.authToken());
            if (authData == null || !Objects.equals(authData.authToken(), request.authToken())) {
                return new DataTransfer<>(new ErrorResponse("Error: unauthorized"));
            }

            GameData gameData = gameDAO.getGame(request.body().gameID());
            int gameID = gameData.gameID();
            String whiteUsername = gameData.whiteUsername();
            String blackUsername = gameData.blackUsername();
            String gameName = gameData.gameName();
            ChessGame game = gameData.game();

            String requestedColor = request.body().playerColor().toUpperCase();  // Handle color case-sensitively

            // Check if the requested color is already taken
            if (requestedColor.equals("WHITE") && whiteUsername != null) {
                return new DataTransfer<>(new TakenErrorResponse("Error: White color already taken"));
            } else if (requestedColor.equals("BLACK") && blackUsername != null) {
                return new DataTransfer<>(new TakenErrorResponse("Error: Black color already taken"));
            }

            // Assign player to the requested color if available
            if (requestedColor.equals("WHITE")) {
                whiteUsername = authData.username();
            } else {
                blackUsername = authData.username();
            }

            // Update the game with the new player assignment
            gameDAO.updateGame(new GameData(gameID, whiteUsername, blackUsername, gameName, game));

            return new DataTransfer<>(new JoinGameResponse());

        } catch (DataAccessException e) {
            return new DataTransfer<>(new ErrorResponse("Catastrophic failure"+e.getMessage()));
        }
    }
}
