package client;

import client.request.*;
import client.request.JoinGameRequest;
import client.response.CreateGameResponse;
import client.response.JoinGameResponse;
import client.response.ListGamesResponse;
import client.response.LoginResponse;
import client.response.LogoutResponse;
import client.response.RegisterResponse;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;

import server.Server;
import facade.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clear(){
        try {
            URI uri = new URI("http",null,facade.getServerAddress(), facade.getPort(), "/db",null,null);
            try (HttpClient client = HttpClient.newHttpClient()) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(uri)
                        .DELETE()
                        .build();
                HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
                Assertions.assertEquals(200,response.statusCode());
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Register user positive with facade")
    public void registerPositive() {
        RegisterResponse registerResponse = facade.register(new RegisterRequest("lolcats", "passw00rd", "foo@bar.baz"));
        Assertions.assertTrue(registerResponse.success());
    }

    @Test
    @Order(2)
    @DisplayName("Register user negative with facade")
    public void registerNegative() {
        facade.register(new RegisterRequest("lolcats", "passw00rd", "foo@bar.baz"));
        RegisterResponse registerResponse = facade.register(new RegisterRequest("lolcats", "passw00rd", "foo@bar.baz"));
        Assertions.assertFalse(registerResponse.success());
    }

    @Test
    @Order(3)
    @DisplayName("Login user positive with facade")
    public void loginPositive() {
        facade.register(new RegisterRequest("lolcats", "passw00rd", "foo@bar.baz"));
        LoginResponse loginResponse = facade.login(new LoginRequest("lolcats", "passw00rd"));
        Assertions.assertTrue(loginResponse.success());
    }

    @Test
    @Order(4)
    @DisplayName("Login user negative with facade")
    public void loginNegative() {
        LoginResponse loginResponse = facade.login(new LoginRequest("lolcats", "passw00rd"));
        Assertions.assertFalse(loginResponse.success());
    }

    @Test
    @Order(5)
    @DisplayName("Logout user positive with facade")
    public void logoutPositive() {
        facade.register(new RegisterRequest("lolcats", "passw00rd", "foo@bar.baz"));
        String authToken = facade.login(new LoginRequest("lolcats", "passw00rd")).authToken();
        LogoutResponse logoutResponse = facade.logout(new LogoutRequest(authToken));
        Assertions.assertTrue(logoutResponse.success());
    }

    @Test
    @Order(6)
    @DisplayName("Logout user negative with facade")
    public void logoutNegative() {
        LogoutResponse logoutResponse = facade.logout(new LogoutRequest("Fakeauthtoken"));
        Assertions.assertFalse(logoutResponse.success());
    }

    @Test
    @Order(7)
    @DisplayName("List games positive with facade")
    public void listGamesPositive() {
        facade.register(new RegisterRequest("username","password","email@example.com"));
        String authToken = facade.login(new LoginRequest("username","password")).authToken();
        ListGamesResponse listGamesResponse = facade.listGames(new ListGamesRequest(authToken));
        Assertions.assertTrue(listGamesResponse.success());
    }

    @Test
    @Order(8)
    @DisplayName("List games negative with facade")
    public void listGamesNegative() {
        ListGamesResponse listGamesResponse = facade.listGames(new ListGamesRequest("fake auth token"));
        Assertions.assertFalse(listGamesResponse.success());
    }

    @Test
    @Order(9)
    @DisplayName("Create game positive with facade")
    public void createGamePositive() {
        facade.register(new RegisterRequest("username","password","email@example.com"));
        String authToken = facade.login(new LoginRequest("username","password")).authToken();
        CreateGameResponse createGameResponse = facade.createGame(new CreateGameRequest(authToken,"woot"));
        Assertions.assertTrue(createGameResponse.success());
    }

    @Test
    @Order(10)
    @DisplayName("Create game negative with facade")
    public void createGameNegative() {
        CreateGameResponse createGameResponse = facade.createGame(new CreateGameRequest("lol bad auth","get played"));
        Assertions.assertFalse(createGameResponse.success());
    }

    @Test
    @Order(11)
    @DisplayName("Join game positive with facade")
    public void joinGamePositive() {
        facade.register(new RegisterRequest("username","password","email@example.com"));
        String authToken = facade.login(new LoginRequest("username","password")).authToken();
        int gameID = facade.createGame(new CreateGameRequest(authToken,"woot")).gameID();
        JoinGameResponse joinGameResponse = facade.joinGame(new JoinGameRequest(authToken,"BLACK",gameID));
        Assertions.assertTrue(joinGameResponse.success());
    }

    @Test
    @Order(12)
    @DisplayName("Join game negative with facade")
    public void joinGameNegative() {
        JoinGameResponse joinGameResponse = facade.joinGame(new JoinGameRequest("Big oof here","BLACK",2));
        Assertions.assertFalse(joinGameResponse.success());
    }
}
