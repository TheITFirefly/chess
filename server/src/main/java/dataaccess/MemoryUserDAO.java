package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO implements UserDAO {
    List<UserData> userTable = new ArrayList<>();
    public void clearUsers() throws DataAccessException {
        try {
            userTable.clear();
        } catch (Exception e) {
            throw new DataAccessException("Failed clearing users table");
        }
    }
}
