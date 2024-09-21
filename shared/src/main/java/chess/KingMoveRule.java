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
        if (checkPos.getRow() >= 1 &&checkPos.getRow() <= 8 && checkPos.getColumn() >=1 && checkPos.getColumn() <= 8) {
            if (board.getPiece(checkPos) == null) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            } else if (board.getPiece(checkPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            }
        }
        //Check up of the king
        checkPos = new ChessPosition(position.getRow()+1, position.getColumn());
        if (checkPos.getRow() >= 1 &&checkPos.getRow() <= 8 && checkPos.getColumn() >=1 && checkPos.getColumn() <= 8) {
            if (board.getPiece(checkPos) == null) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            } else if (board.getPiece(checkPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            }
        }
        //Check up and to the right of the king
        checkPos = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        if (checkPos.getRow() >= 1 &&checkPos.getRow() <= 8 && checkPos.getColumn() >=1 && checkPos.getColumn() <= 8) {
            if (board.getPiece(checkPos) == null) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            } else if (board.getPiece(checkPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            }
        }
        //Check to the left of the king
        checkPos = new ChessPosition(position.getRow(), position.getColumn()-1);
        if (checkPos.getRow() >= 1 &&checkPos.getRow() <= 8 && checkPos.getColumn() >=1 && checkPos.getColumn() <= 8) {
            if (board.getPiece(checkPos) == null) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            } else if (board.getPiece(checkPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            }
        }
        //Check to the right of the king
        checkPos = new ChessPosition(position.getRow(), position.getColumn()+1);
        if (checkPos.getRow() >= 1 &&checkPos.getRow() <= 8 && checkPos.getColumn() >=1 && checkPos.getColumn() <= 8) {
            if (board.getPiece(checkPos) == null) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            } else if (board.getPiece(checkPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            }
        }
        //Check down and to the left of the king
        checkPos = new ChessPosition(position.getRow()-1, position.getColumn()-1);
        if (checkPos.getRow() >= 1 &&checkPos.getRow() <= 8 && checkPos.getColumn() >=1 && checkPos.getColumn() <= 8) {
            if (board.getPiece(checkPos) == null) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            } else if (board.getPiece(checkPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            }
        }
        //Check down of the king
        checkPos = new ChessPosition(position.getRow()-1, position.getColumn());
        if (checkPos.getRow() >= 1 &&checkPos.getRow() <= 8 && checkPos.getColumn() >=1 && checkPos.getColumn() <= 8) {
            if (board.getPiece(checkPos) == null) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            } else if (board.getPiece(checkPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            }
        }
        //Check down and to the right of the king
        checkPos = new ChessPosition(position.getRow()-1, position.getColumn()+1);
        if (checkPos.getRow() >= 1 &&checkPos.getRow() <= 8 && checkPos.getColumn() >=1 && checkPos.getColumn() <= 8) {
            if (board.getPiece(checkPos) == null) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            } else if (board.getPiece(checkPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                goodMoves.add(new ChessMove(position,checkPos,null));
            }
        }

        return goodMoves;
    }
}
