package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import request.*;
import model.AuthData;
import model.GameData;
import response.CreateGameResponse;
import response.ErrorResponse;

import java.util.Objects;

public class CreateGameService {
    AuthDAO authDAO;
    GameDAO gameDAO;
    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public DataTransfer<?> createGame(CreateGameRequest request) {
        try {
            AuthData authData = authDAO.getAuth(request.authToken());
            if (authData != null) {
                if (Objects.equals(authData.authToken(), request.authToken())) {
                    int gameID = gameDAO.listGames().size()+1;
                    gameDAO.createGame(new GameData(gameID,null,null,request.body().gameName(), new ChessGame()));
                    return new DataTransfer<CreateGameResponse>(new CreateGameResponse(gameID));
                }
                return new DataTransfer<ErrorResponse>(new ErrorResponse("Forbidden", "Error: unauthorized"));
            }
            return new DataTransfer<ErrorResponse>(new ErrorResponse("Forbidden", "Error: unauthorized"));
        } catch (DataAccessException e) {
            return new DataTransfer<ErrorResponse>(new ErrorResponse("Catastrophic failure", e.getMessage()));
        }
    }
}
