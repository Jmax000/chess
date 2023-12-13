package ui;

import chess.*;
import static ui.EscapeSequences.*;

public class ChessBoardUI
{
    private static final int BOARD_LENGTH_IN_SQUARES = 8;
    private static final int SQUARE_LENGTH_IN_CHARS = 3;
    private static final String WHITE_COLOR = SET_TEXT_COLOR_BLUE;
    private static final String BLACK_COLOR = SET_TEXT_COLOR_RED;
    private static final String EMPTY = "   ";
    private static final String KING_STRING = " K ";
    private static final String QUEEN_STRING = " Q ";
    private static final String BISHOP_STRING = " B ";
    private static final String KNIGHT_STRING = " N ";
    private static final String ROOK_STRING = " R ";
    private static final String PAWN_STRING = " P ";

    public static String drawChessBoard(ChessBoard board, ChessGame.TeamColor color)
    {
        var out = new StringBuilder();
        out.append(SET_TEXT_BOLD);
        drawBoard(out, board, color);
        out.append(RESET_TEXT_BOLD_FAINT);
        return out.toString();
    }

    private static void drawBoard(StringBuilder out, ChessBoard board, ChessGame.TeamColor color)
    {
        drawHeader(out, color);
        for (int boardRow = 0; boardRow < BOARD_LENGTH_IN_SQUARES; boardRow++)
        {
            drawRow(out, boardRow, board, color);
        }
        drawHeader(out, color);
    }

    private static void drawRow(StringBuilder out, int boardRow, ChessBoard board, ChessGame.TeamColor color)
    {
        setBoarderColor(out);
        if (color == ChessGame.TeamColor.WHITE)
        {
            out.append(" ").append(BOARD_LENGTH_IN_SQUARES - boardRow).append(" ");
        }
        else
        {
            out.append(" ").append(boardRow + 1).append(" ");
        }

        for (int boardCol = 0; boardCol < BOARD_LENGTH_IN_SQUARES; boardCol++)
        {
            if ((boardRow % 2 == 0 && boardCol % 2 == 0) || (boardRow % 2 != 0 && boardCol % 2 != 0))
            {
                setWhite(out);
            }
            else
            {
                setBlack(out);
            }

            ChessPiece piece;
            if (color == ChessGame.TeamColor.WHITE)
            {
                piece = board.getPiece(new ChessPositionImpl((BOARD_LENGTH_IN_SQUARES - 1 - boardRow),(BOARD_LENGTH_IN_SQUARES - 1 - boardCol)));
            }
            else
            {
                piece = board.getPiece(new ChessPositionImpl(boardRow, boardCol));
            }

            if (piece != null)
            {
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                {
                    out.append(WHITE_COLOR);
                }
                else
                {
                    out.append(BLACK_COLOR);
                }

                switch (piece.getPieceType())
                {
                    case KING -> out.append(KING_STRING);
                    case QUEEN -> out.append(QUEEN_STRING);
                    case BISHOP -> out.append(BISHOP_STRING);
                    case KNIGHT -> out.append(KNIGHT_STRING);
                    case ROOK -> out.append(ROOK_STRING);
                    case PAWN -> out.append(PAWN_STRING);
                }
            }
            else
            {
                out.append(EMPTY);
            }
        }

        setBoarderColor(out);
        if (color == ChessGame.TeamColor.WHITE)
        {
            out.append(" ").append(BOARD_LENGTH_IN_SQUARES - boardRow).append(" ");
        }
        else
        {
            out.append(" ").append(boardRow + 1).append(" ");
        }
        out.append(SET_BG_COLOR_DARK_GREY); //FIXME
        out.append("\n");
    }

    private static void drawHeader(StringBuilder out, ChessGame.TeamColor color)
    {
        setBoarderColor(out);
        out.append(EMPTY);
        for (int boardCol = 0; boardCol < BOARD_LENGTH_IN_SQUARES; boardCol++)
        {
            if (color == ChessGame.TeamColor.WHITE)
            {
                out.append(" ").append((char)('a' + boardCol)).append(" ");
            }
            else
            {
                out.append(" ").append((char)('h' - boardCol)).append(" ");
            }
        }
        out.append(EMPTY);
        out.append(SET_BG_COLOR_DARK_GREY); //FIXME
        out.append("\n");
    }

    private static void setBoarderColor(StringBuilder out)
    {
        out.append(SET_BG_COLOR_LIGHT_GREY);
        out.append(SET_TEXT_COLOR_BLACK);
    }
    private static void setWhite(StringBuilder out)
    {
        out.append(SET_BG_COLOR_WHITE);
    }
    private static void setBlack(StringBuilder out)
    {
        out.append(SET_BG_COLOR_BLACK);
    }
}
