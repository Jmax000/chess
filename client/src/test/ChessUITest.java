import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessGameImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static ui.ChessBoardUI.drawChessBoard;

public class ChessUITest
{
    @Test
    public void testDrawBoard()
    {
        ChessGame game = new ChessGameImpl();
        game.getBoard().resetBoard();
        System.out.print(drawChessBoard(game.getBoard(), ChessGame.TeamColor.WHITE));
    }
}
