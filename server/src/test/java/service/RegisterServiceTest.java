package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import datatransfer.ErrorResponse;
import datatransfer.RegisterResponse;
import datatransfer.DataTransfer;
import datatransfer.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class RegisterServiceTest {
    @Test
    @Order(1)
    @DisplayName("Register user positive")
    public void successfulUserRegistration() {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        RegisterService service = new RegisterService(authDAO, userDAO);
        RegisterRequest request = new RegisterRequest("lolcats", "passw00rd", "foo@bar.baz");
        DataTransfer result = service.register(request);
        Assertions.assertInstanceOf(RegisterResponse.class, result.data());
    }


    @Test
    @Order(2)
    @DisplayName("Register user positive")
    public void repeatUserRegistration() {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        RegisterService service = new RegisterService(authDAO, userDAO);
        RegisterRequest request = new RegisterRequest("lolcats", "passw00rd", "foo@bar.baz");
        service.register(request);
        DataTransfer result = service.register(request);
        Assertions.assertInstanceOf(ErrorResponse.class, result.data());
    }
}