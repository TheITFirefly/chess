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
        System.out.println();
        highlighted = printer.highlightLegalMoves(game,new ChessPosition(2,2));
        printer.renderBoard(highlighted, ChessGame.TeamColor.WHITE);
        System.out.println();
        ChessGame game1 = new ChessGame();
        var changedBoardRep = printer.highlightMove(game1,new ChessMove(new ChessPosition(2,1),new ChessPosition(4,1),null));
        printer.renderBoard(changedBoardRep, ChessGame.TeamColor.WHITE);
    }
}
