package chess;

import chess.chessPieces.*;

import java.util.Collection;
import java.util.Vector;

public class ChessBoardImpl implements ChessBoard
{
    public static int BOARD_SIZE = 8;

    private ChessPiece[][] board = new ChessPiece[BOARD_SIZE][BOARD_SIZE];

    //public ChessBoardImpl() { resetBoard(); }
    @Override
    public void addPiece(ChessPositionImpl position, ChessPiece piece)
    {
        board[position.getRow()][position.getColumn()] = piece;
    }

    @Override
    public ChessPiece getPiece(ChessPositionImpl position)
    {
        return board[position.getRow()][position.getColumn()];
    }

    @Override
    public ChessPiece[][] getBoard() {
        return board;
    }

    @Override
    public void resetBoard()
    {
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board.length; j++)
            {
                board[i][j] = null;
            }
        }

        int WHITE_MAIN_ROW = 0;
        int WHITE_PAWN_ROW = 1;
        int BLACK_PAWN_ROW = 6;
        int BLACK_MAIN_ROW = 7;


        //Add White Pieces
        board[WHITE_MAIN_ROW][0] = new Rook(ChessGame.TeamColor.WHITE);
        board[WHITE_MAIN_ROW][1] = new Knight(ChessGame.TeamColor.WHITE);
        board[WHITE_MAIN_ROW][2] = new Bishop(ChessGame.TeamColor.WHITE);
        board[WHITE_MAIN_ROW][3] = new Queen(ChessGame.TeamColor.WHITE);
        board[WHITE_MAIN_ROW][4] = new King(ChessGame.TeamColor.WHITE);
        board[WHITE_MAIN_ROW][5] = new Bishop(ChessGame.TeamColor.WHITE);
        board[WHITE_MAIN_ROW][6] = new Knight(ChessGame.TeamColor.WHITE);
        board[WHITE_MAIN_ROW][7] = new Rook(ChessGame.TeamColor.WHITE);

        //Add Black Pieces
        board[BLACK_MAIN_ROW][0] = new Rook(ChessGame.TeamColor.BLACK);
        board[BLACK_MAIN_ROW][1] = new Knight(ChessGame.TeamColor.BLACK);
        board[BLACK_MAIN_ROW][2] = new Bishop(ChessGame.TeamColor.BLACK);
        board[BLACK_MAIN_ROW][3] = new Queen(ChessGame.TeamColor.BLACK);
        board[BLACK_MAIN_ROW][4] = new King(ChessGame.TeamColor.BLACK);
        board[BLACK_MAIN_ROW][5] = new Bishop(ChessGame.TeamColor.BLACK);
        board[BLACK_MAIN_ROW][6] = new Knight(ChessGame.TeamColor.BLACK);
        board[BLACK_MAIN_ROW][7] = new Rook(ChessGame.TeamColor.BLACK);

        //Add White Pawns
        for (int i = 0; i < BOARD_SIZE; i++)
        {
            board[WHITE_PAWN_ROW][i] = new Pawn(ChessGame.TeamColor.WHITE);
        }

        //Add Black Pawns
        for (int i = 0; i < BOARD_SIZE; i++)
        {
            board[BLACK_PAWN_ROW][i] = new Pawn(ChessGame.TeamColor.BLACK);
        }
    }

    public boolean isInCheck(ChessGame.TeamColor teamColor)
    {
        Collection<ChessMoveImpl> validMoves = new Vector<>();
        ChessPositionImpl kingPos = null;
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board.length; j++)
            {
                if (board[i][j] != null && board[i][j].getTeamColor() != teamColor)
                {
                    validMoves.addAll(board[i][j].pieceMoves(this, new ChessPositionImpl(i, j)));
                }
                // Find King
                if (board[i][j] != null && board[i][j].getPieceType() == ChessPiece.PieceType.KING && board[i][j].getTeamColor() == teamColor)
                {
                    kingPos = new ChessPositionImpl(i, j);
                }
            }
        }

        for (ChessMoveImpl move : validMoves)
        {
            if (kingPos != null && move.getEndPosition().getRow() == kingPos.getRow() && move.getEndPosition().getColumn() == kingPos.getColumn())
            {
                return true;
            }
        }

        return false;
    }
}

