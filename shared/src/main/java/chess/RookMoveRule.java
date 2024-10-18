package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMoveRule implements MoveRule {

    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> goodMoves = new ArrayList<>();

        // Check moving in each direction
        checkDirection(board, position, goodMoves, -1, 0);  // Left
        checkDirection(board, position, goodMoves, 1, 0);   // Right
        checkDirection(board, position, goodMoves, 0, 1);   // Up
        checkDirection(board, position, goodMoves, 0, -1);  // Down

        return goodMoves;
    }

    /**
     * Helper method to check moves in a specific direction.
     *
     * @param board The chess board
     * @param start The starting position of the piece
     * @param moves The list to add valid moves to
     * @param rowIncrement How the row changes per step (e.g., 0 for horizontal)
     * @param colIncrement How the column changes per step (e.g., 1 for right)
     */
    private void checkDirection(ChessBoard board, ChessPosition start,
                                List<ChessMove> moves, int rowIncrement, int colIncrement) {
        int row = start.getRow() + rowIncrement;
        int col = start.getColumn() + colIncrement;

        while (row > 0 && row <= 8 && col > 0 && col <= 8) {
            ChessPosition checkPos = new ChessPosition(row, col);
            if (isValidMove(board, start, checkPos, moves)) {
                break;
            }
            row += rowIncrement;
            col += colIncrement;
        }
    }

    /**
     * Determines if a move is valid and adds it if it is.
     *
     * @param board The chess board
     * @param start The starting position of the piece
     * @param checkPos The position to check
     * @param moves The list to add valid moves to
     * @return true if the search should stop, false if it should continue
     */
    private boolean isValidMove(ChessBoard board, ChessPosition start,
                                ChessPosition checkPos, List<ChessMove> moves) {
        if (board.getPiece(checkPos) == null) {
            moves.add(new ChessMove(start, checkPos, null));
            return false; // Continue checking in this direction
        } else if (board.getPiece(checkPos).getTeamColor() == board.getPiece(start).getTeamColor()) {
            return true; // Stop checking in this direction
        } else {
            moves.add(new ChessMove(start, checkPos, null));
            return true; // Stop after capturing opponent's piece
        }
    }
}
