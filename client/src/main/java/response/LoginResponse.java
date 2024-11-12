package response;

public record LoginResponse(boolean success, String errorMessage, String username, String authToken) {}
