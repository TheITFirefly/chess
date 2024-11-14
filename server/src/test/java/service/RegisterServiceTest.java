package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import errors.DataAccessException;
import errors.DuplicateEntryException;
import response.ErrorResponse;
import response.RegisterResponse;
import request.RegisterRequest;
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

        // Create a RegisterRequest with valid, unique details
        RegisterRequest request = new RegisterRequest("lolcats", "passw00rd", "foo@bar.baz");

        // Perform the registration
        Assertions.assertDoesNotThrow(() -> {
            RegisterResponse response = service.register(request);
        });
    }

    @Test
    @Order(2)
    @DisplayName("Register user negative - already taken")
    public void registerNegative() throws DuplicateEntryException, DataAccessException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        RegisterService service = new RegisterService(authDAO, userDAO);
        RegisterRequest request = new RegisterRequest("lolcats", "passw00rd", "foo@bar.baz");
        // First registration should succeed
        service.register(request);
        // Second registration with the same username should throw an exception
        Assertions.assertThrows(
                DuplicateEntryException.class,
                () -> service.register(request),
                "Expected a DuplicateEntryException due to username being already taken"
        );
    }
}
