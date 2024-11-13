package dataaccess;

import errors.DataAccessException;
import model.AuthData;

public interface AuthDAO {
    void clearAuths() throws DataAccessException;
    void createAuth(AuthData authData) throws DataAccessException;

    AuthData getAuth(String authToken) throws  DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;
}
