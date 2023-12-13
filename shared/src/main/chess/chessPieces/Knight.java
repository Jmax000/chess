package chess.chessPieces;

import chess.*;

import java.util.Collection;
import java.util.Vector;

public class Knight implements ChessPiece
{
    private final ChessGame.TeamColor teamColor;
    public final PieceType pieceType = PieceType.KNIGHT;
    public Knight(ChessGame.TeamColor teamColor) { this.teamColor = teamColor; }
    @Override
    public ChessGame.TeamColor getTeamColor() { return teamColor; }

    @Override
    public PieceType getPieceType() { return PieceType.KNIGHT; }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        Collection<ChessMove> validMoves = new Vector<>();

        //Move 2Up1Right
        ChessPosition nextPos = new ChessPositionImpl(myPosition.getRow() + 2, myPosition.getCol() + 1);
        addValid(board, myPosition, nextPos, validMoves);

        //Move 1Up2Right
        nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getCol() + 2);
        addValid(board, myPosition, nextPos, validMoves);

        //Move 2Up1Left
        nextPos = new ChessPositionImpl(myPosition.getRow() + 2, myPosition.getCol() - 1);
        addValid(board, myPosition, nextPos, validMoves);

        //Move 1Up2Left
        nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getCol() - 2);
        addValid(board, myPosition, nextPos, validMoves);

        //Move 2Down1Right
        nextPos = new ChessPositionImpl(myPosition.getRow() - 2, myPosition.getCol() + 1);
        addValid(board, myPosition, nextPos, validMoves);

        //Move 1Down2Right
        nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getCol() + 2);
        addValid(board, myPosition, nextPos, validMoves);

        //Move 2Down1Left
        nextPos = new ChessPositionImpl(myPosition.getRow() - 2, myPosition.getCol() - 1);
        addValid(board, myPosition, nextPos, validMoves);

        //Move 1Down2Left
        nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getCol() - 2);
        addValid(board, myPosition, nextPos, validMoves);

        return validMoves;
    }

    private void addValid(ChessBoard board, ChessPosition myPosition, ChessPosition nextPos, Collection<ChessMove> validMoves)
    {
        if(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
        else if (nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != teamColor)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
    }
}
