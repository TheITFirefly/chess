package ui;

import chess.*;

public class TestBoardPrinter {
    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        BoardPrinter printer = new BoardPrinter();
        ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        var highlighted = printer.highlightLegalMoves(game,new ChessPosition(2,2));
        printer.renderBoard(highlighted, ChessGame.TeamColor.WHITE);
        System.out.println();
        ChessBoard origBoard = new ChessBoard();
        origBoard.resetBoard();
        try{
            game.makeMove(new ChessMove(new ChessPosition(2,2),new ChessPosition(4,2),null));
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        ChessBoard newBoard = game.getBoard();
        var changed = printer.highlightBoardDifferences(origBoard,newBoard);
        printer.renderBoard(changed, ChessGame.TeamColor.WHITE);
        System.out.println();
        highlighted = printer.highlightLegalMoves(game,new ChessPosition(2,2));
        printer.renderBoard(highlighted, ChessGame.TeamColor.WHITE);
        System.out.println();
    }
}
