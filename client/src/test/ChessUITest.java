import chess.*;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static ui.ChessBoardUI.drawChessBoard;

public class ChessUITest
{
    @Test
    public void testDrawBoard()
    {
        ChessGame game = new ChessGameImpl();
        game.getBoard().resetBoard();
        Collection<ChessMove> moves = game.validMoves(new ChessPositionImpl(1, 0));
        System.out.print(drawChessBoard(game.getBoard(), ChessGame.TeamColor.WHITE, moves));
    }
}
