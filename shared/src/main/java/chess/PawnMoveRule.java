package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveRule implements MoveRule {

    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> goodMoves = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(position);
        ChessGame.TeamColor color = currentPiece.getTeamColor();

        int direction = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;
        boolean isInitialMove = isInitialRow(position, color);
        boolean canPromote = isPromotionRow(position, color);

        addForwardMoves(board, position, direction, isInitialMove, canPromote, goodMoves);
        addCaptureMoves(board, position, direction, canPromote, goodMoves);

        return goodMoves;
    }

    private boolean isInitialRow(ChessPosition position, ChessGame.TeamColor color) {
        return (color == ChessGame.TeamColor.WHITE && position.getRow() == 2) ||
                (color == ChessGame.TeamColor.BLACK && position.getRow() == 7);
    }

    private boolean isPromotionRow(ChessPosition position, ChessGame.TeamColor color) {
        return (color == ChessGame.TeamColor.WHITE && position.getRow() == 7) ||
                (color == ChessGame.TeamColor.BLACK && position.getRow() == 2);
    }

    private void addForwardMoves(ChessBoard board, ChessPosition position, int direction,
                                 boolean isInitialMove, boolean canPromote, ArrayList<ChessMove> goodMoves) {
        int nextRow = position.getRow() + direction;

        if (board.getPiece(new ChessPosition(nextRow, position.getColumn())) == null) {
            addMove(position, nextRow, position.getColumn(), canPromote, goodMoves);

            if (isInitialMove) {
                int twoStepsRow = position.getRow() + 2 * direction;
                if (board.getPiece(new ChessPosition(twoStepsRow, position.getColumn())) == null) {
                    goodMoves.add(new ChessMove(position, new ChessPosition(twoStepsRow, position.getColumn()), null));
                }
            }
        }
    }

    private void addCaptureMoves(ChessBoard board, ChessPosition position, int direction,
                                 boolean canPromote, ArrayList<ChessMove> goodMoves) {
        int nextRow = position.getRow() + direction;

        for (int colOffset = -1; colOffset <= 1; colOffset += 2) {
            int nextCol = position.getColumn() + colOffset;
            if (isValidColumn(nextCol)) {
                ChessPiece target = board.getPiece(new ChessPosition(nextRow, nextCol));
                if (target != null && target.getTeamColor() != board.getPiece(position).getTeamColor()) {
                    addMove(position, nextRow, nextCol, canPromote, goodMoves);
                }
            }
        }
    }

    private boolean isValidColumn(int column) {
        return column >= 1 && column <= 8;
    }

    private void addMove(ChessPosition from, int toRow, int toCol, boolean canPromote, ArrayList<ChessMove> moves) {
        ChessPosition to = new ChessPosition(toRow, toCol);
        if (canPromote) {
            for (ChessPiece.PieceType type : ChessPiece.PieceType.values()) {
                if (type != ChessPiece.PieceType.PAWN && type != ChessPiece.PieceType.KING) {
                    moves.add(new ChessMove(from, to, type));
                }
            }
        } else {
            moves.add(new ChessMove(from, to, null));
        }
    }
}
