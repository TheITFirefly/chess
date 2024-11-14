package service;

import errors.DataAccessException;
import dataaccess.MemoryAuthDAO;
import server.model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import request.DataTransfer;
import request.LogoutRequest;
import response.ErrorResponse;
import response.LogoutResponse;

public class LogoutServiceTest {

    @Test
    @Order(1)
    @DisplayName("Logout positive")
    public void logoutPositive() throws DataAccessException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        LogoutService logoutService = new LogoutService(authDAO);
        authDAO.createAuth(new AuthData("validToken", "player1"));
        LogoutRequest request = new LogoutRequest("validToken");
        DataTransfer<?> result = logoutService.logout(request);
        Assertions.assertInstanceOf(LogoutResponse.class, result.data());
        AuthData authData = authDAO.getAuth("validToken");
        Assertions.assertNull(authData);  // Token should no longer exist
    }

    @Test
    @Order(2)
    @DisplayName("Logout negative - Unauthorized")
    public void logoutNegative() throws DataAccessException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        LogoutService logoutService = new LogoutService(authDAO);
        LogoutRequest request = new LogoutRequest("invalidToken");
        DataTransfer<?> result = logoutService.logout(request);
        Assertions.assertInstanceOf(ErrorResponse.class, result.data());
        ErrorResponse errorResponse = (ErrorResponse) result.data();
        Assertions.assertEquals("Error: unauthorized", errorResponse.message());
    }
}
