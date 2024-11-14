package service;

import dataaccess.*;
import errors.DataAccessException;
import errors.DuplicateEntryException;
import org.mindrot.jbcrypt.BCrypt;
import request.RegisterRequest;
import response.RegisterResponse;
import server.model.AuthData;
import server.model.UserData;

import java.util.UUID;

public class RegisterService {
    AuthDAO authDAO;
    UserDAO userDAO;

    public RegisterService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public RegisterResponse register(RegisterRequest request) throws DuplicateEntryException, DataAccessException {
        try {
            UserData userData = userDAO.getUser(request.username());
            if (userData == null){
                String hashedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());
                userDAO.createUser(new UserData(request.username(), hashedPassword, request.email()));
                String authToken = generateToken();
                authDAO.createAuth(new AuthData(authToken,request.username()));
                return new RegisterResponse(request.username(), authToken);
            }
            throw new DuplicateEntryException("Error: already taken");
        } catch (DataAccessException e) {
            throw new DataAccessException("Catastrophic failure"+e.getMessage());
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
