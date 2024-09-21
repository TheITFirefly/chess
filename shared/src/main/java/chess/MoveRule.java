package chess;

import java.util.Collection;

public interface MoveRule {
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position);
}
