package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class BoardPrinter {

    public void printBoard(ChessBoard board, ChessGame.TeamColor closerSide) {
        StringBuilder output = new StringBuilder();
        boolean isCloserSideWhite = closerSide == ChessGame.TeamColor.WHITE;

        // Top row for column labels, with black background
        output.append(EscapeSequences.SET_BG_COLOR_BLACK).append("   "); // Corner space
        for (char col = 'A'; col <= 'H'; col++) {
            output.append(" ").append(col).append(" ");
        }
        output.append(EscapeSequences.RESET_BG_COLOR).append("\n");

        // Determine row iteration based on closer side
        int startRow = isCloserSideWhite ? 1 : 8;
        int endRow = isCloserSideWhite ? 9 : 0;
        int rowIncrement = isCloserSideWhite ? 1 : -1;

        // Build each row of the chessboard
        for (int row = startRow; row != endRow; row += rowIncrement) {
            // Left row label with black background
            output.append(EscapeSequences.SET_BG_COLOR_BLACK).append(" ").append(row).append(" ").append(EscapeSequences.RESET_BG_COLOR);

            // Chessboard squares
            for (char col = 'A'; col <= 'H'; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col - 'A' + 1));
                boolean isDarkSquare = ((row + col) % 2) == 0;

                // Background color based on square color
                if (isDarkSquare) {
                    output.append(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                } else {
                    output.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                }

                // Piece or empty square
                if (piece != null) {
                    output.append(" ").append(getSymbolForPiece(piece)).append(" ");
                } else {
                    output.append(EscapeSequences.EMPTY);
                }

                // Reset colors
                output.append(EscapeSequences.RESET_BG_COLOR).append(EscapeSequences.RESET_TEXT_COLOR);
            }

            // Right row label with black background
            output.append(EscapeSequences.SET_BG_COLOR_BLACK).append(" ").append(row).append(" ");
            output.append(EscapeSequences.RESET_BG_COLOR).append("\n");
        }

        // Bottom row for column labels, with black background
        output.append(EscapeSequences.SET_BG_COLOR_BLACK).append("   "); // Corner space
        for (char col = 'A'; col <= 'H'; col++) {
            output.append(" ").append(col).append(" ");
        }
        output.append(EscapeSequences.RESET_BG_COLOR).append("\n");

        // Print the final output
        System.out.print(output);
    }

    // Helper method to get the Unicode symbol for each piece
    private String getSymbolForPiece(ChessPiece piece) {
        switch (piece.getPieceType()) {
            case KING:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING;
            case QUEEN:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN;
            case BISHOP:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP;
            case KNIGHT:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT;
            case ROOK:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK;
            case PAWN:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN;
            default:
                return EscapeSequences.EMPTY;
        }
    }
}
