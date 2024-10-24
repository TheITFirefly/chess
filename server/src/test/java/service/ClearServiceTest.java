package service;

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
    @DisplayName("Clear database positive")
    public void databaseClearPositive(){
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        ClearService service = new ClearService(authDAO,userDAO,gameDAO);
        DataTransfer result = service.clearDatabase();
        Assertions.assertInstanceOf(ClearResponse.class, result.data());

    }
}
