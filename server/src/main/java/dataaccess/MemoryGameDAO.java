package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    List<GameData> gameTable = new ArrayList<>();
    public void clearGames() throws DataAccessException{
        try {
            gameTable.clear();
        } catch (Exception e) {
            throw new DataAccessException("Error: Failed clearing game table");
        }
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        try {
            return gameTable;
        } catch (Exception e) {
            throw new DataAccessException("Error: Couldn't contact database");
        }
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        try {
            gameTable.add(gameData);
        } catch (Exception e) {
            throw new DataAccessException("Error: Couldn't contact database");
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try {
            for (GameData gameData : gameTable) {
                if (gameData.gameID() == gameID) {
                    return gameData;
                }
            }
            return null;
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        try {
            for (int i = 0; i < gameTable.size(); i++) {
                if (Objects.equals(gameTable.get(i).gameID(), gameData.gameID())) {
                    gameTable.set(i, gameData);
                    return;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
