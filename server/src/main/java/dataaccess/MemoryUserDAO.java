package dataaccess;

import errors.DataAccessException;
import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO implements UserDAO {
    List<UserData> userTable = new ArrayList<>();
    public void clearUsers() throws DataAccessException {
        try {
            userTable.clear();
        } catch (Exception e) {
            throw new DataAccessException("Error: Failed clearing users table");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try {
            for (UserData user : userTable) {
                if (user.username().equals(username)) {
                    return user;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Error: Couldn't reach database");
        }
        return null;
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        try {
            userTable.add(userData);
        } catch (Exception e) {
            throw new DataAccessException("Error: Couldn't reach database");
        }
    }
}
