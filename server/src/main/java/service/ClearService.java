package service;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import datatransfer.ClearResponse;
import datatransfer.DataTransfer;
import datatransfer.ErrorResponse;

public class ClearService {
    MemoryAuthDAO authDAO;
    MemoryUserDAO userDAO;
    MemoryGameDAO gameDAO;

    public ClearService(MemoryAuthDAO authDAO, MemoryUserDAO userDAO, MemoryGameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public DataTransfer<?> clearDatabase() {
        try{
            authDAO.clearAuths();
            userDAO.clearUsers();
            gameDAO.clearGames();
        } catch (DataAccessException error) {
            return new DataTransfer<ErrorResponse>(new ErrorResponse("Failure","Error: unable to clear database"));
        }
        return new DataTransfer<ClearResponse>(new ClearResponse());
    }
}
