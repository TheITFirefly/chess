package client.response;

public record RegisterResponse(boolean success, String errorMessage, String username, String authToken) {}
