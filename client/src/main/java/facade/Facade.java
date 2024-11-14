package facade;

import client.request.*;
import client.response.*;

public interface Facade {
    RegResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
    LogoutResponse logout(LogoutRequest logoutRequest);
    ListGamesResponse listGames(ListGamesRequest listGamesRequest);
    CreateGameResponse createGame(CreateGameRequest request);
    JoinGameResponse joinGame(JoinGameRequest joinGameRequest);
}
