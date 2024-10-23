package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import datatransfer.*;
import model.AuthData;
import model.GameData;

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
                return new DataTransfer<ErrorResponse>(new ErrorResponse("Forbidden", "Error: unauthorized"));
            }
            return new DataTransfer<ErrorResponse>(new ErrorResponse("Forbidden", "Error: unauthorized"));
        } catch (DataAccessException e) {
            return new DataTransfer<ErrorResponse>(new ErrorResponse("Catastrophic failure", e.getMessage()));
        }
    }
}