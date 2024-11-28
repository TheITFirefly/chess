package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class TestBoardPrinter {
    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        BoardPrinter printer = new BoardPrinter();
        ChessPosition newQueenPos = new ChessPosition(4,4);
        ChessPosition badPos = new ChessPosition(2,4);
        ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        game.getBoard().addPiece(newQueenPos,whiteQueen);
        var highlighted = printer.highlightLegalMoves(game,badPos);
        printer.renderBoard(highlighted, ChessGame.TeamColor.WHITE);
    }
}
