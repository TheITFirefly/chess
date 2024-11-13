package service;

import dataaccess.*;
import errors.DataAccessException;
import response.ClearResponse;
import request.DataTransfer;
import response.ErrorResponse;

public class ClearService {
    AuthDAO authDAO;
    UserDAO userDAO;
    GameDAO gameDAO;

    public ClearService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public DataTransfer<?> clearDatabase() {
        try{
            userDAO.clearUsers();
            authDAO.clearAuths();
            gameDAO.clearGames();
        } catch (DataAccessException error) {
            return new DataTransfer<ErrorResponse>(new ErrorResponse("Error: unable to clear database"));
        }
        return new DataTransfer<ClearResponse>(new ClearResponse());
    }
}
