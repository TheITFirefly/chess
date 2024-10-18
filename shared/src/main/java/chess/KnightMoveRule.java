package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMoveRule implements MoveRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> goodMoves = new ArrayList<>();
        // Up and to the left
        ChessPosition checkPos = new ChessPosition(position.getRow()+2, position.getColumn()-1);
        if (checkPos.getRow() >= 1 &&checkPos.getRow() <= 8 && checkPos.getColumn() >=1 && checkPos.getColumn() <= 8) {
            if (board.getPiece(checkPos) == null) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            } else if (board.getPiece(checkPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            }
        }

        // Up and to the right
        checkPos = new ChessPosition(position.getRow()+2, position.getColumn()+1);
        if (moveValid(board,position,checkPos)) {
            goodMoves.add(new ChessMove(position,checkPos,null));
        }


        // Down and to the left
        checkPos = new ChessPosition(position.getRow()-2, position.getColumn()-1);
        if (moveValid(board,position,checkPos)) {
            goodMoves.add(new ChessMove(position,checkPos,null));
        }

        // Down and to the right
        checkPos = new ChessPosition(position.getRow()-2, position.getColumn()+1);
        if (moveValid(board,position,checkPos)) {
            goodMoves.add(new ChessMove(position,checkPos,null));
        }

        // To the left and up
        checkPos = new ChessPosition(position.getRow()+1, position.getColumn()-2);
        if (moveValid(board,position,checkPos)) {
            goodMoves.add(new ChessMove(position,checkPos,null));
        }

        // To the left and down
        checkPos = new ChessPosition(position.getRow()-1, position.getColumn()-2);
        if (moveValid(board,position,checkPos)) {
            goodMoves.add(new ChessMove(position,checkPos,null));
        }

        // To the right and up
        checkPos = new ChessPosition(position.getRow()+1, position.getColumn()+2);
        if (moveValid(board,position,checkPos)) {
            goodMoves.add(new ChessMove(position,checkPos,null));
        }

        // To the right and down
        checkPos = new ChessPosition(position.getRow()-1, position.getColumn()+2);
        if (moveValid(board,position,checkPos)) {
            goodMoves.add(new ChessMove(position,checkPos,null));
        }

        return goodMoves;
    }

    private boolean moveValid(ChessBoard board, ChessPosition position, ChessPosition checkPos) {
        // Is the move in bounds
        if (checkPos.getRow() < 1 || checkPos.getRow() > 8 || checkPos.getColumn() < 1 || checkPos.getColumn() > 8) {
            return false;
        }
        ChessPiece targetPiece = board.getPiece(checkPos);
        ChessPiece currentPiece = board.getPiece(position);
        if (targetPiece == null) {
            return true;
        }
        return targetPiece.getTeamColor() != currentPiece.getTeamColor();
    }
}
