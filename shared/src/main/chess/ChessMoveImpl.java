package chess;


import java.util.Collection;
import java.util.Objects;

public class ChessMoveImpl implements ChessMove
{
    private ChessPositionImpl startPos;
    private ChessPositionImpl endPos;

    ChessPiece.PieceType promoType;

    public ChessMoveImpl(ChessPositionImpl start, ChessPositionImpl end, ChessPiece.PieceType promotionPiece)
    {
        startPos = start;
        endPos = end;
        promoType = promotionPiece;
    }

    public ChessMoveImpl(ChessPositionImpl start, ChessPositionImpl end)
    {
        startPos = start;
        endPos = end;
    }

    @Override
    public ChessPositionImpl getStartPosition() { return startPos; }

    @Override
    public ChessPositionImpl getEndPosition() { return endPos; }

    @Override
    public ChessPiece.PieceType getPromotionPiece() { return promoType; }

    public static void checkValidMoveRight(ChessBoard board, ChessPositionImpl myPosition, Collection<ChessMoveImpl> validMoves, ChessPiece piece)
    {
        ChessPositionImpl nextPos = new ChessPositionImpl(myPosition.getRow(), myPosition.getColumn() + 1);

        while(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            nextPos = new ChessPositionImpl(nextPos.getRow(), nextPos.getColumn() + 1);
        }
        if (nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != piece.getTeamColor())
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
    }

    public static void checkValidMoveLeft(ChessBoard board, ChessPositionImpl myPosition, Collection<ChessMoveImpl> validMoves, ChessPiece piece)
    {
        //Check Left
        ChessPositionImpl nextPos = new ChessPositionImpl(myPosition.getRow(), myPosition.getColumn() - 1);

        while(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            nextPos = new ChessPositionImpl(nextPos.getRow(), nextPos.getColumn() - 1);
        }
        if (nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != piece.getTeamColor())
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
    }

    public static void checkValidMoveUp(ChessBoard board, ChessPositionImpl myPosition, Collection<ChessMoveImpl> validMoves, ChessPiece piece)
    {
        ChessPositionImpl nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn());

        while(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            nextPos = new ChessPositionImpl(nextPos.getRow() + 1, nextPos.getColumn());
        }
        if (nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != piece.getTeamColor())
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
    }

    public static void checkValidMoveDown(ChessBoard board, ChessPositionImpl myPosition, Collection<ChessMoveImpl> validMoves, ChessPiece piece)
    {
        ChessPositionImpl nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn());

        while(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            nextPos = new ChessPositionImpl(nextPos.getRow() - 1, nextPos.getColumn());
        }
        if (nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != piece.getTeamColor())
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
    }

    public static void checkValidMoveUpRight(ChessBoard board, ChessPositionImpl myPosition, Collection<ChessMoveImpl> validMoves, ChessPiece piece)
    {
        ChessPositionImpl nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn() + 1);

        while(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            nextPos = new ChessPositionImpl(nextPos.getRow() + 1, nextPos.getColumn() + 1);
        }
        if (nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != piece.getTeamColor())
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
    }

    public static void checkValidMoveUpLeft(ChessBoard board, ChessPositionImpl myPosition, Collection<ChessMoveImpl> validMoves, ChessPiece piece)
    {
        ChessPositionImpl nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn() - 1);

        while(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            nextPos = new ChessPositionImpl(nextPos.getRow() + 1, nextPos.getColumn() - 1);
        }
        if (nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != piece.getTeamColor())
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
    }

    public static void checkValidMoveDownRight(ChessBoard board, ChessPositionImpl myPosition, Collection<ChessMoveImpl> validMoves, ChessPiece piece)
    {
        ChessPositionImpl nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn() + 1);

        while(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            nextPos = new ChessPositionImpl(nextPos.getRow() - 1, nextPos.getColumn() + 1);
        }
        if (nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != piece.getTeamColor())
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
    }

    public static void checkValidMoveDownLeft(ChessBoard board, ChessPositionImpl myPosition, Collection<ChessMoveImpl> validMoves, ChessPiece piece)
    {
        ChessPositionImpl nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn() - 1);

        while(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            nextPos = new ChessPositionImpl(nextPos.getRow() - 1, nextPos.getColumn() - 1);
        }
        if (nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != piece.getTeamColor())
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMoveImpl chessMove = (ChessMoveImpl) o;
        return Objects.equals(startPos, chessMove.startPos) && Objects.equals(endPos, chessMove.endPos) && promoType == chessMove.promoType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPos, endPos, promoType);
    }
}
