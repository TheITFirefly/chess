package client.response;

public record RegResponse(boolean success, String errorMessage, String username, String authToken) {}
