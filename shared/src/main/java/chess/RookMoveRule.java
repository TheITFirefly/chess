package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMoveRule implements MoveRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> goodMoves = new ArrayList<>();
        ChessPosition checkPos;
        // Check moving left
        for (int column = (position.getColumn()-1); column > 0; column--) {
            checkPos = new ChessPosition(position.getRow(), column);
            if (board.getPiece(checkPos) == null) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            } else if (board.getPiece(checkPos).getTeamColor() == board.getPiece(position).getTeamColor()) {
                break;
            } else if (board.getPiece(checkPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                goodMoves.add(new ChessMove(position,checkPos,null));
                break;
            }
        }

        // Check moving right
        for (int column = (position.getColumn()+1); column <= 8; column++) {
            checkPos = new ChessPosition(position.getRow(), column);
            if (board.getPiece(checkPos) == null) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            } else if (board.getPiece(checkPos).getTeamColor() == board.getPiece(position).getTeamColor()) {
                break;
            } else if (board.getPiece(checkPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                goodMoves.add(new ChessMove(position,checkPos,null));
                break;
            }
        }

        // Check moving up
        for (int row = (position.getRow()+1); row <= 8; row++) {
            checkPos = new ChessPosition(row, position.getColumn());
            if (board.getPiece(checkPos) == null) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            } else if (board.getPiece(checkPos).getTeamColor() == board.getPiece(position).getTeamColor()) {
                break;
            } else if (board.getPiece(checkPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                goodMoves.add(new ChessMove(position,checkPos,null));
                break;
            }
        }
        // Check moving down
        for (int row = (position.getRow()-1); row > 0; row--) {
            checkPos = new ChessPosition(row, position.getColumn());
            if (board.getPiece(checkPos) == null) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            } else if (board.getPiece(checkPos).getTeamColor() == board.getPiece(position).getTeamColor()) {
                break;
            } else if (board.getPiece(checkPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                goodMoves.add(new ChessMove(position,checkPos,null));
                break;
            }
        }

        return goodMoves;
    }
}
