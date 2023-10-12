package chess.chessPieces;

import chess.*;

import java.util.Collection;
import java.util.Vector;

public class Pawn implements ChessPiece
{
    private ChessGame.TeamColor teamColor;
    public Pawn(ChessGame.TeamColor teamColor) { this.teamColor = teamColor; }
    @Override
    public ChessGame.TeamColor getTeamColor() { return teamColor; }

    @Override
    public PieceType getPieceType() { return PieceType.PAWN; }

    @Override
    public Collection<ChessMoveImpl> pieceMoves(ChessBoard board, ChessPositionImpl myPosition)
    {
        Collection<ChessMoveImpl> validMoves = new Vector<>();

        if(teamColor == ChessGame.TeamColor.WHITE)
        {
            //Move up
            ChessPositionImpl nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn());

            //Pawn hasn't moved Check
            if (myPosition.getRow() == 1)
            {
                ChessPositionImpl moveTwo = new ChessPositionImpl(myPosition.getRow() + 2, myPosition.getColumn());
                if(moveTwo.validPos() && board.getPiece(nextPos) == null && board.getPiece(moveTwo) == null)
                {
                    validMoves.add(new ChessMoveImpl(myPosition, moveTwo));
                }
            }
            if(nextPos.validPos() && board.getPiece(nextPos) == null && !checkPromotion(myPosition, nextPos, validMoves))
            {
                validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            }

            //Take upRight
            nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            if(nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != teamColor && !checkPromotion(myPosition, nextPos, validMoves))
            {
                validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            }

            //Take upLeft
            nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            if(nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != teamColor && !checkPromotion(myPosition, nextPos, validMoves))
            {
                validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            }
        }
        else
        {
            //Move Down
            ChessPositionImpl nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn());

            //Pawn hasn't moved Check
            if (myPosition.getRow() == 6)
            {
                ChessPositionImpl moveTwo = new ChessPositionImpl(myPosition.getRow() - 2, myPosition.getColumn());
                if(moveTwo.validPos() && board.getPiece(nextPos) == null && board.getPiece(moveTwo) == null)
                {
                    validMoves.add(new ChessMoveImpl(myPosition, moveTwo));
                }
            }

            if(nextPos.validPos() && board.getPiece(nextPos) == null && !checkPromotion(myPosition, nextPos, validMoves))
            {
                validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            }

            //Take DownRight
            nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            if(nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != teamColor && !checkPromotion(myPosition, nextPos, validMoves))
            {
                validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            }

            //Take DownLeft
            nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            if(nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != teamColor && !checkPromotion(myPosition, nextPos, validMoves))
            {
                validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            }
        }

        return validMoves;
    }

    private boolean checkPromotion(ChessPositionImpl myPosition, ChessPositionImpl nextPos, Collection<ChessMoveImpl> validMoves)
    {
        if(nextPos.validPos() && nextPos.getRow() == 7 && teamColor == ChessGame.TeamColor.WHITE)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos, PieceType.QUEEN));
            validMoves.add(new ChessMoveImpl(myPosition, nextPos, PieceType.BISHOP));
            validMoves.add(new ChessMoveImpl(myPosition, nextPos, PieceType.ROOK));
            validMoves.add(new ChessMoveImpl(myPosition, nextPos, PieceType.KNIGHT));
            return true;
        }
        else if(nextPos.validPos() && nextPos.getRow() == 0 && teamColor == ChessGame.TeamColor.BLACK)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos, PieceType.QUEEN));
            validMoves.add(new ChessMoveImpl(myPosition, nextPos, PieceType.BISHOP));
            validMoves.add(new ChessMoveImpl(myPosition, nextPos, PieceType.ROOK));
            validMoves.add(new ChessMoveImpl(myPosition, nextPos, PieceType.KNIGHT));
            return true;
        }
        return false;
    }
}


