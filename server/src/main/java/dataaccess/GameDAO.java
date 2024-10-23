package dataaccess;

import model.GameData;

import java.util.List;

public interface GameDAO {
    void clearGames() throws DataAccessException;

    List<GameData> listGames() throws DataAccessException;
}
