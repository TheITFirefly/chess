package service;

import dataaccess.AuthDAO;
import errors.DataAccessException;
import dataaccess.GameDAO;
import request.*;
import server.model.AuthData;
import model.GameData;
import response.ErrorResponse;
import response.ListGamesResponse;

import java.util.List;
import java.util.Objects;

public class ListGamesService {
    AuthDAO authDAO;
    GameDAO gameDAO;
    public ListGamesService(AuthDAO authDAO,GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public DataTransfer<?> listGames(ListGamesRequest request) {
        try {
            AuthData authData = authDAO.getAuth(request.authToken());
            if (authData != null) {
                if (Objects.equals(authData.authToken(), request.authToken())) {
                    List<GameData> games = gameDAO.listGames();
                    return new DataTransfer<ListGamesResponse>(new ListGamesResponse(games));
                }
                return new DataTransfer<ErrorResponse>(new ErrorResponse("Error: unauthorized"));
            }
            return new DataTransfer<ErrorResponse>(new ErrorResponse("Error: unauthorized"));
        } catch (DataAccessException e) {
            return new DataTransfer<ErrorResponse>(new ErrorResponse("Catastrophic failure"+e.getMessage()));
        }
    }
}