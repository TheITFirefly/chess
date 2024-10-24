package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import request.DataTransfer;
import request.ListGamesRequest;
import response.ErrorResponse;
import response.ListGamesResponse;

import java.util.Arrays;
import java.util.List;

public class ListGamesServiceTest {

    @Test
    @Order(1)
    @DisplayName("List games positive")
    public void listGamesPositive() throws Exception {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        ListGamesService listGamesService = new ListGamesService(authDAO, gameDAO);
        authDAO.createAuth(new AuthData("validToken", "player1"));
        gameDAO.createGame(new GameData(1, "player1", "player2", "Game 1", null));
        gameDAO.createGame(new GameData(2, "player3", null, "Game 2", null));
        ListGamesRequest request = new ListGamesRequest("validToken");
        DataTransfer<?> result = listGamesService.listGames(request);
        Assertions.assertInstanceOf(ListGamesResponse.class, result.data());
        ListGamesResponse response = (ListGamesResponse) result.data();
        List<GameData> games = response.games();
        Assertions.assertEquals(2, games.size());  // Verify the correct number of games
        Assertions.assertEquals("Game 1", games.get(0).gameName());
        Assertions.assertEquals("Game 2", games.get(1).gameName());
    }

    @Test
    @Order(2)
    @DisplayName("List games negative - Unauthorized")
    public void listGamesNegative() throws Exception {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        ListGamesService listGamesService = new ListGamesService(authDAO, gameDAO);
        ListGamesRequest request = new ListGamesRequest("invalidToken");
        DataTransfer<?> result = listGamesService.listGames(request);
        Assertions.assertInstanceOf(ErrorResponse.class, result.data());
        ErrorResponse errorResponse = (ErrorResponse) result.data();
        Assertions.assertEquals("Error: unauthorized", errorResponse.message());
    }
}
