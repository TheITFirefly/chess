package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMoveRule implements MoveRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> goodMoves = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(position);

        // Black move checks
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            // First move check black
            if (position.getRow() == 7) {
                if (board.getPiece(new ChessPosition(position.getRow()-1, position.getColumn())) == null) {
                    goodMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn()), null));
                    if (board.getPiece(new ChessPosition(position.getRow()-2, position.getColumn())) == null) {
                        goodMoves.add(new ChessMove(position, new ChessPosition(position.getRow() - 2, position.getColumn()), null));
                    }
                }
            }
            // Regular move
            if (board.getPiece(new ChessPosition(position.getRow()-1, position.getColumn())) == null) {
                //Promotion check
                if (position.getRow() == 2) {
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()), ChessPiece.PieceType.QUEEN));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()), ChessPiece.PieceType.ROOK));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()), ChessPiece.PieceType.KNIGHT));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()), ChessPiece.PieceType.BISHOP));
                } else {
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()), null));
                }
            }
            // Columns in bounds
            if (position.getColumn() > 1 && position.getColumn() < 8) {
                //Check attack left
                if (board.getPiece(new ChessPosition(position.getRow()-1, position.getColumn()-1)) != null) {
                    if (board.getPiece(new ChessPosition(position.getRow()-1, position.getColumn()-1)).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        if (position.getRow() == 2) {
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()-1), ChessPiece.PieceType.QUEEN));
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()-1), ChessPiece.PieceType.ROOK));
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()-1), ChessPiece.PieceType.KNIGHT));
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()-1), ChessPiece.PieceType.BISHOP));
                        } else {
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()-1), null));
                        }
                    }
                }
                // Check attack right
                if (board.getPiece(new ChessPosition(position.getRow()-1, position.getColumn()+1)) != null) {
                    if (board.getPiece(new ChessPosition(position.getRow()-1, position.getColumn()+1)).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        if (position.getRow() == 2) {
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()+1), ChessPiece.PieceType.QUEEN));
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()+1), ChessPiece.PieceType.ROOK));
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()+1), ChessPiece.PieceType.KNIGHT));
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()+1), ChessPiece.PieceType.BISHOP));
                        } else {
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()-1, position.getColumn()+1), null));
                        }
                    }
                }
            }
        }
        // White move checks
        else {
            // First move check white
            if (position.getRow() == 2) {
                if (board.getPiece(new ChessPosition(position.getRow()+1, position.getColumn())) == null) {
                    goodMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn()), null));
                    if (board.getPiece(new ChessPosition(position.getRow()+2, position.getColumn())) == null) {
                        goodMoves.add(new ChessMove(position, new ChessPosition(position.getRow() + 2, position.getColumn()), null));
                    }
                }
            }
            // Regular move
            if (board.getPiece(new ChessPosition(position.getRow()+1, position.getColumn())) == null) {
                //Promotion check
                if (position.getRow() == 7) {
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()), ChessPiece.PieceType.QUEEN));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()), ChessPiece.PieceType.ROOK));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()), ChessPiece.PieceType.KNIGHT));
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()), ChessPiece.PieceType.BISHOP));
                } else {
                    goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()), null));
                }
            }
            // Columns in bounds
            if (position.getColumn() > 1 && position.getColumn() < 8) {
                //Check attack left
                if (board.getPiece(new ChessPosition(position.getRow()+1, position.getColumn()-1)) != null) {
                    if (board.getPiece(new ChessPosition(position.getRow()+1, position.getColumn()-1)).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        if (position.getRow() == 7) {
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()-1), ChessPiece.PieceType.QUEEN));
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()-1), ChessPiece.PieceType.ROOK));
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()-1), ChessPiece.PieceType.KNIGHT));
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()-1), ChessPiece.PieceType.BISHOP));
                        } else {
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()-1), null));
                        }
                    }
                }
                // Check attack right
                if (board.getPiece(new ChessPosition(position.getRow()+1, position.getColumn()+1)) != null) {
                    if (board.getPiece(new ChessPosition(position.getRow()+1, position.getColumn()+1)).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        if (position.getRow() == 7) {
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()+1), ChessPiece.PieceType.QUEEN));
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()+1), ChessPiece.PieceType.ROOK));
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()+1), ChessPiece.PieceType.KNIGHT));
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()+1), ChessPiece.PieceType.BISHOP));
                        } else {
                            goodMoves.add(new ChessMove(position,new ChessPosition(position.getRow()+1, position.getColumn()+1), null));
                        }
                    }
                }
            }
        }
        return goodMoves;
    }
}
