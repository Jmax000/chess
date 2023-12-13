package chess;


import java.util.Collection;
import java.util.Objects;

public class ChessMoveImpl implements ChessMove
{
    private final ChessPosition startPos;
    private final ChessPosition endPos;

    ChessPiece.PieceType promoType;

    public ChessMoveImpl(ChessPosition start, ChessPosition end, ChessPiece.PieceType promotionPiece)
    {
        startPos = start;
        endPos = end;
        promoType = promotionPiece;
    }

    public ChessMoveImpl(ChessPosition start, ChessPosition end)
    {
        startPos = start;
        endPos = end;
    }

    @Override
    public ChessPosition getStartPosition() { return startPos; }

    @Override
    public ChessPosition getEndPosition() { return endPos; }

    @Override
    public ChessPiece.PieceType getPromotionPiece() { return promoType; }

    public static void checkValidMoveRight(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, ChessPiece piece)
    {
        ChessPosition nextPos = new ChessPositionImpl(myPosition.getRow(), myPosition.getCol() + 1);

        while(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            nextPos = new ChessPositionImpl(nextPos.getRow(), nextPos.getCol() + 1);
        }
        if (nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != piece.getTeamColor())
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
    }

    public static void checkValidMoveLeft(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, ChessPiece piece)
    {
        //Check Left
        ChessPosition nextPos = new ChessPositionImpl(myPosition.getRow(), myPosition.getCol() - 1);

        while(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            nextPos = new ChessPositionImpl(nextPos.getRow(), nextPos.getCol() - 1);
        }
        if (nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != piece.getTeamColor())
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
    }

    public static void checkValidMoveUp(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, ChessPiece piece)
    {
        ChessPosition nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getCol());

        while(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            nextPos = new ChessPositionImpl(nextPos.getRow() + 1, nextPos.getCol());
        }
        if (nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != piece.getTeamColor())
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
    }

    public static void checkValidMoveDown(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, ChessPiece piece)
    {
        ChessPosition nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getCol());

        while(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            nextPos = new ChessPositionImpl(nextPos.getRow() - 1, nextPos.getCol());
        }
        if (nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != piece.getTeamColor())
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
    }

    public static void checkValidMoveUpRight(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, ChessPiece piece)
    {
        ChessPosition nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getCol() + 1);

        while(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            nextPos = new ChessPositionImpl(nextPos.getRow() + 1, nextPos.getCol() + 1);
        }
        if (nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != piece.getTeamColor())
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
    }

    public static void checkValidMoveUpLeft(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, ChessPiece piece)
    {
        ChessPosition nextPos = new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getCol() - 1);

        while(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            nextPos = new ChessPositionImpl(nextPos.getRow() + 1, nextPos.getCol() - 1);
        }
        if (nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != piece.getTeamColor())
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
    }

    public static void checkValidMoveDownRight(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, ChessPiece piece)
    {
        ChessPosition nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getCol() + 1);

        while(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            nextPos = new ChessPositionImpl(nextPos.getRow() - 1, nextPos.getCol() + 1);
        }
        if (nextPos.validPos() && board.getPiece(nextPos) != null && board.getPiece(nextPos).getTeamColor() != piece.getTeamColor())
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
        }
    }

    public static void checkValidMoveDownLeft(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, ChessPiece piece)
    {
        ChessPosition nextPos = new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getCol() - 1);

        while(nextPos.validPos() && board.getPiece(nextPos) == null)
        {
            validMoves.add(new ChessMoveImpl(myPosition, nextPos));
            nextPos = new ChessPositionImpl(nextPos.getRow() - 1, nextPos.getCol() - 1);
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
