package service;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import datatransfer.DataTransfer;

public class ClearService {
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryGameDAO gameDAO = new MemoryGameDAO();

    public ClearService(MemoryAuthDAO authDAO, MemoryUserDAO userDAO, MemoryGameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public DataTransfer clearDatabase() {
        try{
            authDAO.clearAuths();
            userDAO.clearUsers();
            gameDAO.clearGames();
        } catch (DataAccessException error) {
            return new DataTransfer("Error", error.getMessage(), null);
        }
        return new DataTransfer("Success",null,null);
    }
}
