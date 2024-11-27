package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import static ui.EscapeSequences.*;

public class BoardPrinter {

    public void printBoard(ChessBoard board, ChessGame.TeamColor closerSide) {
        StringBuilder output = new StringBuilder();

        // Print top column labels
        output.append(EMPTY_BORDER_SQUARE);
        if (closerSide == ChessGame.TeamColor.WHITE) {
            output.append(COLUMN_A + COLUMN_B + COLUMN_C + COLUMN_D + COLUMN_E + COLUMN_F + COLUMN_G + COLUMN_H);
        } else {
            output.append(COLUMN_H + COLUMN_G + COLUMN_F + COLUMN_E + COLUMN_D + COLUMN_C + COLUMN_B + COLUMN_A);
        }
        output.append(EMPTY_BORDER_SQUARE).append("\n");

        // Determine row printing order
        int rowStart = closerSide == ChessGame.TeamColor.WHITE ? 8 : 1;
        int rowEnd = closerSide == ChessGame.TeamColor.WHITE ? 1 : 8;
        int rowIncrement = closerSide == ChessGame.TeamColor.WHITE ? -1 : 1;

        for (int row = rowStart; row != rowEnd + rowIncrement; row += rowIncrement) {
            // Print left row label
            output.append(getRowLabel(row));

            // Determine column printing order
            int colStart = closerSide == ChessGame.TeamColor.WHITE ? 1 : 8;
            int colEnd = closerSide == ChessGame.TeamColor.WHITE ? 8 : 1;
            int colIncrement = closerSide == ChessGame.TeamColor.WHITE ? 1 : -1;

            for (int col = colStart; col != colEnd + colIncrement; col += colIncrement) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                boolean isDarkSquare = (row + col) % 2 == 0;

                if (piece == null) {
                    // Empty square
                    output.append(isDarkSquare ? EMPTY_DARK_SQUARE : EMPTY_LIGHT_SQUARE);
                } else {
                    // Render the piece
                    output.append(renderPiece(piece, isDarkSquare));
                }
            }

            // Print right row label
            output.append(getRowLabel(row)).append("\n");
        }

        // Print bottom column labels
        output.append(EMPTY_BORDER_SQUARE);
        if (closerSide == ChessGame.TeamColor.WHITE) {
            output.append(COLUMN_A + COLUMN_B + COLUMN_C + COLUMN_D + COLUMN_E + COLUMN_F + COLUMN_G + COLUMN_H);
        } else {
            output.append(COLUMN_H + COLUMN_G + COLUMN_F + COLUMN_E + COLUMN_D + COLUMN_C + COLUMN_B + COLUMN_A);
        }
        output.append(EMPTY_BORDER_SQUARE).append("\n");

        System.out.print(output.toString());
    }

    private String renderPiece(ChessPiece piece, boolean isDarkSquare) {
        String pieceRepresentation = getPieceRepresentation(piece);
        return isDarkSquare
                ? SET_BG_COLOR_DARK_GREY + pieceRepresentation + RESET_BG_COLOR
                : SET_BG_COLOR_LIGHT_GREY + pieceRepresentation + RESET_BG_COLOR;
    }

    private String getPieceRepresentation(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            switch (piece.getPieceType()) {
                case KING: return WHITE_KING;
                case QUEEN: return WHITE_QUEEN;
                case BISHOP: return WHITE_BISHOP;
                case KNIGHT: return WHITE_KNIGHT;
                case ROOK: return WHITE_ROOK;
                case PAWN: return WHITE_PAWN;
            }
        } else {
            switch (piece.getPieceType()) {
                case KING: return BLACK_KING;
                case QUEEN: return BLACK_QUEEN;
                case BISHOP: return BLACK_BISHOP;
                case KNIGHT: return BLACK_KNIGHT;
                case ROOK: return BLACK_ROOK;
                case PAWN: return BLACK_PAWN;
            }
        }
        return EMPTY; // Fallback, though this shouldn't happen
    }

    private String getRowLabel(int row) {
        switch (row) {
            case 1: return ROW_1;
            case 2: return ROW_2;
            case 3: return ROW_3;
            case 4: return ROW_4;
            case 5: return ROW_5;
            case 6: return ROW_6;
            case 7: return ROW_7;
            case 8: return ROW_8;
            default: return EMPTY_BORDER_SQUARE; // Should never happen
        }
    }

    public void highlightLegalMoves(ChessGame game, ChessGame.TeamColor closerSide, ChessPosition highlightPosition) {

    }
}