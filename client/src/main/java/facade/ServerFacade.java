package facade;

import request.*;
import response.*;

import java.net.http.*;

public class ServerFacade implements Facade {
    private String serverAddress = "localhost";
    private final int port;

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
        throw new RuntimeException("Not implemented");
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
}
