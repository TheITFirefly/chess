package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveRule implements MoveRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> goodMoves = new ArrayList<>();
        ChessPosition checkPos;
        //Check up and to the left of the king
        checkPos = new ChessPosition(position.getRow()+1, position.getColumn()-1);
        if (moveValid(board,position,checkPos)) {
            goodMoves.add(new ChessMove(position,checkPos,null));
        }
        checkPos = new ChessPosition(position.getRow()+1, position.getColumn());
        if (moveValid(board,position,checkPos)) {
            goodMoves.add(new ChessMove(position,checkPos,null));
        }
        checkPos = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        if (moveValid(board,position,checkPos)) {
            goodMoves.add(new ChessMove(position,checkPos,null));
        }
        checkPos = new ChessPosition(position.getRow(), position.getColumn()-1);
        if (moveValid(board,position,checkPos)) {
            goodMoves.add(new ChessMove(position,checkPos,null));
        }
        checkPos = new ChessPosition(position.getRow(), position.getColumn()+1);
        if (moveValid(board,position,checkPos)) {
            goodMoves.add(new ChessMove(position,checkPos,null));
        }
        checkPos = new ChessPosition(position.getRow()-1, position.getColumn()-1);
        if (moveValid(board,position,checkPos)) {
            goodMoves.add(new ChessMove(position,checkPos,null));
        }
        checkPos = new ChessPosition(position.getRow()-1, position.getColumn());
        if (moveValid(board,position,checkPos)) {
            goodMoves.add(new ChessMove(position,checkPos,null));
        }
        checkPos = new ChessPosition(position.getRow()-1, position.getColumn()+1);
        if (moveValid(board,position,checkPos)) {
            goodMoves.add(new ChessMove(position,checkPos,null));
        }

        return goodMoves;
    }

    private boolean moveValid(ChessBoard board, ChessPosition position, ChessPosition checkPos) {
        // Check if the position is within the board limits (1 to 8 for both rows and columns)
        if (checkPos.getRow() < 1 || checkPos.getRow() > 8 || checkPos.getColumn() < 1 || checkPos.getColumn() > 8) {
            return false; // Move is out of bounds
        }

        // Get the piece at the target position (if any)
        ChessPiece targetPiece = board.getPiece(checkPos);
        ChessPiece currentPiece = board.getPiece(position); // The king piece

        // If the target square is empty, it's a valid move
        if (targetPiece == null) {
            return true;
        }

        // If the target square contains a piece of the same color, it's not a valid move
        if (targetPiece.getTeamColor() == currentPiece.getTeamColor()) {
            return false;
        }

        // If the target square contains an opponent's piece, it's a valid capture
        return true;
    }
}
