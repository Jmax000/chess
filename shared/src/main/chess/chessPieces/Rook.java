package chess.chessPieces;

import chess.*;

import java.util.Collection;
import java.util.Vector;

public class Rook implements ChessPiece
{
    private final ChessGame.TeamColor teamColor;
    public final PieceType pieceType = PieceType.ROOK;
    public Rook(ChessGame.TeamColor teamColor) { this.teamColor = teamColor; }
    @Override
    public ChessGame.TeamColor getTeamColor() { return teamColor; }

    @Override
    public PieceType getPieceType() { return PieceType.ROOK; }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        Collection<ChessMove> validMoves = new Vector<>();
        ChessMoveImpl.checkValidMoveRight(board, myPosition, validMoves, this);
        ChessMoveImpl.checkValidMoveLeft(board, myPosition, validMoves, this);
        ChessMoveImpl.checkValidMoveUp(board, myPosition, validMoves, this);
        ChessMoveImpl.checkValidMoveDown(board, myPosition, validMoves, this);

        return validMoves;
    }
}
