package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void clearAuths() throws DataAccessException;
    void createAuth(AuthData authData) throws DataAccessException;
}
