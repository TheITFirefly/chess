package dataaccess;

import chess.ChessGame;
import errors.DataAccessException;
import model.GameData;
import server.model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseGameDAOTest {

    private DatabaseUserDAO userDAO;
    private DatabaseGameDAO gameDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO = new DatabaseUserDAO();
        gameDAO = new DatabaseGameDAO();
        userDAO.clearUsers();
        gameDAO.clearGames();
        userDAO.createUser(new UserData("whitePlayer", "password1", "white@example.com"));
        userDAO.createUser(new UserData("blackPlayer", "password2", "black@example.com"));
    }

    @Test
    @Order(1)
    @DisplayName("Clear Games Positive")
    public void clearGamesPositive() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(1, "whitePlayer", "blackPlayer", "Game 1", chessGame);
        gameDAO.createGame(gameData);
        gameDAO.clearGames();
        assertTrue(gameDAO.listGames().isEmpty(), "Games table should be empty after clearing.");
    }

    @Test
    @Order(2)
    @DisplayName("Create Game Positive")
    public void createGamePositive() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(2, "whitePlayer", "blackPlayer", "Game 2", chessGame);
        gameDAO.createGame(gameData);

        GameData retrievedGame = gameDAO.getGame(2);
        assertNotNull(retrievedGame, "Created game should be retrievable.");
        assertEquals(gameData.gameID(), retrievedGame.gameID(), "Game ID should match.");
        assertEquals(gameData.whiteUsername(), retrievedGame.whiteUsername(), "White username should match.");
        assertEquals(gameData.blackUsername(), retrievedGame.blackUsername(), "Black username should match.");
    }

    @Test
    @Order(3)
    @DisplayName("Create Game Negative")
    public void createGameNegative() {
        ChessGame chessGame = new ChessGame();
        GameData invalidGameData = new GameData(3, "invalidWhite", "blackPlayer", "Game 3", chessGame);
        DataAccessException thrown = assertThrows(DataAccessException.class, () -> gameDAO.createGame(invalidGameData));
        assertTrue(thrown.getMessage().contains("Error inserting"), "Exception should indicate insertion error due to invalid user.");
    }

    @Test
    @Order(4)
    @DisplayName("List Games Positive")
    public void listGamesPositive() throws DataAccessException {
        ChessGame game1 = new ChessGame();
        ChessGame game2 = new ChessGame();
        gameDAO.createGame(new GameData(4, "whitePlayer", "blackPlayer", "Game 4", game1));
        gameDAO.createGame(new GameData(5, "whitePlayer", "blackPlayer", "Game 5", game2));
        List<GameData> games = gameDAO.listGames();
        assertEquals(2, games.size(), "Two games should be listed.");
    }

    @Test
    @Order(5)
    @DisplayName("List Games Negative")
    public void listGamesNegative() throws DataAccessException {
        List<GameData> games = gameDAO.listGames();
        assertTrue(games.isEmpty(), "Listing games should return an empty list if no games exist.");
    }

    @Test
    @Order(6)
    @DisplayName("Get Game Positive")
    public void getGamePositive() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(6, "whitePlayer", "blackPlayer", "Game 6", chessGame);
        gameDAO.createGame(gameData);
        GameData retrievedGame = gameDAO.getGame(6);
        assertNotNull(retrievedGame, "Game should be retrieved by ID.");
        assertEquals(gameData.gameID(), retrievedGame.gameID(), "Game ID should match.");
    }

    @Test
    @Order(7)
    @DisplayName("Get Game Negative")
    public void getGameNegative() throws DataAccessException {
        assertNull(gameDAO.getGame(999), "Retrieving non-existent game should return null.");
    }

    @Test
    @Order(8)
    @DisplayName("Update Game Positive")
    public void updateGamePositive() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(7, "whitePlayer", "blackPlayer", "Initial Game", chessGame);
        gameDAO.createGame(gameData);
        GameData updatedGameData = new GameData(7, "whitePlayer", "blackPlayer", "Updated Game", chessGame);
        gameDAO.updateGame(updatedGameData);
        GameData retrievedGame = gameDAO.getGame(7);
        assertEquals("Updated Game", retrievedGame.gameName(), "Game name should be updated.");
    }

    @Test
    @Order(9)
    @DisplayName("Update Game Negative")
    public void updateGameNegative() {
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(8, "whitePlayer", "blackPlayer", "Non-Existent Game", chessGame);
        DataAccessException thrown = assertThrows(DataAccessException.class, () -> gameDAO.updateGame(gameData));
        assertTrue(thrown.getMessage().contains("No game found with gameID"), "Exception should indicate missing game ID.");
    }
}
