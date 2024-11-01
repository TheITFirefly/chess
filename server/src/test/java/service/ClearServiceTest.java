package service;

import dataaccess.DatabaseUserDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import response.ClearResponse;
import request.DataTransfer;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClearServiceTest {
    @Test
    @Order(1)
    @DisplayName("Clear memory positive")
    public void memoryClearPositive(){
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        ClearService service = new ClearService(authDAO,userDAO,gameDAO);
        DataTransfer result = service.clearDatabase();
        Assertions.assertInstanceOf(ClearResponse.class, result.data());

    }

    @Test
    @Order(2)
    @DisplayName("Clear database positive")
    public void databaseClearPositive(){
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        ClearService service = new ClearService(authDAO,userDAO,gameDAO);
        DataTransfer result = service.clearDatabase();
        Assertions.assertInstanceOf(ClearResponse.class, result.data());

    }
}
