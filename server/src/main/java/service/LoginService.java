package service;

import dataaccess.AuthDAO;
import errors.DataAccessException;
import dataaccess.UserDAO;
import org.mindrot.jbcrypt.BCrypt;
import request.*;
import model.AuthData;
import model.UserData;
import response.LoginResponse;

import java.util.UUID;

public class LoginService {
    AuthDAO authDAO;
    UserDAO userDAO;
    public LoginService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public LoginResponse login(LoginRequest request) throws UserDAO.UnauthorizedAccessException, DataAccessException {
        try {
            UserData userData = userDAO.getUser(request.username());
            if (userData == null) {
                throw new UserDAO.UnauthorizedAccessException("Error: unauthorized");
            } else if (!BCrypt.checkpw(request.password(), userData.password())) {
                throw new UserDAO.UnauthorizedAccessException("Error: unauthorized");
            }
            String authToken = generateToken();
            authDAO.createAuth(new AuthData(authToken, request.username()));
            return new LoginResponse(request.username(), authToken);
        } catch (DataAccessException e) {
            throw new DataAccessException("Catastrophic failure"+e.getMessage());
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
