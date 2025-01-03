package service;

import dataaccess.AuthDAO;
import errors.DataAccessException;
import request.DataTransfer;
import response.ErrorResponse;
import request.LogoutRequest;
import response.LogoutResponse;
import server.model.AuthData;

public class LogoutService {
    AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public DataTransfer<?> logout(LogoutRequest request) {
        try {
            AuthData authData = authDAO.getAuth(request.authToken());
            if (authData != null) {
                authDAO.deleteAuth(request.authToken());
                return new DataTransfer<LogoutResponse>(new LogoutResponse());
            }
            return new DataTransfer<ErrorResponse>(new ErrorResponse("Error: unauthorized"));
        } catch (DataAccessException e) {
            return new DataTransfer<ErrorResponse>(new ErrorResponse("Catastrophic failure"+e.getMessage()));
        }
    }
}
