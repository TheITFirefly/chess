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
            throw  new DataAccessException("Failed clearing auth table");
        }
    }
}
