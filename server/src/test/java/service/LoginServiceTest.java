package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import errors.DataAccessException;
import errors.DuplicateEntryException;
import request.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import response.ErrorResponse;
import response.LoginResponse;
import response.RegisterResponse;

public class LoginServiceTest {
    @Test
    @Order(1)
    @DisplayName("Login user positive")
    public void loginPositive() throws DuplicateEntryException, DataAccessException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        RegisterService registerService = new RegisterService(authDAO, userDAO);
        RegisterRequest registerRequest = new RegisterRequest("lolcats", "passw00rd", "foo@bar.baz");
        // Registration should succeed
        registerService.register(registerRequest);
        LoginService loginService = new LoginService(authDAO, userDAO);
        LoginRequest loginRequest = new LoginRequest("lolcats", "passw00rd");
        Assertions.assertDoesNotThrow(() -> {
            LoginResponse response = loginService.login(loginRequest);
        });
    }


    @Test
    @Order(2)
    @DisplayName("Login user negative - Unauthorized")
    public void loginNegative() throws UserDAO.UnauthorizedAccessException, DataAccessException, DuplicateEntryException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        RegisterService registerService = new RegisterService(authDAO, userDAO);
        RegisterRequest registerRequest = new RegisterRequest("lolcats", "passw00rd", "foo@bar.baz");
        // Register shouldn't throw
        registerService.register(registerRequest);
        LoginService loginService = new LoginService(authDAO, userDAO);
        LoginRequest loginRequest = new LoginRequest("lolcats", "pwrd");
        Assertions.assertThrows(
                UserDAO.UnauthorizedAccessException.class,
                () -> loginService.login(loginRequest)
        );
    }
}
