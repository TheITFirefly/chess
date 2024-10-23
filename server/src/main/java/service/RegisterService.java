package service;

import dataaccess.*;
import datatransfer.DataTransfer;
import datatransfer.ErrorResponse;
import datatransfer.RegisterRequest;
import datatransfer.RegisterResponse;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class RegisterService {
    MemoryAuthDAO authDAO;
    MemoryUserDAO userDAO;

    public RegisterService(MemoryAuthDAO authDAO, MemoryUserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public DataTransfer<?> register(RegisterRequest request) {
        try {
            UserData userData = userDAO.getUser(request.username());
            if (userData == null){
                userDAO.createUser(new UserData(request.username(), request.password(), request.email()));
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
