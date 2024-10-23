package request;

public record CreateGameRequest(String authToken, CreateGameRequestBody body) {}
