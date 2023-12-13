import chess.*;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static ui.ChessBoardUI.drawChessBoard;

public class ChessUITest
{
    @Test
    public void testDrawBoard()
    {
//        ChessGame game = new ChessGameImpl();
//        game.getBoard().resetBoard();
//        Collection<ChessMove> moves = game.validMoves(new ChessPositionImpl(1, 0));
//        System.out.print(drawChessBoard(game.getBoard(), ChessGame.TeamColor.WHITE, moves));
        ChessGame game = new ChessGameImpl();
        game.getBoard().resetBoard();
        System.out.print(displayMove(game, new ChessPositionImpl(0,0)));
    }

    private static String displayMove(ChessGame game, ChessPosition endPos)
    {
        ChessPiece piece = game.getBoard().getBoard()[endPos.getRow()][endPos.getCol()];
        String pieceType = switch (piece.getPieceType())
        {
            case KING -> "K";
            case QUEEN -> "Q";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case ROOK -> "R";
            case PAWN -> "P";
        };
        int row = endPos.getRow() + 1;
        char col = (char)(endPos.getCol() + 'a');

        return pieceType + col + row;
    }
}
