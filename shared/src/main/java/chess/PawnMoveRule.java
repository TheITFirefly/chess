package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveRule implements MoveRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> goodMoves = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(position);
        ChessGame.TeamColor currentColor = currentPiece.getTeamColor();
        boolean blackInitMove = false;
        boolean whiteInitMove = false;
        boolean blackPossiblePromote = false;
        boolean whitePossiblePromote = false;
        boolean checkLeft = false;
        boolean checkRight = false;
        if (currentColor == ChessGame.TeamColor.BLACK && position.getRow() == 7) {
            blackInitMove = true;
        } else if (currentColor == ChessGame.TeamColor.BLACK && position.getRow() == 2) {
            blackPossiblePromote = true;
        }
        if (currentColor == ChessGame.TeamColor.WHITE && position.getRow() == 2) {
            whiteInitMove = true;
        } else if (currentColor == ChessGame.TeamColor.WHITE && position.getRow() == 7) {
            whitePossiblePromote = true;
        }
        if (position.getColumn() > 1 && position.getColumn()<=8) {
            checkLeft = true;
        }
        if (position.getColumn() < 8 && position.getColumn()>=1) {
            checkRight = true;
        }
        if (blackInitMove) {
            if (board.getPiece(new ChessPosition(position.getRow()-1, position.getColumn())) == null) {
                goodMoves.add(new ChessMove(position, new ChessPosition(position.getRow()-1, position.getColumn()), null));
                if (board.getPiece(new ChessPosition(position.getRow()-2, position.getColumn())) == null) {
                    goodMoves.add(new ChessMove(position, new ChessPosition(position.getRow()-2, position.getColumn()), null));
                }
            }
        } else if (currentColor == ChessGame.TeamColor.BLACK && !blackPossiblePromote && board.getPiece(new ChessPosition(position.getRow()-1, position.getColumn())) == null) {
            goodMoves.add(new ChessMove(position, new ChessPosition(position.getRow()-1, position.getColumn()), null));
        }
        if (blackPossiblePromote) {
            if (board.getPiece(new ChessPosition(position.getRow()-1, position.getColumn())) == null) {
                goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()), ChessPiece.PieceType.QUEEN));
                goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()), ChessPiece.PieceType.ROOK));
                goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()), ChessPiece.PieceType.KNIGHT));
                goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()), ChessPiece.PieceType.BISHOP));
            }
        }
        if (currentColor == ChessGame.TeamColor.BLACK && checkLeft){
            if (board.getPiece(new ChessPosition(position.getRow()-1, position.getColumn()-1)) != null && board.getPiece(new ChessPosition(position.getRow()-1, position.getColumn()-1)).getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (blackPossiblePromote) {
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1,position.getColumn()-1), ChessPiece.PieceType.QUEEN));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1,position.getColumn()-1), ChessPiece.PieceType.ROOK));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1,position.getColumn()-1), ChessPiece.PieceType.KNIGHT));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1,position.getColumn()-1), ChessPiece.PieceType.BISHOP));
                } else {
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1,position.getColumn()-1),null));
                }
            }
        }
        if (currentColor == ChessGame.TeamColor.BLACK && checkRight){
            if (board.getPiece(new ChessPosition(position.getRow()-1, position.getColumn()+1)) != null && board.getPiece(new ChessPosition(position.getRow()-1, position.getColumn()+1)).getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (blackPossiblePromote) {
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1,position.getColumn()+1), ChessPiece.PieceType.QUEEN));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1,position.getColumn()+1), ChessPiece.PieceType.ROOK));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1,position.getColumn()+1), ChessPiece.PieceType.KNIGHT));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1,position.getColumn()+1), ChessPiece.PieceType.BISHOP));
                } else {
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1,position.getColumn()+1),null));
                }
            }
        }
        if (whiteInitMove) {
            if (board.getPiece(new ChessPosition(position.getRow()+1, position.getColumn())) == null) {
                goodMoves.add(new ChessMove(position, new ChessPosition(position.getRow()+1, position.getColumn()), null));
                if (board.getPiece(new ChessPosition(position.getRow()+2, position.getColumn())) == null) {
                    goodMoves.add(new ChessMove(position, new ChessPosition(position.getRow()+2, position.getColumn()), null));
                }
            }
        } else if (currentColor == ChessGame.TeamColor.WHITE && !whitePossiblePromote && board.getPiece(new ChessPosition(position.getRow()+1, position.getColumn())) == null) {
            goodMoves.add(new ChessMove(position, new ChessPosition(position.getRow()+1, position.getColumn()), null));
        }
        if (whitePossiblePromote){
            if (board.getPiece(new ChessPosition(position.getRow()+1, position.getColumn())) == null) {
                goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()), ChessPiece.PieceType.QUEEN));
                goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()), ChessPiece.PieceType.ROOK));
                goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()), ChessPiece.PieceType.KNIGHT));
                goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()), ChessPiece.PieceType.BISHOP));
            }
        }
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE && checkLeft) {
            if (board.getPiece(new ChessPosition(position.getRow()+1, position.getColumn()-1)) != null && board.getPiece(new ChessPosition(position.getRow()+1, position.getColumn()-1)).getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (blackPossiblePromote) {
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1,position.getColumn()-1), ChessPiece.PieceType.QUEEN));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1,position.getColumn()-1), ChessPiece.PieceType.ROOK));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1,position.getColumn()-1), ChessPiece.PieceType.KNIGHT));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1,position.getColumn()-1), ChessPiece.PieceType.BISHOP));
                } else {
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1,position.getColumn()-1),null));
                }
            }
        }
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE && checkRight) {
            if (board.getPiece(new ChessPosition(position.getRow()+1, position.getColumn()+1)) != null && board.getPiece(new ChessPosition(position.getRow()+1, position.getColumn()+1)).getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (blackPossiblePromote) {
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1,position.getColumn()+1), ChessPiece.PieceType.QUEEN));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1,position.getColumn()+1), ChessPiece.PieceType.ROOK));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1,position.getColumn()+1), ChessPiece.PieceType.KNIGHT));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1,position.getColumn()+1), ChessPiece.PieceType.BISHOP));
                } else {
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1,position.getColumn()+1),null));
                }
            }
        }
        return goodMoves;
    }
}