package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    List<GameData> gameTable = new ArrayList<>();
    public void clearGames() throws DataAccessException{
        try {
            gameTable.clear();
        } catch (Exception e) {
            throw new DataAccessException("Failed clearing game table");
        }
    }
}
