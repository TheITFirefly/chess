package service;

import chess.*;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import datatransfer.DataTransfer;
import org.junit.jupiter.api.*;
import spark.Response;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClearServiceTest {
    @Test
    @Order(1)
    @DisplayName("Clear database positive")
    public void databaseClear(){
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        ClearService service = new ClearService(authDAO,userDAO,gameDAO);
        DataTransfer result = service.clearDatabase();
        Assertions.assertEquals("Success",result.status());
    }
}
