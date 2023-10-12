package chess.chessPieces;

import chess.*;

import java.util.Collection;
import java.util.Vector;

public class King implements ChessPiece
{
    private ChessGame.TeamColor teamColor;
    public King(ChessGame.TeamColor teamColor) { this.teamColor = teamColor; }
    @Override
    public ChessGame.TeamColor getTeamColor() { return teamColor; }

    @Override
    public PieceType getPieceType() { return PieceType.KING; }

    @Override
    public Collection<ChessMoveImpl> pieceMoves(ChessBoard board, ChessPositionImpl myPosition)
    {
        Collection<ChessMoveImpl> validMoves = new Vector<>();
        ChessBoardImpl currentBoard = (ChessBoardImpl)board;

        //Move up
        ChessPositionImpl nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn());
        addValid(currentBoard, myPosition, nextPos, validMoves);

        //Move Down
        nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn());
        addValid(currentBoard, myPosition, nextPos, validMoves);

        //Move Right
        nextPos = new ChessPositionImpl(myPosition.getRow(), myPosition.getColumn() + 1);
        addValid(currentBoard, myPosition, nextPos, validMoves);

        //Move Left
        nextPos = new ChessPositionImpl(myPosition.getRow(), myPosition.getColumn() - 1);
        addValid(currentBoard, myPosition, nextPos, validMoves);

        //Move UpRight
        nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        addValid(currentBoard, myPosition, nextPos, validMoves);

        //Move UpLeft
        nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        addValid(currentBoard, myPosition, nextPos, validMoves);

        //Move DownRight
        nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        addValid(currentBoard, myPosition, nextPos, validMoves);

        //Move DownLeft
        nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        addValid(currentBoard, myPosition, nextPos, validMoves);

        return validMoves;
    }

    private void addValid(ChessBoardImpl board, ChessPositionImpl myPosition, ChessPositionImpl nextPos, Collection<ChessMoveImpl> validMoves)
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
