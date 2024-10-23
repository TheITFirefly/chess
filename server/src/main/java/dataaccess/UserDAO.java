package dataaccess;

import model.UserData;

public interface UserDAO {
    void clearUsers() throws DataAccessException;
    UserData getUser(String username) throws DataAccessException, NotFoundException;
    void createUser(UserData userData) throws DataAccessException, DuplicateEntryException;
}
