package ui;

import java.util.Collection;

import chess.*;

import static ui.EscapeSequences.*;

public class BoardPrinter {
    public String[][] highlightLegalMoves(ChessGame game, ChessPosition position) {
        ChessBoard board = game.getBoard();
        // Initialize the boardRep with the current pieces or empty squares
        String[][] boardRep = importBoard(board);

        // Position should be in bounds
        if (!positionInBounds(position)) {
            return boardRep;
        }

        // Get all valid moves for the selected piece
        Collection<ChessMove> validMoves = game.validMoves(position);
        // Highlight the selected piece
        ChessPiece selectedPiece = board.getPiece(position);
        if (selectedPiece != null) {
            boardRep[position.getRow()-1][position.getColumn()-1] = renderSelectedPiece(selectedPiece);
        }
        if (validMoves == null) {
            return boardRep;
        }
        // Highlight each destination square
        for (ChessMove move : validMoves) {
            ChessPosition highlightPos = move.getEndPosition();
            int highlightRow = highlightPos.getRow()-1; //0-based indexing
            int highlightCol = highlightPos.getColumn()-1; //0-based indexing
            boolean isDarkSquare = (highlightRow + highlightCol) % 2 == 0;
            ChessPiece attackedPiece = board.getPiece(highlightPos);
            if (attackedPiece == null) {
                // Empty square to be highlighted
                boardRep[highlightRow][highlightCol] = isDarkSquare
                        ? EscapeSequences.EMPTY_DARK_HIGHLIGHT_SQUARE
                        : EscapeSequences.EMPTY_LIGHT_HIGHLIGHT_SQUARE;
            } else {
                // Square with a capturable piece to be highlighted
                boardRep[highlightRow][highlightCol] = renderAttackedPiece(attackedPiece);
            }
        }
        return boardRep;
    }

    public void renderBoard(String[][] boardRep, ChessGame.TeamColor viewpoint) {
        StringBuilder output = new StringBuilder();

        // Print top column labels
        output.append(EscapeSequences.EMPTY_BORDER_SQUARE);
        if (viewpoint == ChessGame.TeamColor.WHITE) {
            output.append(EscapeSequences.COLUMN_A + EscapeSequences.COLUMN_B + EscapeSequences.COLUMN_C +
                    EscapeSequences.COLUMN_D + EscapeSequences.COLUMN_E + EscapeSequences.COLUMN_F +
                    EscapeSequences.COLUMN_G + EscapeSequences.COLUMN_H);
        } else {
            output.append(EscapeSequences.COLUMN_H + EscapeSequences.COLUMN_G + EscapeSequences.COLUMN_F +
                    EscapeSequences.COLUMN_E + EscapeSequences.COLUMN_D + EscapeSequences.COLUMN_C +
                    EscapeSequences.COLUMN_B + EscapeSequences.COLUMN_A);
        }
        output.append(EscapeSequences.EMPTY_BORDER_SQUARE).append("\n");

        // Determine row printing order
        int rowStart = viewpoint == ChessGame.TeamColor.WHITE ? 8 : 1;
        int rowEnd = viewpoint == ChessGame.TeamColor.WHITE ? 1 : 8;
        int rowIncrement = viewpoint == ChessGame.TeamColor.WHITE ? -1 : 1;

        for (int row = rowStart; row != rowEnd + rowIncrement; row += rowIncrement) {
            // Print left row label
            output.append(getRowLabel(row));

            // Determine column printing order
            int colStart = viewpoint == ChessGame.TeamColor.WHITE ? 1 : 8;
            int colEnd = viewpoint == ChessGame.TeamColor.WHITE ? 8 : 1;
            int colIncrement = viewpoint == ChessGame.TeamColor.WHITE ? 1 : -1;

            for (int col = colStart; col != colEnd + colIncrement; col += colIncrement) {
                // Append the representation from boardRep
                output.append(boardRep[row - 1][col - 1]);
            }

            // Print right row label
            output.append(getRowLabel(row)).append("\n");
        }

        // Print bottom column labels
        output.append(EscapeSequences.EMPTY_BORDER_SQUARE);
        if (viewpoint == ChessGame.TeamColor.WHITE) {
            output.append(EscapeSequences.COLUMN_A + EscapeSequences.COLUMN_B + EscapeSequences.COLUMN_C +
                    EscapeSequences.COLUMN_D + EscapeSequences.COLUMN_E + EscapeSequences.COLUMN_F +
                    EscapeSequences.COLUMN_G + EscapeSequences.COLUMN_H);
        } else {
            output.append(EscapeSequences.COLUMN_H + EscapeSequences.COLUMN_G + EscapeSequences.COLUMN_F +
                    EscapeSequences.COLUMN_E + EscapeSequences.COLUMN_D + EscapeSequences.COLUMN_C +
                    EscapeSequences.COLUMN_B + EscapeSequences.COLUMN_A);
        }
        output.append(EscapeSequences.EMPTY_BORDER_SQUARE).append("\n");

        System.out.print(output);
    }

