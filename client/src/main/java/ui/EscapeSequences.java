package ui;

/**
 * This class contains constants and functions relating to ANSI Escape Sequences that are useful in the Client display
 */
public class EscapeSequences {

    private static final String UNICODE_ESCAPE = "\u001b";
    private static final String SET_TEXT_COLOR = UNICODE_ESCAPE + "[38;5;";
    private static final String SET_BG_COLOR = UNICODE_ESCAPE + "[48;5;";

    public static final String SET_TEXT_COLOR_BLACK = SET_TEXT_COLOR + "0m";
    public static final String SET_TEXT_COLOR_LIGHT_GREY = SET_TEXT_COLOR + "242m";
    public static final String SET_TEXT_COLOR_DARK_GREY = SET_TEXT_COLOR + "235m";
    public static final String SET_TEXT_COLOR_RED = SET_TEXT_COLOR + "160m";
    public static final String SET_TEXT_COLOR_GREEN = SET_TEXT_COLOR + "46m";
    public static final String SET_TEXT_COLOR_YELLOW = SET_TEXT_COLOR + "226m";
    public static final String SET_TEXT_COLOR_BLUE = SET_TEXT_COLOR + "12m";
    public static final String SET_TEXT_COLOR_MAGENTA = SET_TEXT_COLOR + "5m";
    public static final String SET_TEXT_COLOR_WHITE = SET_TEXT_COLOR + "15m";
    public static final String RESET_TEXT_COLOR = UNICODE_ESCAPE + "[39m";

    public static final String SET_BG_COLOR_BLACK = SET_BG_COLOR + "0m";
    public static final String SET_BG_COLOR_LIGHT_GREY = SET_BG_COLOR + "242m";
    public static final String SET_BG_COLOR_DARK_GREY = SET_BG_COLOR + "235m";
    public static final String SET_BG_COLOR_RED = SET_BG_COLOR + "160m";
    public static final String SET_BG_COLOR_GREEN = SET_BG_COLOR + "46m";
    public static final String SET_BG_COLOR_DARK_GREEN = SET_BG_COLOR + "22m";
    public static final String SET_BG_COLOR_YELLOW = SET_BG_COLOR + "226m";
    public static final String SET_BG_COLOR_BLUE = SET_BG_COLOR + "12m";
    public static final String SET_BG_COLOR_MAGENTA = SET_BG_COLOR + "5m";
    public static final String SET_BG_COLOR_WHITE = SET_BG_COLOR + "15m";
    public static final String RESET_BG_COLOR = UNICODE_ESCAPE + "[49m";

    public static final String WHITE_KING = SET_TEXT_COLOR_WHITE+" K "+RESET_TEXT_COLOR;
    public static final String WHITE_QUEEN = SET_TEXT_COLOR_WHITE+" Q "+RESET_TEXT_COLOR;
    public static final String WHITE_BISHOP = SET_TEXT_COLOR_WHITE+" B "+RESET_TEXT_COLOR;
    public static final String WHITE_KNIGHT = SET_TEXT_COLOR_WHITE+" N "+RESET_TEXT_COLOR;
    public static final String WHITE_ROOK = SET_TEXT_COLOR_WHITE+" R "+RESET_TEXT_COLOR;
    public static final String WHITE_PAWN = SET_TEXT_COLOR_WHITE+" P "+RESET_TEXT_COLOR;
    public static final String BLACK_KING = SET_TEXT_COLOR_BLACK+" K "+RESET_TEXT_COLOR;
    public static final String BLACK_QUEEN = SET_TEXT_COLOR_BLACK+" Q "+RESET_TEXT_COLOR;
    public static final String BLACK_BISHOP = SET_TEXT_COLOR_BLACK+" B "+RESET_TEXT_COLOR;
    public static final String BLACK_KNIGHT = SET_TEXT_COLOR_BLACK+" N "+RESET_TEXT_COLOR;
    public static final String BLACK_ROOK = SET_TEXT_COLOR_BLACK+" R "+RESET_TEXT_COLOR;
    public static final String BLACK_PAWN = SET_TEXT_COLOR_BLACK+" P "+RESET_TEXT_COLOR;
    public static final String EMPTY = "   ";

    // Empty stuff
    public static final String EMPTY_DARK_SQUARE = SET_BG_COLOR_DARK_GREY+EMPTY+RESET_BG_COLOR;
    public static final String EMPTY_LIGHT_SQUARE = SET_BG_COLOR_LIGHT_GREY+EMPTY+RESET_BG_COLOR;
    public static final String EMPTY_BORDER_SQUARE = SET_BG_COLOR_BLACK+EMPTY+RESET_BG_COLOR;
    public static final String EMPTY_DARK_HIGHLIGHT_SQUARE = SET_BG_COLOR_DARK_GREEN+RESET_BG_COLOR;
    public static final String EMPTY_LIGHT_HIGHLIGHT_SQUARE = SET_BG_COLOR_GREEN+RESET_BG_COLOR;

