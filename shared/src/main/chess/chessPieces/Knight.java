package chess.chessPieces;

import chess.*;

import java.util.Collection;
import java.util.Vector;

public class Knight implements ChessPiece
{
    private ChessGame.TeamColor teamColor;
    public Knight(ChessGame.TeamColor teamColor) { this.teamColor = teamColor; }
    @Override
    public ChessGame.TeamColor getTeamColor() { return teamColor; }

    @Override
    public PieceType getPieceType() { return PieceType.KNIGHT; }

    @Override
    public Collection<ChessMoveImpl> pieceMoves(ChessBoard board, ChessPositionImpl myPosition)
    {
        Collection<ChessMoveImpl> validMoves = new Vector<>();

        //Move 2Up1Right
        ChessPositionImpl nextPos = new ChessPositionImpl(myPosition.getRow() + 2, myPosition.getColumn() + 1);
        addValid(board, myPosition, nextPos, validMoves);

        //Move 1Up2Right
        nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn() + 2);
        addValid(board, myPosition, nextPos, validMoves);

        //Move 2Up1Left
        nextPos = new ChessPositionImpl(myPosition.getRow() + 2, myPosition.getColumn() - 1);
        addValid(board, myPosition, nextPos, validMoves);

        //Move 1Up2Left
        nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn() - 2);
        addValid(board, myPosition, nextPos, validMoves);

        //Move 2Down1Right
        nextPos = new ChessPositionImpl(myPosition.getRow() - 2, myPosition.getColumn() + 1);
        addValid(board, myPosition, nextPos, validMoves);

        //Move 1Down2Right
        nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn() + 2);
        addValid(board, myPosition, nextPos, validMoves);

        //Move 2Down1Left
        nextPos = new ChessPositionImpl(myPosition.getRow() - 2, myPosition.getColumn() - 1);
        addValid(board, myPosition, nextPos, validMoves);

        //Move 1Down2Left
        nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn() - 2);
        addValid(board, myPosition, nextPos, validMoves);

        return validMoves;
    }

    private void addValid(ChessBoard board, ChessPositionImpl myPosition, ChessPositionImpl nextPos, Collection<ChessMoveImpl> validMoves)
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
