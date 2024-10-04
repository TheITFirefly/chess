package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pColor;
    private ChessPiece.PieceType pType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pColor = pieceColor;
        this.pType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        MoveRule moveSet;
        moveSet = switch (pType) {
            case KING -> new KingMoveRule();
            case QUEEN -> new QueenMoveRule();
            case BISHOP -> new BishopMoveRule();
            case KNIGHT -> new KnightMoveRule();
            case ROOK -> new RookMoveRule();
            case PAWN -> new PawnMoveRule();
        };
        return moveSet.validMoves(board,myPosition);
    }

    @Override
    public String toString() {
        String retString = "";
        if (pColor == ChessGame.TeamColor.WHITE) {
            return switch (pType) {
                case KING -> retString + "K";
                case QUEEN -> retString + "Q";
                case BISHOP -> retString + "B";
                case KNIGHT -> retString + "KN";
                case ROOK -> retString + "R";
                case PAWN -> retString + "P";
                default -> retString + "NULL";
            };
        }
        return switch (pType) {
            case KING -> retString + "k";
            case QUEEN -> retString + "q";
            case BISHOP -> retString + "b";
            case KNIGHT -> retString + "kn";
            case ROOK -> retString + "r";
            case PAWN -> retString + "p";
            default -> retString + "null";
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pColor == that.pColor && pType == that.pType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pColor, pType);
    }
}
