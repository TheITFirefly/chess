package dataaccess;

import errors.DataAccessException;
import errors.NotFoundException;
import model.UserData;

public interface UserDAO {
    void clearUsers() throws DataAccessException;
    UserData getUser(String username) throws DataAccessException, NotFoundException;
    void createUser(UserData userData) throws DataAccessException;

    class UnauthorizedAccessException extends Exception {
        public UnauthorizedAccessException(String message) {super(message);}
    }
}
