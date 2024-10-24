package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.CreateGameRequestBody;
import request.DataTransfer;
import response.CreateGameResponse;
import response.ErrorResponse;

public class CreateGameServiceTest {

    @Test
    @Order(1)
    @DisplayName("Create game positive")
    public void createGamePositive() throws Exception {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
        authDAO.createAuth(new AuthData("validToken", "player1"));
        CreateGameRequestBody requestBody = new CreateGameRequestBody("Epic Chess Match");
        CreateGameRequest request = new CreateGameRequest("validToken", requestBody);
        DataTransfer<?> result = createGameService.createGame(request);
        Assertions.assertInstanceOf(CreateGameResponse.class, result.data());
        CreateGameResponse response = (CreateGameResponse) result.data();
        Assertions.assertEquals(1, response.gameID());  // First game, so ID should be 1
        GameData createdGame = gameDAO.getGame(1);
        Assertions.assertNotNull(createdGame);  // Ensure the game exists
        Assertions.assertEquals("Epic Chess Match", createdGame.gameName());  // Verify the game name
    }

    @Test
    @Order(2)
    @DisplayName("Create game negative - Invalid Auth Token")
    public void createGameNegative() throws Exception {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
        CreateGameRequestBody requestBody = new CreateGameRequestBody("Invalid Game");
        CreateGameRequest request = new CreateGameRequest("invalidToken", requestBody);
        DataTransfer<?> result = createGameService.createGame(request);
        Assertions.assertInstanceOf(ErrorResponse.class, result.data());
        ErrorResponse errorResponse = (ErrorResponse) result.data();
        Assertions.assertEquals("Error: unauthorized", errorResponse.message());  // Check error type
    }
}
