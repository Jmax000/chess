package chess.chessPieces;

import chess.*;

import java.util.Collection;
import java.util.Vector;

public class Queen implements ChessPiece
{
    private ChessGame.TeamColor teamColor;
    public Queen(ChessGame.TeamColor teamColor) { this.teamColor = teamColor; }
    @Override
    public ChessGame.TeamColor getTeamColor() { return teamColor; }

    @Override
    public PieceType getPieceType() { return PieceType.QUEEN; }

    @Override
    public Collection<ChessMoveImpl> pieceMoves(ChessBoard board, ChessPositionImpl myPosition)
    {
        Collection<ChessMoveImpl> validMoves = new Vector<>();
        ChessMoveImpl.checkValidMoveRight(board, myPosition, validMoves, this);
        ChessMoveImpl.checkValidMoveLeft(board, myPosition, validMoves, this);
        ChessMoveImpl.checkValidMoveUp(board, myPosition, validMoves, this);
        ChessMoveImpl.checkValidMoveDown(board, myPosition, validMoves, this);

        ChessMoveImpl.checkValidMoveUpRight(board, myPosition, validMoves, this);
        ChessMoveImpl.checkValidMoveUpLeft(board, myPosition, validMoves, this);
        ChessMoveImpl.checkValidMoveDownRight(board, myPosition, validMoves, this);
        ChessMoveImpl.checkValidMoveDownLeft(board, myPosition, validMoves, this);

        return validMoves;
    }
}
