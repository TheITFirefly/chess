package websocket.messages;

import chess.ChessGame;
import chess.ChessMove;

public class LoadGameMessage extends ServerMessage {
    public ChessGame game;
    public ChessMove move = null;

    public LoadGameMessage(ChessGame game, ChessMove move) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.move = move;
    }
}
