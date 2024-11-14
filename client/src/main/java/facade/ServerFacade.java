package facade;

import chess.ChessGame;
import client.request.*;
import client.response.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

public class ServerFacade implements Facade {
    private String serverAddress = "localhost";
    private int port = 8080;
    private final Gson gson = new Gson();

    public ServerFacade() {}

    public ServerFacade(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public ServerFacade(int port) {
        this.port = port;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        // Convert the RegisterRequest object to JSON
        String jsonBody = gson.toJson(registerRequest);

        // Make the POST request to the /user endpoint
        HttpResponse<String> response = makeWebRequest("POST", "/user", jsonBody, null);

        // Handle the response based on its status code
        return switch (response.statusCode()) {
            case 200 -> {
                // Parse the success response
                var successResponse = gson.fromJson(response.body(), RegisterResponse.class);
                yield new RegisterResponse(true, "", successResponse.username(), successResponse.authToken());
            }
            case 400 -> new RegisterResponse(false, "Bad request", null, null);
            case 403 -> new RegisterResponse(false, "Username already taken", null, null);
            case 500 -> new RegisterResponse(false, "500 Error: " + response.body(), null, null);
            default -> new RegisterResponse(false, "An unexpected error occurred", null, null);
        };
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // Convert the LoginRequest object to JSON
        String jsonBody = gson.toJson(loginRequest);

        // Make the POST request to the /session endpoint
        HttpResponse<String> response = makeWebRequest("POST", "/session", jsonBody, null);

        // Handle the response based on its status code
        return switch (response.statusCode()) {
            case 200 -> {
                // Parse the success response
                var successResponse = gson.fromJson(response.body(), LoginResponse.class);
                yield new LoginResponse(true, "", successResponse.username(), successResponse.authToken());
            }
            case 401 -> new LoginResponse(false, "Error: unauthorized", null, null);
            case 500 -> new LoginResponse(false, "Error: " + response.body(), null, null);
            default -> new LoginResponse(false, "An unexpected error occurred", null, null);
        };
    }

    @Override
    public LogoutResponse logout(LogoutRequest logoutRequest) {
        HttpResponse<String> response = makeWebRequest("DELETE", "/session", null, logoutRequest.authToken());
        if (response.statusCode() == 200) {
            return new LogoutResponse(true,"");
        } else if (response.statusCode() == 401) {
            return new LogoutResponse(false,"Authorization invalid");
        }
        return new LogoutResponse(false,"An error occurred connecting to the server");
    }

    @Override
    public ListGamesResponse listGames(ListGamesRequest listGamesRequest) {
        // Make the GET request to the /game endpoint with the authToken
        HttpResponse<String> response = makeWebRequest("GET", "/game", null, listGamesRequest.authToken());

        // Handle the response based on its status code
        return switch (response.statusCode()) {
            case 200 -> {
                // Parse the success response
                var successResponse = gson.fromJson(response.body(), ListGamesResponse.class);
                yield new ListGamesResponse(true, "", successResponse.games());
            }
            case 401 -> new ListGamesResponse(false, "Error: unauthorized", null);
            case 500 -> new ListGamesResponse(false, "Error: " + response.body(), null);
            default -> new ListGamesResponse(false, "An unexpected error occurred", null);
        };
    }

    @Override
    public CreateGameResponse createGame(CreateGameRequest createGameRequest) {
        // Convert the CreateGameRequest object to JSON
        String jsonBody = gson.toJson(createGameRequest);

        // Make the POST request to the /game endpoint with the authToken
        HttpResponse<String> response = makeWebRequest("POST", "/game", jsonBody, createGameRequest.authToken());

        // Handle the response based on its status code
        return switch (response.statusCode()) {
            case 200 -> {
                // Parse the success response
                var successResponse = gson.fromJson(response.body(), CreateGameResponse.class);
                yield new CreateGameResponse(true, "", successResponse.gameID());
            }
            case 400 -> new CreateGameResponse(false, "Error: bad request", -1);
            case 401 -> new CreateGameResponse(false, "Error: unauthorized", -1);
            case 500 -> new CreateGameResponse(false, "Error: " + response.body(), -1);
            default -> new CreateGameResponse(false, "An unexpected error occurred", -1);
        };
    }

    @Override
    public JoinGameResponse joinGame(JoinGameRequest joinGameRequest) {
        // Convert the JoinGameRequest object to JSON
        String jsonBody = gson.toJson(joinGameRequest);

        // Make the PUT request to the /game endpoint with the authToken
        HttpResponse<String> response = makeWebRequest("PUT", "/game", jsonBody, joinGameRequest.authToken());

        // Handle the response based on its status code
        return switch (response.statusCode()) {
            case 200 -> new JoinGameResponse(true, ""); // Success with no error message
            case 400 -> new JoinGameResponse(false, "Error: bad request");
            case 401 -> new JoinGameResponse(false, "Error: unauthorized");
            case 403 -> new JoinGameResponse(false, "Error: color already taken");
            case 500 -> new JoinGameResponse(false, "Error: " + response.body());
            default -> new JoinGameResponse(false, "An unexpected error occurred");
        };
    }

    public HttpResponse<String> makeWebRequest(String method, String endpoint, String jsonBody, String authToken) {
        try {
            // Build URI from server address, port, and endpoint
            URI uri = new URI("http", null, serverAddress, port, endpoint, null, null);

            // Initialize HttpClient
            HttpClient client = HttpClient.newBuilder()
                    .build();

            // Build HttpRequest with appropriate HTTP method and headers
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json");

            // Add Authorization header if authToken is provided
            if (authToken != null) {
                requestBuilder.header("Authorization", authToken);
            }

            // Configure request method and body if applicable
            switch (method.toUpperCase()) {
                case "POST":
                    if (jsonBody != null) {
                        requestBuilder.POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8));
                    } else {
                        requestBuilder.POST(HttpRequest.BodyPublishers.noBody());
                    }
                    break;
                case "PUT":
                    if (jsonBody != null) {
                        requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8));
                    } else {
                        requestBuilder.PUT(HttpRequest.BodyPublishers.noBody());
                    }
                    break;
                case "DELETE":
                    requestBuilder.DELETE();
                    break;
                case "GET":
                default:
                    requestBuilder.GET();
                    break;
            }

            // Send the request and return the response
            HttpRequest request = requestBuilder.build();
            HttpResponse<String> stringHttpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            return stringHttpResponse;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException("Error making web request", e);
        }
    }

    public int getPort() {
        return port;
    }

    public String getServerAddress() {
        return serverAddress;
    }
}
