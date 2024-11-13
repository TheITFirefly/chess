package dataaccess;

import errors.DataAccessException;
import model.GameData;

import java.util.List;

public interface GameDAO {
    void clearGames() throws DataAccessException;

    List<GameData> listGames() throws DataAccessException;
    void createGame(GameData gameData) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void updateGame(GameData gameData) throws DataAccessException;
}
