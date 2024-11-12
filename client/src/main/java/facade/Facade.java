package facade;

import request.*;
import response.*;

public interface Facade {
    RegisterResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
    LogoutResponse logout(LogoutRequest logoutRequest);
    ListGamesResponse listGames(ListGamesRequest listGamesRequest);
    CreateGameResponse createGame(CreateGameRequest request);
    JoinGameResponse joinGame(JoinGameRequest joinGameRequest);
}
