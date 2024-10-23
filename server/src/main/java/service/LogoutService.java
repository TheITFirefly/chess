package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import datatransfer.DataTransfer;
import datatransfer.ErrorResponse;
import datatransfer.LogoutRequest;
import datatransfer.LogoutResponse;
import model.AuthData;

public class LogoutService {
    MemoryAuthDAO authDAO;

    public LogoutService(MemoryAuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public DataTransfer<?> logout(LogoutRequest request) {
        try {
            AuthData authData = authDAO.getAuth(request.authToken());
            if (authData != null) {
                authDAO.deleteAuth(request.authToken());
                return new DataTransfer<LogoutResponse>(new LogoutResponse());
            }
            return new DataTransfer<ErrorResponse>(new ErrorResponse("Forbidden", "Error: unauthorized"));
        } catch (DataAccessException e) {
            return new DataTransfer<ErrorResponse>(new ErrorResponse("Catastrophic failure", e.getMessage()));
        }
    }
}
