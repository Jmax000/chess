package chess.chessPieces;

import chess.*;

import java.util.Collection;
import java.util.Vector;

public class Bishop implements ChessPiece
{
    private final ChessGame.TeamColor teamColor;
    public final PieceType pieceType = PieceType.BISHOP;
    public Bishop(ChessGame.TeamColor teamColor) { this.teamColor = teamColor; }
    @Override
    public ChessGame.TeamColor getTeamColor() { return teamColor; }

    @Override
    public PieceType getPieceType() { return PieceType.BISHOP; }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        Collection<ChessMove> validMoves = new Vector<>();
        ChessMoveImpl.checkValidMoveUpRight(board, myPosition, validMoves, this);
        ChessMoveImpl.checkValidMoveUpLeft(board, myPosition, validMoves, this);
        ChessMoveImpl.checkValidMoveDownRight(board, myPosition, validMoves, this);
        ChessMoveImpl.checkValidMoveDownLeft(board, myPosition, validMoves, this);

        return validMoves;
    }

}
