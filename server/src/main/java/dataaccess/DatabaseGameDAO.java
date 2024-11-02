package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseGameDAO implements GameDAO{
    @Override
    public void clearGames() throws DataAccessException {
        try {
            initializeGameTable();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed during table initialization: " + e.getMessage());
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(
                    "DELETE FROM gamedata;")) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing userdata table: " + e.getMessage());
        }
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        List<GameData> gameTable = new ArrayList<>();
        try {
            initializeGameTable();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed during table initialization: " + e.getMessage());
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement("SELECT * FROM gamedata")) {
                try (var rs = stmt.executeQuery()) {
                    Gson gson = new Gson();
                    while (rs.next()) {
                        int gameId = rs.getInt("gameid");
                        String whiteUsername = rs.getString("whiteusername");
                        String blackUsername = rs.getString("blackusername");
                        String gameName = rs.getString("gamename");
                        String gameJson = rs.getString("game");
                        ChessGame game = gson.fromJson(gameJson, ChessGame.class);

                        GameData gameData = new GameData(gameId, whiteUsername, blackUsername, gameName, game);
                        gameTable.add(gameData);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving games from gamedata table: " + e.getMessage());
        }

        return gameTable;
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        try {
            initializeGameTable();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed during table initialization: " + e.getMessage());
        }
        Gson gson = new Gson();
        ChessGame game = gameData.game();
        String gameJson = gson.toJson(game);
        try (var conn = DatabaseManager.getConnection()) {
            try (var insertStmt = conn.prepareStatement("INSERT INTO gamedata (gameid,whiteusername,blackusername,gamename,game) VALUES (?, ?, ?, ?, ?)")) {
                insertStmt.setString(1, gameData.gameID().toString());
                insertStmt.setString(2, gameData.whiteUsername());
                insertStmt.setString(3, gameData.blackUsername());
                insertStmt.setString(4, gameData.gameName());
                insertStmt.setString(5, gameJson);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting user into userdata table: " + e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try {
            initializeGameTable();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed during table initialization: " + e.getMessage());
        }

        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement("SELECT * FROM gamedata WHERE gameid = ?")) {
                stmt.setInt(1, gameID);
                try (var rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Gson gson = new Gson();
                        String whiteUsername = rs.getString("whiteusername");
                        String blackUsername = rs.getString("blackusername");
                        String gameName = rs.getString("gamename");
                        String gameJson = rs.getString("game");
                        ChessGame game = gson.fromJson(gameJson, ChessGame.class);

                        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving game from gamedata table: " + e.getMessage());
        }
    }


    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        try {
            initializeGameTable();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed during table initialization: " + e.getMessage());
        }

        Gson gson = new Gson();
        String gameJson = gson.toJson(gameData.game());

        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(
                    "UPDATE gamedata SET whiteusername = ?, blackusername = ?, gamename = ?, game = ? WHERE gameid = ?")) {
                stmt.setString(1, gameData.whiteUsername());
                stmt.setString(2, gameData.blackUsername());
                stmt.setString(3, gameData.gameName());
                stmt.setString(4, gameJson);
                stmt.setInt(5, gameData.gameID());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new DataAccessException("No game found with gameID: " + gameData.gameID());
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error updating game in gamedata table: " + e.getMessage());
        }
    }

    private void initializeGameTable() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS userdata (" +
                            "username VARCHAR(255) PRIMARY KEY, " +
                            "password VARCHAR(255) NOT NULL, " +
                            "email VARCHAR(255) NOT NULL);")) {
                stmt.executeUpdate();
            }
            try (var stmt = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS gamedata (" +
                            "gameid INT PRIMARY KEY, " +
                            "whiteusername VARCHAR(255), " +
                            "blackusername VARCHAR(255), " +
                            "gamename VARCHAR(255) NOT NULL, " +
                            "game JSON," +
                            "FOREIGN KEY (whiteusername) REFERENCES userdata(username) ON DELETE SET NULL, " +
                            "FOREIGN KEY (blackusername) REFERENCES userdata(username) ON DELETE SET NULL);")) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error initializing auth table: " + e.getMessage());
        }
    }
}