    // Black filled squares
    public static final String DARK_SQUARE_BLACK_PAWN = SET_BG_COLOR_DARK_GREY+BLACK_PAWN+RESET_BG_COLOR;
    public static final String LIGHT_SQUARE_BLACK_PAWN = SET_BG_COLOR_LIGHT_GREY+BLACK_PAWN+RESET_BG_COLOR;
    public static final String DARK_SQUARE_BLACK_ROOK = SET_BG_COLOR_DARK_GREY+BLACK_ROOK+RESET_BG_COLOR;
    public static final String LIGHT_SQUARE_BLACK_ROOK = SET_BG_COLOR_LIGHT_GREY+BLACK_ROOK+RESET_BG_COLOR;
    public static final String DARK_SQUARE_BLACK_KNIGHT = SET_BG_COLOR_DARK_GREY+BLACK_KNIGHT+RESET_BG_COLOR;
    public static final String LIGHT_SQUARE_BLACK_KNIGHT = SET_BG_COLOR_LIGHT_GREY+BLACK_KNIGHT+RESET_BG_COLOR;
    public static final String DARK_SQUARE_BLACK_BISHOP = SET_BG_COLOR_DARK_GREY+BLACK_BISHOP+RESET_BG_COLOR;
    public static final String LIGHT_SQUARE_BLACK_BISHOP = SET_BG_COLOR_LIGHT_GREY+BLACK_BISHOP+RESET_BG_COLOR;
    public static final String DARK_SQUARE_BLACK_QUEEN = SET_BG_COLOR_DARK_GREY+BLACK_QUEEN+RESET_BG_COLOR;
    public static final String LIGHT_SQUARE_BLACK_QUEEN = SET_BG_COLOR_LIGHT_GREY+BLACK_QUEEN+RESET_BG_COLOR;
    public static final String DARK_SQUARE_BLACK_KING = SET_BG_COLOR_DARK_GREY+BLACK_KING+RESET_BG_COLOR;
    public static final String LIGHT_SQUARE_BLACK_KING = SET_BG_COLOR_LIGHT_GREY+BLACK_KING+RESET_BG_COLOR;

    // White filled squares
    public static final String DARK_SQUARE_WHITE_PAWN = SET_BG_COLOR_DARK_GREY+WHITE_PAWN+RESET_BG_COLOR;
    public static final String LIGHT_SQUARE_WHITE_PAWN = SET_BG_COLOR_LIGHT_GREY+WHITE_PAWN+RESET_BG_COLOR;
    public static final String DARK_SQUARE_WHITE_ROOK = SET_BG_COLOR_DARK_GREY+WHITE_ROOK+RESET_BG_COLOR;
    public static final String LIGHT_SQUARE_WHITE_ROOK = SET_BG_COLOR_LIGHT_GREY+WHITE_ROOK+RESET_BG_COLOR;
    public static final String DARK_SQUARE_WHITE_KNIGHT = SET_BG_COLOR_DARK_GREY+WHITE_KNIGHT+RESET_BG_COLOR;
    public static final String LIGHT_SQUARE_WHITE_KNIGHT = SET_BG_COLOR_LIGHT_GREY+WHITE_KNIGHT+RESET_BG_COLOR;
    public static final String DARK_SQUARE_WHITE_BISHOP = SET_BG_COLOR_DARK_GREY+WHITE_BISHOP+RESET_BG_COLOR;
    public static final String LIGHT_SQUARE_WHITE_BISHOP = SET_BG_COLOR_LIGHT_GREY+WHITE_BISHOP+RESET_BG_COLOR;
    public static final String DARK_SQUARE_WHITE_QUEEN = SET_BG_COLOR_DARK_GREY+WHITE_QUEEN+RESET_BG_COLOR;
    public static final String LIGHT_SQUARE_WHITE_QUEEN = SET_BG_COLOR_LIGHT_GREY+WHITE_QUEEN+RESET_BG_COLOR;
    public static final String DARK_SQUARE_WHITE_KING = SET_BG_COLOR_DARK_GREY+WHITE_KING+RESET_BG_COLOR;
    public static final String LIGHT_SQUARE_WHITE_KING = SET_BG_COLOR_LIGHT_GREY+WHITE_KING+RESET_BG_COLOR;

    // Column labels
    public static final String COLUMN_A = SET_BG_COLOR_BLACK+" A "+RESET_BG_COLOR;
    public static final String COLUMN_B = SET_BG_COLOR_BLACK+" B "+RESET_BG_COLOR;
    public static final String COLUMN_C = SET_BG_COLOR_BLACK+" C "+RESET_BG_COLOR;
    public static final String COLUMN_D = SET_BG_COLOR_BLACK+" D "+RESET_BG_COLOR;
    public static final String COLUMN_E = SET_BG_COLOR_BLACK+" E "+RESET_BG_COLOR;
    public static final String COLUMN_F = SET_BG_COLOR_BLACK+" F "+RESET_BG_COLOR;
    public static final String COLUMN_G = SET_BG_COLOR_BLACK+" G "+RESET_BG_COLOR;
    public static final String COLUMN_H = SET_BG_COLOR_BLACK+" H "+RESET_BG_COLOR;

