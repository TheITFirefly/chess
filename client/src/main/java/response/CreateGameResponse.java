package response;

public record CreateGameResponse(boolean success, String errorMessage, int gameID) {}
