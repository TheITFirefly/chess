package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard gameBoard;
    private TeamColor currentTurn;
    private boolean blackLRookUnmoved;
    private boolean blackRRookUnmoved;
    private boolean whiteLRookUnmoved;
    private boolean whiteRRookUnmoved;
    private boolean blackKingUnmoved;
    private boolean whiteKingUnmoved;

    public ChessGame() {
        gameBoard = new ChessBoard();
        gameBoard.resetBoard();
        setTeamTurn(TeamColor.WHITE);
        whiteKingUnmoved = true;
        whiteLRookUnmoved = true;
        whiteRRookUnmoved = true;
        blackKingUnmoved = true;
        blackLRookUnmoved = true;
        blackRRookUnmoved = true;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (gameBoard.getPiece(startPosition) == null) {
            return null;
        } else {
            // Track pieces so they can be moved back
            ChessPiece endPositionPiece = null;
            ChessPiece startPositionPiece = gameBoard.getPiece(startPosition);
            // Assume all moves are good to start
            Collection<ChessMove> possibleGoodMoves = startPositionPiece.pieceMoves(gameBoard,startPosition);
            Collection<ChessMove> goodMoves = new ArrayList<>();
            for (ChessMove move : possibleGoodMoves) {
                // Simulate a movement of the piece. Don't forget to save a piece if there is a capture
                if (gameBoard.getPiece(move.getEndPosition()) != null) {
                    endPositionPiece = gameBoard.getPiece(move.getEndPosition());
                }
                gameBoard.movePiece(move);
                if (!isInCheck(startPositionPiece.getTeamColor())) {
                    goodMoves.add(move);
                }
                // Return pieces to original position
                gameBoard.movePiece(new ChessMove(move.getEndPosition(),move.getStartPosition(),move.getPromotionPiece()));
                gameBoard.addPiece(move.getEndPosition(),endPositionPiece);
                endPositionPiece = null;
            }
            return goodMoves;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // Get all good moves. Move it with movePiece if the move is in good moves for the start position. Else throw an exception
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = locateKing(teamColor);
        ChessPiece kingPiece;
            // Assume the king is now a knight. If the king has a valid move to a knight of opposite color, he is in check
            kingPiece = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
            gameBoard.addPiece(kingPos,kingPiece);
            Collection<ChessMove> knightMoves = gameBoard.getPiece(kingPos).pieceMoves(gameBoard,kingPos);
            for (ChessMove move : knightMoves) {
                ChessPiece possibleAttacker = gameBoard.getPiece(move.getEndPosition());
                if (possibleAttacker != null) {
                    if (possibleAttacker.getTeamColor() != teamColor && possibleAttacker.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                        gameBoard.addPiece(kingPos,new ChessPiece(teamColor, ChessPiece.PieceType.KING));
                        return true;
                    }
                }
            }
            // Ensure the king is put back in place
            gameBoard.addPiece(kingPos,new ChessPiece(teamColor, ChessPiece.PieceType.KING));
            // Assume the king is a bishop. If the king has a valid move to a bishop of opposite color, he is in check
            kingPiece = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
            gameBoard.addPiece(kingPos,kingPiece);
            Collection<ChessMove> bishopMoves = gameBoard.getPiece(kingPos).pieceMoves(gameBoard,kingPos);
            for (ChessMove move : bishopMoves) {
                ChessPiece possibleAttacker = gameBoard.getPiece(move.getEndPosition());
                if (possibleAttacker != null) {
                    if (possibleAttacker.getTeamColor() != teamColor && (possibleAttacker.getPieceType() == ChessPiece.PieceType.BISHOP || possibleAttacker.getPieceType() == ChessPiece.PieceType.QUEEN)) {
                        gameBoard.addPiece(kingPos,new ChessPiece(teamColor, ChessPiece.PieceType.KING));
                        return true;
                    }
                }
            }
            // Ensure the king is put back in place
            gameBoard.addPiece(kingPos,new ChessPiece(teamColor, ChessPiece.PieceType.KING));
            // Assume the king is a rook. If the king has a valid move to a rook of opposite color, he is in check
            kingPiece = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
            gameBoard.addPiece(kingPos,kingPiece);
            Collection<ChessMove> rookMoves = gameBoard.getPiece(kingPos).pieceMoves(gameBoard,kingPos);
            for (ChessMove move : rookMoves) {
                ChessPiece possibleAttacker = gameBoard.getPiece(move.getEndPosition());
                if (possibleAttacker != null) {
                    if (possibleAttacker.getTeamColor() != teamColor && (possibleAttacker.getPieceType() == ChessPiece.PieceType.ROOK || possibleAttacker.getPieceType() == ChessPiece.PieceType.QUEEN)) {
                        gameBoard.addPiece(kingPos,new ChessPiece(teamColor, ChessPiece.PieceType.KING));
                        return true;
                    }
                }
            }
            // Ensure the king is put back in place
            gameBoard.addPiece(kingPos,new ChessPiece(teamColor, ChessPiece.PieceType.KING));
            // 2 above cases cover queens
            // King is in check if there is a pawn in either of the positions diagonal to the king, assuming they aren't out of bounds
            if (teamColor == TeamColor.WHITE) {
                ChessPosition leftPawn = new ChessPosition(kingPos.getRow()+1, kingPos.getColumn()-1);
                ChessPosition rightPawn = new ChessPosition(kingPos.getRow()+1, kingPos.getColumn()+1);
                if (leftPawn.getRow()<=8) {
                    if (1 <= leftPawn.getColumn() && leftPawn.getColumn() <= 8) {
                        if (gameBoard.getPiece(leftPawn) != null && gameBoard.getPiece(leftPawn).getPieceType() == ChessPiece.PieceType.PAWN) {
                            return true;
                        }
                    }
                }
            } else {
                ChessPosition leftPawn = new ChessPosition(kingPos.getRow() - 1, kingPos.getColumn() - 1);
                ChessPosition rightPawn = new ChessPosition(kingPos.getRow() - 1, kingPos.getColumn() + 1);
                if (rightPawn.getRow()<=8) {
                    if (1 <= rightPawn.getColumn() && rightPawn.getColumn() <= 8) {
                        if (gameBoard.getPiece(rightPawn) != null && gameBoard.getPiece(rightPawn).getPieceType() == ChessPiece.PieceType.PAWN) {
                            return true;
                        }
                    }
                }
            }
            // Ensure the king is put back in place
            gameBoard.addPiece(kingPos,new ChessPiece(teamColor, ChessPiece.PieceType.KING));
            // Find the opposing king with findKing
            TeamColor opposingColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
            ChessPosition opposingKingPos = locateKing(opposingColor);
            if (opposingKingPos == null) {
                return false;
            }
            ChessPiece opposingKingPiece = gameBoard.getPiece(opposingKingPos);
            // Check if the opposing king can move to the current king's position
            Collection<ChessMove> opposingKingMoves = opposingKingPiece.pieceMoves(gameBoard, opposingKingPos);
            for (ChessMove move : opposingKingMoves) {
                if (move.getEndPosition().equals(kingPos)) {
                    return true;
                }
            }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    /**
     * Returns the position of the king of a specified color
     */
    public ChessPosition locateKing(TeamColor teamColor) {
        for (int row = 1; row <=8; row++) {
            for (int column = 1; column <=8; column++) {
                ChessPosition checkPos = new ChessPosition(row,column);
                if (gameBoard.getPiece(checkPos) != null) {
                    ChessPiece possibleKing = gameBoard.getPiece(checkPos);
                    if (possibleKing.getPieceType() == ChessPiece.PieceType.KING && possibleKing.getTeamColor() == teamColor) {
                        return  checkPos;
                    }
                }
            }
        }
        return null;
    }
}
