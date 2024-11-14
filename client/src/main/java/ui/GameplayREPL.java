package ui;

import chess.ChessGame;
import facade.ServerFacade;

public class GameplayREPL {
    ServerFacade facade;
    String authToken;
    ChessGame.TeamColor playerColor;

    public GameplayREPL(ServerFacade facade,String authToken, String playerColor) {
        this.facade = facade;
        this.authToken = authToken;
        this.playerColor = playerColor.equals("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
    }

    public void run() {
        System.out.println("Gameplay not yet implemented");
        // Also print out the initial board
    }
}
