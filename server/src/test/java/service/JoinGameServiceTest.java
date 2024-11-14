package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import server.model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import request.DataTransfer;
import request.JoinGameRequest;
import request.JoinGameRequestBody;
import response.JoinGameResponse;
import response.TakenErrorResponse;

public class JoinGameServiceTest {

    @Test
    @Order(1)
    @DisplayName("Join game positive")
    public void joinGamePositive() throws Exception {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        JoinGameService joinGameService = new JoinGameService(authDAO, gameDAO);
        authDAO.createAuth(new AuthData("validToken", "player1"));  // Simulate user login
        gameDAO.createGame(new GameData(1, null, "player2", "Test Game", null));  // Game with one spot open
        JoinGameRequestBody requestBody = new JoinGameRequestBody("WHITE",1);
        JoinGameRequest request = new JoinGameRequest("validToken", requestBody);
        DataTransfer<?> result = joinGameService.joinGame(request);
        Assertions.assertInstanceOf(JoinGameResponse.class, result.data());
        GameData updatedGame = gameDAO.getGame(1);
        Assertions.assertEquals("player1", updatedGame.whiteUsername());  // Check that player1 joined as white
        Assertions.assertEquals("player2", updatedGame.blackUsername());  // Ensure other player remains
    }

    @Test
    @Order(2)
    @DisplayName("Join game negative - Color already taken")
    public void joinGameNegative() throws Exception {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        JoinGameService joinGameService = new JoinGameService(authDAO, gameDAO);
        authDAO.createAuth(new AuthData("validToken", "player1"));
        gameDAO.createGame(new GameData(2, "player3", "player2", "Test Game", null));  // Both colors taken
        JoinGameRequestBody requestBody = new JoinGameRequestBody("WHITE",2);
        JoinGameRequest request = new JoinGameRequest("validToken", requestBody);
        DataTransfer<?> result = joinGameService.joinGame(request);
        Assertions.assertInstanceOf(TakenErrorResponse.class, result.data());
    }
}