    private String renderPiece(ChessPiece piece, boolean isDarkSquare) {
        String pieceRep = getPieceRepresentation(piece);
        return isDarkSquare
                ? SET_BG_COLOR_DARK_GREY + pieceRep + RESET_BG_COLOR
                : SET_BG_COLOR_LIGHT_GREY + pieceRep + RESET_BG_COLOR;
    }

    private String renderAttackedPiece(ChessPiece piece) {
        String pieceRep = getPieceRepresentation(piece);
        return SET_BG_COLOR_RED + pieceRep + RESET_BG_COLOR;
    }

    private String renderSelectedPiece(ChessPiece piece) {
        String pieceRep = getPieceRepresentation(piece);
        return SET_BG_COLOR_YELLOW + pieceRep + RESET_BG_COLOR;
    }

    private String renderChangedPiece(ChessPiece piece) {
        String pieceRep = getPieceRepresentation(piece);
        return SET_BG_COLOR_BLUE + pieceRep + RESET_BG_COLOR;
    }

    private String getPieceRepresentation(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return switch (piece.getPieceType()) {
                case KING -> WHITE_KING;
                case QUEEN -> WHITE_QUEEN;
                case BISHOP -> WHITE_BISHOP;
                case KNIGHT -> WHITE_KNIGHT;
                case ROOK -> WHITE_ROOK;
                case PAWN -> WHITE_PAWN;
            };
        } else {
            return switch (piece.getPieceType()) {
                case KING -> BLACK_KING;
                case QUEEN -> BLACK_QUEEN;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case ROOK -> BLACK_ROOK;
                case PAWN -> BLACK_PAWN;
            };
        }
    }

    private String getRowLabel(int row) {
        return switch (row) {
            case 1 -> ROW_1;
            case 2 -> ROW_2;
            case 3 -> ROW_3;
            case 4 -> ROW_4;
            case 5 -> ROW_5;
            case 6 -> ROW_6;
            case 7 -> ROW_7;
            case 8 -> ROW_8;
            default -> EMPTY_BORDER_SQUARE; // Should never happen
        };
    }

    private boolean positionInBounds(ChessPosition position) {
        boolean rowInBounds = 1 <= position.getRow() && position.getRow() <= 8;
        boolean colInBounds = 1 <= position.getColumn() && position.getColumn() <= 8;
        return rowInBounds && colInBounds;
    }

    public String[][] highlightMove(ChessGame game,ChessMove madeMove) {
        String[][] retBoardRep = highlightLegalMoves(game,new ChessPosition(9,9));
        var startRow = madeMove.getStartPosition().getRow()-1;
        var startCol = madeMove.getStartPosition().getColumn()-1;
        var endRow = madeMove.getEndPosition().getRow()-1;
        var endCol = madeMove.getEndPosition().getColumn()-1;
        var board = game.getBoard();
        var startPiece = board.getPiece(madeMove.getStartPosition());
        var endPiece = board.getPiece(madeMove.getEndPosition());
        retBoardRep[startRow][startCol] = renderChangedPiece(startPiece);
        retBoardRep[endRow][endCol] = renderChangedPiece(endPiece);
        return retBoardRep;
    }

    public String[][] importBoard(ChessBoard board) {
        String[][] boardRep = new String[8][8];
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                boolean isDarkSquare = (row + col) % 2 == 0;

                if (piece == null) {
                    // Empty square
                    boardRep[row - 1][col - 1] = isDarkSquare
                            ? EscapeSequences.EMPTY_DARK_SQUARE
                            : EscapeSequences.EMPTY_LIGHT_SQUARE;
                } else {
                    // Square with a piece
                    boardRep[row - 1][col - 1] = renderPiece(piece, isDarkSquare);
                }
            }
        }
        return boardRep;
    }
}