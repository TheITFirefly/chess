package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.UserDAO;
import org.mindrot.jbcrypt.BCrypt;
import request.*;
import model.AuthData;
import model.UserData;
import response.ErrorResponse;
import response.LoginResponse;

import java.util.UUID;

public class LoginService {
    AuthDAO authDAO;
    UserDAO userDAO;
    public LoginService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public DataTransfer<?> login(LoginRequest request) {
        try {
            UserData userData = userDAO.getUser(request.username());
            if (userData == null) {
                return new DataTransfer<ErrorResponse>(new ErrorResponse("Forbidden","Error: unauthorized"));
            } else if (!BCrypt.checkpw(request.password(), userData.password())) {
                return new DataTransfer<ErrorResponse>(new ErrorResponse("Forbidden","Error: unauthorized"));
            }
            String authToken = generateToken();
            authDAO.createAuth(new AuthData(authToken, request.username()));
            return new DataTransfer<LoginResponse>(new LoginResponse(request.username(), authToken));
        } catch (DataAccessException e) {
            return new DataTransfer<ErrorResponse>(new ErrorResponse("Catastrophic failure",e.getMessage()));
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
