package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import request.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import response.ErrorResponse;
import response.LoginResponse;
import response.LogoutResponse;

public class LogoutServiceTest {
    @Test
    @Order(1)
    @DisplayName("Logout user positive")
    public void successfulUserLogin() {
        // Set up DAO
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        // Register a user
        RegisterService registerService = new RegisterService(authDAO, userDAO);
        RegisterRequest registerRequest = new RegisterRequest("lolcats", "passw00rd", "foo@bar.baz");
        registerService.register(registerRequest);

        // Login the registered user
        LoginService loginService = new LoginService(authDAO, userDAO);
        LoginRequest loginRequest = new LoginRequest("lolcats", "passw00rd");
        DataTransfer<?> loginResponse = loginService.login(loginRequest);

        // Logout the now logged-in user
        LogoutService logoutService = new LogoutService(authDAO);
        if (loginResponse.data() instanceof LoginResponse) {
            LogoutRequest logoutRequest = new LogoutRequest(((LoginResponse) loginResponse.data()).authToken());
            DataTransfer result = logoutService.logout(logoutRequest);
            Assertions.assertInstanceOf(LogoutResponse.class, result.data());
        }
        // User not logged in
        Assertions.fail();
    }


    @Test
    @Order(2)
    @DisplayName("Logout user negative")
    public void unsuccessfulUserLogin() {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        RegisterService registerService = new RegisterService(authDAO, userDAO);
        RegisterRequest registerRequest = new RegisterRequest("lolcats", "passw00rd", "foo@bar.baz");
        registerService.register(registerRequest);
        LoginService loginService = new LoginService(authDAO, userDAO);
        LoginRequest loginRequest = new LoginRequest("lolcats", "pwrd");
        DataTransfer result = loginService.login(loginRequest);
        Assertions.assertInstanceOf(ErrorResponse.class, result.data());
    }
}

