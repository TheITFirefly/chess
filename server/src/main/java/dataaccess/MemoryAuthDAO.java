package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;

public class MemoryAuthDAO implements AuthDAO {
    List<AuthData> authTable = new ArrayList<>();
    public void clearAuths() throws DataAccessException{
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
}
