package chess;

import java.net.CookieHandler;
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
    private boolean leftBlackRookUnmoved;
    private boolean rightBlackRookUnmoved;
    private boolean leftWhiteRookUnmoved;
    private boolean rightWhiteRookUnmoved;
    private boolean blackKingUnmoved;
    private boolean whiteKingUnmoved;
    private boolean enPassantPossible;
    private int enPassantColumn;

    public ChessGame() {
        gameBoard = new ChessBoard();
        gameBoard.resetBoard();
        setTeamTurn(TeamColor.WHITE);
        whiteKingUnmoved = true;
        leftWhiteRookUnmoved = true;
        rightWhiteRookUnmoved = true;
        blackKingUnmoved = true;
        leftBlackRookUnmoved = true;
        rightBlackRookUnmoved = true;
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
            Collection<ChessMove> goodMoves = new ArrayList<>();            // Castling tests
            Collection<ChessMove> castlingMoves = new ArrayList<>();
            // white left
            if (whiteKingUnmoved && leftWhiteRookUnmoved) {
                if (gameBoard.getPiece(new ChessPosition(1,4)) == null
                        && gameBoard.getPiece(new ChessPosition(1,3)) == null
                        && gameBoard.getPiece(new ChessPosition(1,2)) == null) {
                    castlingMoves.add(new ChessMove(new ChessPosition(1,5),new ChessPosition(1,3),null));
                }
            }
            // white right
            if (whiteKingUnmoved && rightWhiteRookUnmoved) {
                if (gameBoard.getPiece(new ChessPosition(1,6)) == null
                        && gameBoard.getPiece(new ChessPosition(1,7)) == null) {
                    castlingMoves.add(new ChessMove(new ChessPosition(1,5),new ChessPosition(1,7),null));
                }
            }
            // black left
            if (blackKingUnmoved && leftBlackRookUnmoved) {
                if (gameBoard.getPiece(new ChessPosition(8,4)) == null
                        && gameBoard.getPiece(new ChessPosition(8,3)) == null
                        && gameBoard.getPiece(new ChessPosition(8,2)) == null) {
                    castlingMoves.add(new ChessMove(new ChessPosition(8,5),new ChessPosition(8,3),null));
                }
            }
            // black right
            if (blackKingUnmoved && rightBlackRookUnmoved) {
                if (gameBoard.getPiece(new ChessPosition(8,6)) == null
                        && gameBoard.getPiece(new ChessPosition(8,7)) == null) {
                    castlingMoves.add(new ChessMove(new ChessPosition(8,5),new ChessPosition(8,7),null));
                }
            }

            // En passant test white
            // En passant test black

            // Track pieces so they can be moved back
            ChessPiece endPositionPiece = null;
            ChessPiece startPositionPiece = gameBoard.getPiece(startPosition);
            // Assume all moves are good to start
            Collection<ChessMove> possibleGoodMoves = startPositionPiece.pieceMoves(gameBoard,startPosition);
            possibleGoodMoves.addAll(castlingMoves);
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
        ChessPiece movingPiece = gameBoard.getPiece(move.getStartPosition());
        if (movingPiece == null) {
            throw new InvalidMoveException();
        } else if (movingPiece.getTeamColor() != currentTurn) {
            throw new InvalidMoveException();
        }
        // Get all good moves. Move it with movePiece if the move is in good moves for the start position. Else throw an exception
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (validMoves != null && validMoves.contains(move)) {
            gameBoard.movePiece(move);
            setTeamTurn(currentTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
        } else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = locateKing(teamColor);
        if (kingPos == null) {
            // King can't be in check if there is no king?
            return false;
        }
        ChessPiece kingPiece;
        // Assume the king is now a knight. If the king has a valid move to a knight of opposite color, he is in check
        if (attackedByPiece(kingPos, ChessPiece.PieceType.KNIGHT,teamColor)) {
            return true;
        }
        if (attackedByPiece(kingPos, ChessPiece.PieceType.ROOK,teamColor)) {
            return true;
        }
        if (attackedByPiece(kingPos, ChessPiece.PieceType.BISHOP,teamColor)) {
            return true;
        }
        if (attackedByPiece(kingPos, ChessPiece.PieceType.QUEEN,teamColor)) {
            return true;
        }
        if (teamColor == TeamColor.WHITE) {
            ChessPosition leftPawn = new ChessPosition(kingPos.getRow()+1, kingPos.getColumn()-1);
            ChessPosition rightPawn = new ChessPosition(kingPos.getRow()+1, kingPos.getColumn()+1);
            if (1 <= leftPawn.getRow() && leftPawn.getRow()<=8) {
                if (1 <= leftPawn.getColumn() && leftPawn.getColumn() <= 8) {
                    if (gameBoard.getPiece(leftPawn) != null && gameBoard.getPiece(leftPawn).getPieceType() == ChessPiece.PieceType.PAWN && gameBoard.getPiece(leftPawn).getTeamColor() != teamColor) {
                            return true;
                    }
                }
                if (1 <= rightPawn.getColumn() && rightPawn.getColumn() <= 8) {
                    if (gameBoard.getPiece(rightPawn) != null && gameBoard.getPiece(rightPawn).getPieceType() == ChessPiece.PieceType.PAWN && gameBoard.getPiece(rightPawn).getTeamColor() != teamColor) {
                        return true;
                    }
                }
            }
        } else {
            ChessPosition leftPawn = new ChessPosition(kingPos.getRow() - 1, kingPos.getColumn() - 1);
            ChessPosition rightPawn = new ChessPosition(kingPos.getRow() - 1, kingPos.getColumn() + 1);
            if (1<=rightPawn.getRow()&&rightPawn.getRow()<=8) {
                if (1 <= leftPawn.getColumn() && leftPawn.getColumn() <= 8) {
                    if (gameBoard.getPiece(leftPawn) != null && gameBoard.getPiece(leftPawn).getPieceType() == ChessPiece.PieceType.PAWN && gameBoard.getPiece(leftPawn).getTeamColor() != teamColor) {
                        return true;
                    }
                }
                if (1 <= rightPawn.getColumn() && rightPawn.getColumn() <= 8) {
                    if (gameBoard.getPiece(rightPawn) != null && gameBoard.getPiece(rightPawn).getPieceType() == ChessPiece.PieceType.PAWN && gameBoard.getPiece(rightPawn).getTeamColor() != teamColor) {
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
     * Determine if a piece is attacked by another piece that moves freely across the board
     *
     * @param checkPosition position being checked for attack possibilities
     * @param pieceType type of piece to check attacks the position
     * @param defendingColor color of piece at specified position
     * @return True if the specified piece type can attack the specified position
     * */
    public boolean attackedByPiece(ChessPosition checkPosition, ChessPiece.PieceType pieceType, TeamColor defendingColor) {
        ChessPiece attackedPiece = gameBoard.getPiece(checkPosition);
        if (attackedPiece.getTeamColor() != defendingColor) {
            // Can't get attacked by my own color
            return false;
        }
        ChessPiece attackPiece;
        // Pawns are a special case since they don't move bidirectionally on the board
        if (pieceType == ChessPiece.PieceType.PAWN) {
            // TODO: implement attack checks for pawns
            return false;
        }
        attackPiece = new ChessPiece(defendingColor,pieceType);
        Collection<ChessMove> attackMoves = attackPiece.pieceMoves(gameBoard, checkPosition);
        for (ChessMove move : attackMoves) {
            ChessPiece possibleAttacker = gameBoard.getPiece(move.getEndPosition());
            if (possibleAttacker != null) {
                if (possibleAttacker.getTeamColor() != defendingColor && possibleAttacker.getPieceType() == pieceType) {
                    return true;
                }
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
        if (isInCheck(teamColor)) {
            ChessPosition kingPos = locateKing(teamColor);
            Collection<ChessMove> kingPieceMoves = validMoves(kingPos);
            if (kingPieceMoves.isEmpty()) {
                // Some other piece may be able to capture the checking piece
                Collection<ChessPosition> checkingPieces = locateCheckingPieces(teamColor);
                if (checkingPieces.size() > 1) {
                    return true;
                }
                for (ChessPosition checkingPiecePos : checkingPieces) {
                    // if piece is capturable
                    ChessPiece capturablePiece = gameBoard.getPiece(checkingPiecePos);
                    TeamColor opposingColor = capturablePiece.getTeamColor();
                    // Assume the capturable piece is a knight
                    gameBoard.addPiece(checkingPiecePos,new ChessPiece(opposingColor, ChessPiece.PieceType.KNIGHT));
                    Collection<ChessMove> knightMoves = gameBoard.getPiece(checkingPiecePos).pieceMoves(gameBoard,checkingPiecePos);
                    for (ChessMove move : knightMoves) {
                        if (gameBoard.getPiece(move.getEndPosition()) != null && gameBoard.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KNIGHT && gameBoard.getPiece(move.getEndPosition()).getTeamColor() == teamColor) {
                            gameBoard.addPiece(checkingPiecePos,capturablePiece);
                            return false;
                        }
                    }
                    // Assume the capturable piece is a rook
                    gameBoard.addPiece(checkingPiecePos,new ChessPiece(opposingColor, ChessPiece.PieceType.ROOK));
                    Collection<ChessMove> rookMoves = gameBoard.getPiece(checkingPiecePos).pieceMoves(gameBoard,checkingPiecePos);
                    for (ChessMove move : rookMoves) {
                        if (gameBoard.getPiece(move.getEndPosition()) != null && gameBoard.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.ROOK && gameBoard.getPiece(move.getEndPosition()).getTeamColor() == teamColor) {
                            gameBoard.addPiece(checkingPiecePos,capturablePiece);
                            return false;
                        }
                    }
                    // Assume the capturable piece is a bishop
                    gameBoard.addPiece(checkingPiecePos,new ChessPiece(opposingColor, ChessPiece.PieceType.BISHOP));
                    Collection<ChessMove> bishopMoves = gameBoard.getPiece(checkingPiecePos).pieceMoves(gameBoard,checkingPiecePos);
                    for (ChessMove move : bishopMoves) {
                        if (gameBoard.getPiece(move.getEndPosition()) != null && gameBoard.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.BISHOP && gameBoard.getPiece(move.getEndPosition()).getTeamColor() == teamColor) {
                            gameBoard.addPiece(checkingPiecePos,capturablePiece);
                            return false;
                        }
                    }
                    // Can a pawn take?
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * returns positions of pieces that are currently checking the king
     *
     * @param teamColor teamColor of king in check
     */
    public Collection<ChessPosition> locateCheckingPieces(TeamColor teamColor) {
        Collection<ChessPosition> checkingPieces = new ArrayList<>();
        if (!isInCheck(teamColor)) {
            return checkingPieces;
        }
        ChessPosition kingPos = locateKing(teamColor);
        if (kingPos == null) {
            return checkingPieces;
        }
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
                    checkingPieces.add(move.getEndPosition());
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
                    checkingPieces.add(move.getEndPosition());
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
                    checkingPieces.add(move.getEndPosition());
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
            if (1 <= leftPawn.getRow() && leftPawn.getRow()<=8) {
                if (1 <= leftPawn.getColumn() && leftPawn.getColumn() <= 8) {
                    if (gameBoard.getPiece(leftPawn) != null && gameBoard.getPiece(leftPawn).getPieceType() == ChessPiece.PieceType.PAWN && gameBoard.getPiece(leftPawn).getTeamColor() != teamColor) {
                        checkingPieces.add(leftPawn);
                    }
                }
                if (1 <= rightPawn.getColumn() && rightPawn.getColumn() <= 8) {
                    if (gameBoard.getPiece(rightPawn) != null && gameBoard.getPiece(rightPawn).getPieceType() == ChessPiece.PieceType.PAWN && gameBoard.getPiece(rightPawn).getTeamColor() != teamColor) {
                        checkingPieces.add(rightPawn);
                    }
                }
            }
        } else {
            ChessPosition leftPawn = new ChessPosition(kingPos.getRow() - 1, kingPos.getColumn() - 1);
            ChessPosition rightPawn = new ChessPosition(kingPos.getRow() - 1, kingPos.getColumn() + 1);
            if (1<=rightPawn.getRow()&&rightPawn.getRow()<=8) {
                if (1 <= leftPawn.getColumn() && leftPawn.getColumn() <= 8) {
                    if (gameBoard.getPiece(leftPawn) != null && gameBoard.getPiece(leftPawn).getPieceType() == ChessPiece.PieceType.PAWN && gameBoard.getPiece(leftPawn).getTeamColor() != teamColor) {
                        checkingPieces.add(leftPawn);
                    }
                }
                if (1 <= rightPawn.getColumn() && rightPawn.getColumn() <= 8) {
                    if (gameBoard.getPiece(rightPawn) != null && gameBoard.getPiece(rightPawn).getPieceType() == ChessPiece.PieceType.PAWN && gameBoard.getPiece(rightPawn).getTeamColor() != teamColor) {
                        checkingPieces.add(rightPawn);
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
            return checkingPieces;
        }
        ChessPiece opposingKingPiece = gameBoard.getPiece(opposingKingPos);
        // Check if the opposing king can move to the current king's position
        Collection<ChessMove> opposingKingMoves = opposingKingPiece.pieceMoves(gameBoard, opposingKingPos);
        for (ChessMove move : opposingKingMoves) {
            if (move.getEndPosition().equals(kingPos)) {
                checkingPieces.add(opposingKingPos);
                return checkingPieces;
            }
        }
        return checkingPieces;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor) || isInCheckmate(teamColor)) {
            return false;
        }
        // stalemate with 2 kings
        int numPieces = 0;
        for (int row = 1; row <=8 ; row++) {
            for (int column = 1; column <=8 ; column++) {
                if (gameBoard.getPiece(new ChessPosition(row,column))!= null) {
                    numPieces++;
                }
            }
        }
        if (numPieces <= 2) {
            return true;
        }
        for (int row = 1; row <=8 ; row++) {
            for (int column = 1; column <=8 ; column++) {
                if (gameBoard.getPiece(new ChessPosition(row,column)) != null && gameBoard.getPiece(new ChessPosition(row,column)).getTeamColor() == teamColor) {
                    if (!validMoves(new ChessPosition(row, column)).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
        ChessPosition leftWhiteRookPos = new ChessPosition(1,1);
        ChessPosition rightWhiteRookPos = new ChessPosition(1,8);
        ChessPosition leftBlackRookPos = new ChessPosition(8,1);
        ChessPosition rightBlackRookPos = new ChessPosition(8,8);
        ChessPosition whiteKingPos = new ChessPosition(1,5);
        ChessPosition blackKingPos = new ChessPosition(8,5);
        if (locateKing(TeamColor.WHITE) != null && !locateKing(TeamColor.WHITE).equals(whiteKingPos)) {
            whiteKingUnmoved = false;
        }
        if (locateKing(TeamColor.BLACK)!= null && !locateKing(TeamColor.BLACK).equals(blackKingPos)) {
            blackKingUnmoved = false;
        }
        ChessPiece leftWhiteRook = gameBoard.getPiece(leftWhiteRookPos);
        if (leftWhiteRook == null) {
            leftWhiteRookUnmoved = false;
        } else if (leftWhiteRook.getTeamColor() != TeamColor.WHITE || leftWhiteRook.getPieceType() != ChessPiece.PieceType.ROOK) {
            leftWhiteRookUnmoved = false;
        }
        ChessPiece rightWhiteRook = gameBoard.getPiece(rightWhiteRookPos);
        if (rightWhiteRook == null) {
            rightWhiteRookUnmoved = false;
        } else if (rightWhiteRook.getTeamColor() != TeamColor.WHITE || rightWhiteRook.getPieceType() != ChessPiece.PieceType.ROOK) {
            rightWhiteRookUnmoved = false;
        }
        ChessPiece leftBlackRook = gameBoard.getPiece(leftBlackRookPos);
        if (leftBlackRook == null) {
            leftBlackRookUnmoved = false;
        } else if (leftBlackRook.getTeamColor() != TeamColor.WHITE || leftBlackRook.getPieceType() != ChessPiece.PieceType.ROOK) {
            leftBlackRookUnmoved = false;
        }
        ChessPiece rightBlackRook = gameBoard.getPiece(leftWhiteRookPos);
        if (rightBlackRook == null) {
            rightBlackRookUnmoved = false;
        } else if (rightBlackRook.getTeamColor() != TeamColor.WHITE || rightBlackRook.getPieceType() != ChessPiece.PieceType.ROOK) {
            rightBlackRookUnmoved = false;
        }
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
