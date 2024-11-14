package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import static ui.EscapeSequences.*;

public class BoardPrinter {

    public void printBoard(ChessBoard board, ChessGame.TeamColor closerSide) {
        StringBuilder output = new StringBuilder();
        output.append(EMPTY_BORDER_SQUARE+COLUMN_H+COLUMN_G+COLUMN_F+COLUMN_E+COLUMN_D+COLUMN_C+COLUMN_B+COLUMN_A+"\n");
        output.append(ROW_1);
        output.append(SET_BG_COLOR_LIGHT_GREY+WHITE_ROOK+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+WHITE_KNIGHT+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+WHITE_BISHOP+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+WHITE_KING+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+WHITE_QUEEN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+WHITE_BISHOP+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+WHITE_KNIGHT+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+WHITE_ROOK+RESET_BG_COLOR);
        output.append("\n");

        output.append(ROW_2);
        output.append(SET_BG_COLOR_DARK_GREY+WHITE_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+WHITE_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+WHITE_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+WHITE_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+WHITE_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+WHITE_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+WHITE_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+WHITE_PAWN+RESET_BG_COLOR);
        output.append("\n");

        output.append(ROW_3+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+"\n");
        output.append(ROW_4+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+"\n");
        output.append(ROW_5+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+"\n");
        output.append(ROW_6+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+"\n");

        output.append(ROW_7);
        output.append(SET_BG_COLOR_LIGHT_GREY+BLACK_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+BLACK_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+BLACK_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+BLACK_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+BLACK_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+BLACK_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+BLACK_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+BLACK_PAWN+RESET_BG_COLOR);
        output.append("\n");

        output.append(ROW_8);
        output.append(SET_BG_COLOR_DARK_GREY+BLACK_ROOK+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+BLACK_KNIGHT+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+BLACK_BISHOP+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+BLACK_KING+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+BLACK_QUEEN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+BLACK_BISHOP+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+BLACK_KNIGHT+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+BLACK_ROOK+RESET_BG_COLOR);
        output.append("\n");

        System.out.println(output);

        output = new StringBuilder();
        output.append(EMPTY_BORDER_SQUARE+COLUMN_A+COLUMN_B+COLUMN_C+COLUMN_D+COLUMN_E+COLUMN_F+COLUMN_G+COLUMN_H+"\n");
        output.append(ROW_8);
        output.append(SET_BG_COLOR_LIGHT_GREY+BLACK_ROOK+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+BLACK_KNIGHT+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+BLACK_BISHOP+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+BLACK_QUEEN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+BLACK_KING+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+BLACK_BISHOP+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+BLACK_KNIGHT+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+BLACK_ROOK+RESET_BG_COLOR);
        output.append("\n");

        output.append(ROW_7);
        output.append(SET_BG_COLOR_DARK_GREY+BLACK_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+BLACK_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+BLACK_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+BLACK_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+BLACK_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+BLACK_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+BLACK_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+BLACK_PAWN+RESET_BG_COLOR);
        output.append("\n");

        output.append(ROW_6+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+"\n");
        output.append(ROW_5+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+"\n");
        output.append(ROW_4+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+"\n");
        output.append(ROW_3+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+EMPTY_DARK_SQUARE+EMPTY_LIGHT_SQUARE+"\n");

        output.append(ROW_2);
        output.append(SET_BG_COLOR_LIGHT_GREY+WHITE_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+WHITE_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+WHITE_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+WHITE_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+WHITE_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+WHITE_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+WHITE_PAWN+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+WHITE_PAWN+RESET_BG_COLOR);
        output.append("\n");

        output.append(ROW_1);
        output.append(SET_BG_COLOR_DARK_GREY+WHITE_ROOK+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+WHITE_KNIGHT+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+WHITE_BISHOP+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+WHITE_KING+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+WHITE_KING+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+WHITE_BISHOP+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_DARK_GREY+WHITE_KNIGHT+RESET_BG_COLOR);
        output.append(SET_BG_COLOR_LIGHT_GREY+WHITE_ROOK+RESET_BG_COLOR);
        output.append("\n");
        System.out.println(output);
    }

    // Helper method to get the Unicode symbol for each piece
    private String getSymbolForPiece(ChessPiece piece) {
        switch (piece.getPieceType()) {
            case KING:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING;
            case QUEEN:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK;
            case PAWN:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN;
            default:
                return EscapeSequences.EMPTY;
        }
    }
}
