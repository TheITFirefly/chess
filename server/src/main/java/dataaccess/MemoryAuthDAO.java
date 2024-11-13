package dataaccess;

import errors.DataAccessException;
import model.AuthData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO {
    List<AuthData> authTable = new ArrayList<>();
    public void clearAuths() throws DataAccessException {
        try {
            authTable.clear();
        } catch (Exception e) {
            throw new DataAccessException("Error: Failed clearing auth table");
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        try {
            authTable.add(authData);
        } catch (Exception e) {
            throw new DataAccessException("Error: Failed to add user");
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try {
            for (AuthData authData : authTable) {
                if (Objects.equals(authData.authToken(), authToken)) {
                    return authData;
                }
            }
            return null;
        } catch (Exception e) {
            throw new DataAccessException("Error: Failed to contact database");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try {
            AuthData authData = getAuth(authToken);
            authTable.remove(authData);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
