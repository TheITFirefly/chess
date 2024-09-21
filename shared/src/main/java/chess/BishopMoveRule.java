package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveRule implements MoveRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> goodMoves = new ArrayList<>();
        ChessPosition checkPos;

        // Up and to the right
        for (int offset = 1; position.getRow() + offset <= 8 && position.getColumn() + offset <= 8; offset++) {
            checkPos = new ChessPosition(position.getRow() + offset, position.getColumn() + offset);
            if (board.getPiece(checkPos) == null) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            } else if (board.getPiece(checkPos).getTeamColor() == board.getPiece(position).getTeamColor()) {
                break;
            } else if (board.getPiece(checkPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                goodMoves.add(new ChessMove(position,checkPos,null));
                break;
            }
        }

        // Up and to the left
        for (int offset = 1; position.getRow() + offset <= 8 && position.getColumn() - offset >= 1; offset++) {
            checkPos = new ChessPosition(position.getRow() + offset, position.getColumn() - offset);
            if (board.getPiece(checkPos) == null) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            } else if (board.getPiece(checkPos).getTeamColor() == board.getPiece(position).getTeamColor()) {
                break;
            } else if (board.getPiece(checkPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                goodMoves.add(new ChessMove(position,checkPos,null));
                break;
            }
        }

        // Down and to the right
        for (int offset = 1; position.getRow() - offset >= 1 && position.getColumn() + offset <= 8; offset++) {
            checkPos = new ChessPosition(position.getRow() - offset, position.getColumn() + offset);
            if (board.getPiece(checkPos) == null) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            } else if (board.getPiece(checkPos).getTeamColor() == board.getPiece(position).getTeamColor()) {
                break;
            } else if (board.getPiece(checkPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                goodMoves.add(new ChessMove(position,checkPos,null));
                break;
            }
        }

        // Down and to the left
        for (int offset = 1; position.getRow() - offset >= 1 && position.getColumn() - offset >= 1; offset++) {
            checkPos = new ChessPosition(position.getRow() - offset, position.getColumn() - offset);
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
