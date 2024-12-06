package facade;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.request.*;
import client.response.*;
import facade.communicator.*;
import websocket.commands.UserGameCommand;

public class ServerFacade {
    private String serverAddress = "localhost";
    private int port = 8080;
    WebSocketCommunicator ws;
    ChessGame.TeamColor playerColor;

    public ServerFacade() {}

    // Getters
    public String getServerAddress() {return serverAddress;}
    public int getPort() {return port;}

    // Setters
    public void setServerAddress(String serverAddress) {this.serverAddress=serverAddress;}
    public void setServerPort(int port){this.port=port;}

    public RegisterResponse register(RegisterRequest registerRequest) {
        HttpCommunicator httpCommunicator = new HttpCommunicator(serverAddress,port);
        return httpCommunicator.register(registerRequest);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        HttpCommunicator httpCommunicator = new HttpCommunicator(serverAddress,port);
        return httpCommunicator.login(loginRequest);
    }

    public LogoutResponse logout(LogoutRequest logoutRequest) {
        HttpCommunicator httpCommunicator = new HttpCommunicator(serverAddress,port);
        return httpCommunicator.logout(logoutRequest);
    }

    public ListGamesResponse listGames(ListGamesRequest listGamesRequest) {
        HttpCommunicator httpCommunicator = new HttpCommunicator(serverAddress,port);
        return httpCommunicator.listGames(listGamesRequest);
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) {
        HttpCommunicator httpCommunicator = new HttpCommunicator(serverAddress,port);
        return httpCommunicator.createGame(createGameRequest);
    }

    public JoinGameResponse joinGame(JoinGameRequest joinGameRequest) {
        HttpCommunicator httpCommunicator = new HttpCommunicator(serverAddress,port);
        return httpCommunicator.joinGame(joinGameRequest);
    }

    public void sendMessage(UserGameCommand command){
        ws.send(command);
    }


    public void printBoard(ChessGame.TeamColor playerColor) {
        ws.highlightBoard(playerColor, null);
    }

    public void highlightBoard(ChessGame.TeamColor playerColor, ChessPosition position){
        ws.highlightBoard(playerColor,position);
    }

    public void openWebsocket() {
        ws = new WebSocketCommunicator(serverAddress,port,playerColor);
    }

    public void setPlayerColor(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }
}
