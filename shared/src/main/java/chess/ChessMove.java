package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition sPosition;
    private ChessPosition ePosition;
    private ChessPiece.PieceType promType;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this.sPosition = startPosition;
        this.ePosition = endPosition;
        this.promType = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return sPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return ePosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promType;
    }

    @Override public String toString() {
        String retString = sPosition.toString()+','+ePosition.toString()+',';
        if (getPromotionPiece() == null) {
            return retString+"NULL";
        }
        switch (getPromotionPiece()) {
            case KING:                
                return retString+"KING";
            case QUEEN:
                return retString+"QUEEN";
            case BISHOP:
                return retString+"BISHOP";
            case KNIGHT:
                return retString+"KNIGHT";
            case ROOK:
                return retString+"ROOK";
            case PAWN:
                return retString+"PAWN";
            default:
                return retString+"OOPSEY";
        }
    }

    @Override public int hashCode() { //ChessMove hashCode is sPositionePositionpromType
        int retValue = Integer.parseInt(Integer.toString(getStartPosition().hashCode())+Integer.toString(getEndPosition().hashCode()));
        if (getPromotionPiece() == null) {
            return Integer.parseInt(Integer.toString(retValue)+Integer.toString(0));
        }
        switch (getPromotionPiece()) {
            case KING:                
                return Integer.parseInt(Integer.toString(retValue)+Integer.toString(1));
            case QUEEN:
                return Integer.parseInt(Integer.toString(retValue)+Integer.toString(2));
            case BISHOP:
                return Integer.parseInt(Integer.toString(retValue)+Integer.toString(3));
            case KNIGHT:
                return Integer.parseInt(Integer.toString(retValue)+Integer.toString(4));
            case ROOK:
                return Integer.parseInt(Integer.toString(retValue)+Integer.toString(5));
            case PAWN:
                return Integer.parseInt(Integer.toString(retValue)+Integer.toString(6));
            default:
                return Integer.parseInt(Integer.toString(retValue)+Integer.toString(7));
        }
    }

    @Override public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ChessMove objComp = (ChessMove) obj;
        return hashCode() == objComp.hashCode();
        //try this again after implementing equals for chesspiece.piecetype
        //return getStartPosition() == objComp.getStartPosition() && getEndPosition() == objComp.getEndPosition() && getPromotionPiece() == objComp.getPromotionPiece();
    }
}
