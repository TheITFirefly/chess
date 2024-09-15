package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row;
    private int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }
    /* Need to override tostring, hashcode, equals */
    @Override
    public String toString() {
        return "lol";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = hash * 5 + row;
        hash = hash * 5 + col;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ChessPosition objComp = (ChessPosition) obj;
        return getRow() == objComp.getRow() && getColumn() == objComp.getColumn();
    }
}