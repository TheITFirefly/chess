package service;

import dataaccess.*;
import org.mindrot.jbcrypt.BCrypt;
import request.DataTransfer;
import response.ErrorResponse;
import request.RegisterRequest;
import response.RegisterResponse;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class RegisterService {
    AuthDAO authDAO;
    UserDAO userDAO;

    public RegisterService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public DataTransfer<?> register(RegisterRequest request) {
        try {
            UserData userData = userDAO.getUser(request.username());
            if (userData == null){
                String hashedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());
                userDAO.createUser(new UserData(request.username(), hashedPassword, request.email()));
                String authToken = generateToken();
                authDAO.createAuth(new AuthData(authToken,request.username()));
                return new DataTransfer<RegisterResponse>(new RegisterResponse(request.username(), authToken));
            }
            return new DataTransfer<ErrorResponse>(new ErrorResponse("Forbidden","Error: already taken"));
        } catch (DataAccessException e) {
            return new DataTransfer<ErrorResponse>(new ErrorResponse("Catastrophic failure",e.getMessage()));
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
