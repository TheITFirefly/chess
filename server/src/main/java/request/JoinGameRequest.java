package request;

public record JoinGameRequest(String authToken, JoinGameRequestBody body) {}
