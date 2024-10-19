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

    public ChessGame() {
        gameBoard = new ChessBoard();
        gameBoard.resetBoard();
        setTeamTurn(TeamColor.WHITE);
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
            ChessPiece endPositionPiece = null;
            ChessPiece startPositionPiece = gameBoard.getPiece(startPosition);
            Collection<ChessMove> possibleGoodMoves = startPositionPiece.pieceMoves(gameBoard,startPosition); // Assume all moves are good to start
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
            return false; // King can't be in check if there is no king? YEP
        }
        // Check for attacks from other pieces
        if (attackedByPiece(kingPos, ChessPiece.PieceType.KNIGHT, teamColor) ||
                attackedByPiece(kingPos, ChessPiece.PieceType.ROOK, teamColor) ||
                attackedByPiece(kingPos, ChessPiece.PieceType.BISHOP, teamColor) ||
                attackedByPiece(kingPos, ChessPiece.PieceType.QUEEN, teamColor)) {
            return true;
        }

        // Check for pawn attacks using attackedByPiece method
        if (attackedByPiece(kingPos, ChessPiece.PieceType.PAWN, teamColor)) {
            return true;
        }

        gameBoard.addPiece(kingPos, new ChessPiece(teamColor, ChessPiece.PieceType.KING)); // Ensure the king is put back in place

        // Find the opposing king
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
            // Determine the direction of the pawn's attack based on its color
            int direction = (defendingColor == TeamColor.WHITE) ? 1 : -1; // 1 for white (attacks up), -1 for black (attacks down)
            boolean badRow = 1 > checkPosition.getRow()+direction || checkPosition.getRow()+direction > 8;
            if (badRow) {
                return false;
            }

            boolean needLeftAttacker = (1 < checkPosition.getColumn() && checkPosition.getColumn() <= 8);
            boolean needRightAttacker = 1 <= checkPosition.getColumn() && checkPosition.getColumn() < 8;
            // Calculate the positions where the pawn can attack
            ChessPosition leftAttackPosition = new ChessPosition(checkPosition.getRow()+direction, checkPosition.getColumn()-1);
            ChessPosition rightAttackPosition = new ChessPosition(checkPosition.getRow()+direction,checkPosition.getColumn()+1);

            // Check if there's an opposing piece at the left attack position
            ChessPiece leftAttacker = null;
            if (needLeftAttacker){leftAttacker = gameBoard.getPiece(leftAttackPosition);}
            if (leftAttacker != null && leftAttacker.getTeamColor() != defendingColor) {
                return true;
            }

            // Check if there's an opposing piece at the right attack position
            ChessPiece rightAttacker = null;
            if (needRightAttacker){
                rightAttacker = gameBoard.getPiece(rightAttackPosition);
            }
            return rightAttacker != null && rightAttacker.getTeamColor() != defendingColor;// No opposing pieces can attack this position
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
                Collection<ChessPosition> checkingPieces = getAttackingPieces(kingPos,teamColor);
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
     * returns positions of pieces that are currently attacking the given position
     *
     * @param defendingColor teamColor of defending position
     * @param checkPos position to check
     */
    public Collection<ChessPosition> getAttackingPieces(ChessPosition checkPos, TeamColor defendingColor) {
        Collection<ChessPosition> attackingPieces = new ArrayList<>();
        ChessPiece defender = null;
        Collection<ChessMove> movesToAttackers = new ArrayList<>();
        if (attackedByPiece(checkPos, ChessPiece.PieceType.KNIGHT,defendingColor)){
            defender = new ChessPiece(defendingColor, ChessPiece.PieceType.KNIGHT);
            movesToAttackers.addAll(defender.pieceMoves(gameBoard,checkPos));
        }
        if (attackedByPiece(checkPos, ChessPiece.PieceType.BISHOP,defendingColor)){
            defender = new ChessPiece(defendingColor, ChessPiece.PieceType.BISHOP);
            movesToAttackers.addAll(defender.pieceMoves(gameBoard,checkPos));
        }
        if (attackedByPiece(checkPos, ChessPiece.PieceType.ROOK,defendingColor)){
            defender = new ChessPiece(defendingColor, ChessPiece.PieceType.ROOK);
            movesToAttackers.addAll(defender.pieceMoves(gameBoard,checkPos));
        }
        if (attackedByPiece(checkPos, ChessPiece.PieceType.QUEEN,defendingColor)){
            defender = new ChessPiece(defendingColor, ChessPiece.PieceType.QUEEN);
            movesToAttackers.addAll(defender.pieceMoves(gameBoard,checkPos));
        }
        if (attackedByPiece(checkPos, ChessPiece.PieceType.PAWN,defendingColor)) {
            int pawnDirection = (defendingColor == TeamColor.WHITE) ? 1 : -1;
            boolean needLeftAttacker = (1 < checkPos.getColumn() && checkPos.getColumn() <= 8);
            boolean needRightAttacker = 1 <= checkPos.getColumn() && checkPos.getColumn() < 8;
            if (needLeftAttacker){
                ChessPosition attackerPos = new ChessPosition(checkPos.getRow()+pawnDirection, checkPos.getColumn()-1);
                ChessPiece attacker = gameBoard.getPiece(attackerPos);
                if (attacker != null && attacker.getPieceType()== ChessPiece.PieceType.PAWN && attacker.getTeamColor() != defendingColor){
                    attackingPieces.add(attackerPos);
                }
            }
            if (needRightAttacker){
                ChessPosition attackerPos = new ChessPosition(checkPos.getRow()+pawnDirection, checkPos.getColumn()+1);
                ChessPiece attacker = gameBoard.getPiece(attackerPos);
                if (attacker != null && attacker.getPieceType()== ChessPiece.PieceType.PAWN && attacker.getTeamColor() != defendingColor){
                    attackingPieces.add(attackerPos);
                }
            }
        }
        for (ChessMove move : movesToAttackers) {
            attackingPieces.add(move.getStartPosition());
        }
        return attackingPieces;
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
