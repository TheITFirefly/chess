package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMoveRule implements MoveRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> goodMoves = new ArrayList<>();
        // Queen moves like a rook
        RookMoveRule horMoveCalc = new RookMoveRule();
        Collection<ChessMove> horMoves = horMoveCalc.validMoves(board,position);
        goodMoves.addAll(horMoves);

        //Queen moves like a bishop
        BishopMoveRule diagMoveCalc = new BishopMoveRule();
        Collection<ChessMove> diagMoves = diagMoveCalc.validMoves(board,position);
        goodMoves.addAll(diagMoves);
        return goodMoves;
    }
}