    // Row labels
    public static final String ROW_1 = SET_BG_COLOR_BLACK+ " 1 " +RESET_BG_COLOR;
    public static final String ROW_2 = SET_BG_COLOR_BLACK+ " 2 " +RESET_BG_COLOR;
    public static final String ROW_3 = SET_BG_COLOR_BLACK+ " 3 " +RESET_BG_COLOR;
    public static final String ROW_4 = SET_BG_COLOR_BLACK+ " 4 " +RESET_BG_COLOR;
    public static final String ROW_5 = SET_BG_COLOR_BLACK+ " 5 " +RESET_BG_COLOR;
    public static final String ROW_6 = SET_BG_COLOR_BLACK+ " 6 " +RESET_BG_COLOR;
    public static final String ROW_7 = SET_BG_COLOR_BLACK+ " 7 " +RESET_BG_COLOR;
    public static final String ROW_8 = SET_BG_COLOR_BLACK+ " 8 " +RESET_BG_COLOR;

    // Piece highlights
    public static final String BLACK_HIGHLIGHT_PAWN = SET_BG_COLOR_YELLOW+BLACK_PAWN+RESET_BG_COLOR;
    public static final String WHITE_HIGHLIGHT_PAWN = SET_BG_COLOR_YELLOW+WHITE_PAWN+RESET_BG_COLOR;
    public static final String BLACK_HIGHLIGHT_ROOK = SET_BG_COLOR_YELLOW+BLACK_ROOK+RESET_BG_COLOR;
    public static final String WHITE_HIGHLIGHT_ROOK = SET_BG_COLOR_YELLOW+WHITE_ROOK+RESET_BG_COLOR;
    public static final String BLACK_HIGHLIGHT_BISHOP = SET_BG_COLOR_YELLOW+BLACK_BISHOP+RESET_BG_COLOR;
    public static final String WHITE_HIGHLIGHT_BISHOP = SET_BG_COLOR_YELLOW+WHITE_BISHOP+RESET_BG_COLOR;
    public static final String BLACK_HIGHLIGHT_KNIGHT = SET_BG_COLOR_YELLOW+BLACK_KNIGHT+RESET_BG_COLOR;
    public static final String WHITE_HIGHLIGHT_KNIGHT = SET_BG_COLOR_YELLOW+WHITE_KNIGHT+RESET_BG_COLOR;
    public static final String BLACK_HIGHLIGHT_QUEEN = SET_BG_COLOR_YELLOW+BLACK_QUEEN+RESET_BG_COLOR;
    public static final String WHITE_HIGHLIGHT_QUEEN = SET_BG_COLOR_YELLOW+WHITE_QUEEN+RESET_BG_COLOR;
    public static final String BLACK_HIGHLIGHT_KING = SET_BG_COLOR_YELLOW+BLACK_KING+RESET_BG_COLOR;
    public static final String WHITE_HIGHLIGHT_KING = SET_BG_COLOR_YELLOW+WHITE_KING+RESET_BG_COLOR;

    //Attacked pieces
    public static final String BLACK_ATTACKED_PAWN = SET_BG_COLOR_RED+BLACK_PAWN+RESET_BG_COLOR;
    public static final String WHITE_ATTACKED_PAWN = SET_BG_COLOR_RED+WHITE_PAWN+RESET_BG_COLOR;
    public static final String BLACK_ATTACKED_ROOK = SET_BG_COLOR_RED+BLACK_ROOK+RESET_BG_COLOR;
    public static final String WHITE_ATTACKED_ROOK = SET_BG_COLOR_RED+WHITE_ROOK+RESET_BG_COLOR;
    public static final String BLACK_ATTACKED_KNIGHT = SET_BG_COLOR_RED+BLACK_KNIGHT+RESET_BG_COLOR;
    public static final String WHITE_ATTACKED_KNIGHT = SET_BG_COLOR_RED+WHITE_KNIGHT+RESET_BG_COLOR;
    public static final String BLACK_ATTACKED_BISHOP = SET_BG_COLOR_RED+BLACK_BISHOP+RESET_BG_COLOR;
    public static final String WHITE_ATTACKED_BISHOP = SET_BG_COLOR_RED+WHITE_BISHOP+RESET_BG_COLOR;
    public static final String BLACK_ATTACKED_QUEEN = SET_BG_COLOR_RED+BLACK_QUEEN+RESET_BG_COLOR;
    public static final String WHITE_ATTACKED_QUEEN = SET_BG_COLOR_RED+WHITE_QUEEN+RESET_BG_COLOR;
    public static final String BLACK_ATTACKED_KING = SET_BG_COLOR_RED+BLACK_KING+RESET_BG_COLOR;
    public static final String WHITE_ATTACKED_KING = SET_BG_COLOR_RED+WHITE_KING+RESET_BG_COLOR;

}
