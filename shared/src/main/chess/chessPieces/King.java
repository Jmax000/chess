package chess.chessPieces;

import chess.*;

import java.util.Collection;
import java.util.Vector;

public class King implements ChessPiece
{
    private final ChessGame.TeamColor teamColor;
    public final PieceType pieceType = PieceType.KING;
    public King(ChessGame.TeamColor teamColor)
    {
        this.teamColor = teamColor;
    }
    @Override
    public ChessGame.TeamColor getTeamColor() { return teamColor; }

    @Override
    public PieceType getPieceType() { return PieceType.KING; }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        Collection<ChessMove> validMoves = new Vector<>();

        //Move up
        ChessPosition nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getCol());
        addValid(board, myPosition, nextPos, validMoves);

        //Move Down
        nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getCol());
        addValid(board, myPosition, nextPos, validMoves);

        //Move Right
        nextPos = new ChessPositionImpl(myPosition.getRow(), myPosition.getCol() + 1);
        addValid(board, myPosition, nextPos, validMoves);

        //Move Left
        nextPos = new ChessPositionImpl(myPosition.getRow(), myPosition.getCol() - 1);
        addValid(board, myPosition, nextPos, validMoves);

        //Move UpRight
        nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getCol() + 1);
        addValid(board, myPosition, nextPos, validMoves);

        //Move UpLeft
        nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getCol() - 1);
        addValid(board, myPosition, nextPos, validMoves);

        //Move DownRight
        nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getCol() + 1);
        addValid(board, myPosition, nextPos, validMoves);

        //Move DownLeft
        nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getCol() - 1);
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
