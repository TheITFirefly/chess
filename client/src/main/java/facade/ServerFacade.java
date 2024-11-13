package facade;

import com.google.gson.Gson;
import request.*;
import response.*;

import java.io.IOException;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

public class ServerFacade implements Facade {
    private String serverAddress = "localhost";
    private final int port;
    private final Gson gson = new Gson();

    public ServerFacade(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public ServerFacade(int port) {
        this.port = port;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        throw new RuntimeException("Not implemented");
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
        throw new RuntimeException("Not implemented");
    }

    @Override
    public CreateGameResponse createGame(CreateGameRequest request) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public JoinGameResponse joinGame(JoinGameRequest joinGameRequest) {
        throw new RuntimeException("Not implemented");
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
                requestBuilder.header("Authorization", "Bearer " + authToken);
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
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException("Error making web request", e);
        }
    }

}
