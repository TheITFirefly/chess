package ui;

import chess.*;
import facade.ServerFacade;

public class GameplayREPL {
    ServerFacade facade;
    String authToken;
    ChessGame game = new ChessGame();
    ChessGame.TeamColor playerColor;

    public GameplayREPL(ServerFacade facade,String authToken, ChessGame game, String playerColor) {
        this.facade = facade;
        this.authToken = authToken;
        this.playerColor = playerColor.equals("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        this.game = game;
    }

    public void run() {
        System.out.println("Gameplay not yet implemented");
        // Also print out the initial board with both colors
        BoardPrinter boardPrinter = new BoardPrinter();
        boardPrinter.printBoard(game.getBoard(), ChessGame.TeamColor.WHITE);
    }

    public void updateGame(ChessGame game) {
        this.game = game;
    }
}
