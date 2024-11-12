package response;

import model.GameData;

import java.util.List;

public record ListGamesResponse(boolean success, String errorMessage, List<GameData> games) {}
