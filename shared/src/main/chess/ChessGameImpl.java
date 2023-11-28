package chess;

import chess.chessPieces.*;

import java.util.Collection;
import java.util.Vector;

public class ChessGameImpl implements ChessGame
{
    private ChessBoard chessBoard = new ChessBoardImpl();
    private TeamColor teamTurn;
    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) { teamTurn = team; }

    @Override
    public Collection<ChessMoveImpl> validMoves(ChessPositionImpl startPosition)
    {
        ChessPiece piece = chessBoard.getBoard()[startPosition.getRow()][startPosition.getColumn()];
        if (piece == null) { return null; }
        Collection<ChessMoveImpl> possibleMoves = piece.pieceMoves(chessBoard, startPosition);
        Collection<ChessMoveImpl> validMoves = new Vector<>();

        for (ChessMoveImpl move : possibleMoves)
        {
            ChessPiece deletedPiece = tempMove(move.getStartPosition(), move.getEndPosition());
            if (!isInCheck(piece.getTeamColor()))
            {
                validMoves.add(move);
            }
            undoMove(move.getStartPosition(), move.getEndPosition(), deletedPiece);
        }

        return validMoves;
    }

    private ChessPiece tempMove(ChessPosition startPos, ChessPosition endPos)
    {
        ChessPiece piece = chessBoard.getBoard()[startPos.getRow()][startPos.getColumn()];
        ChessPiece deletedPiece = chessBoard.getBoard()[endPos.getRow()][endPos.getColumn()];
        chessBoard.getBoard()[startPos.getRow()][startPos.getColumn()] = null;
        chessBoard.getBoard()[endPos.getRow()][endPos.getColumn()] = piece;
        return deletedPiece;
    }

    private void undoMove(ChessPosition startPos, ChessPosition endPos, ChessPiece deletedPiece)
    {
        ChessPiece piece = chessBoard.getBoard()[endPos.getRow()][endPos.getColumn()];
        chessBoard.getBoard()[startPos.getRow()][startPos.getColumn()] = piece;
        chessBoard.getBoard()[endPos.getRow()][endPos.getColumn()] = deletedPiece;
    }

    @Override
    public void makeMove(ChessMoveImpl move) throws InvalidMoveException
    {
        ChessPiece piece = chessBoard.getBoard()[move.getStartPosition().getRow()][move.getStartPosition().getColumn()];
        Collection<ChessMoveImpl> validMoves = validMoves(move.getStartPosition());

        if (!validMoves.contains(move))
        {
            throw new InvalidMoveException("Throw");
        }
        if (teamTurn != piece.getTeamColor())
        {
            throw new InvalidMoveException("Out of turn");
        }

        //promotePawnCheck
        if (move.promoType != null && piece.getPieceType() == ChessPiece.PieceType.PAWN)
        {
            if (piece.getTeamColor() == TeamColor.WHITE && move.getEndPosition().getRow() == 7)
            {
                if (move.getPromotionPiece() == ChessPiece.PieceType.QUEEN) { piece = new Queen(piece.getTeamColor()); }
                else if (move.getPromotionPiece() == ChessPiece.PieceType.BISHOP) { piece = new Bishop(piece.getTeamColor()); }
                else if (move.getPromotionPiece() == ChessPiece.PieceType.KNIGHT) { piece = new Knight(piece.getTeamColor()); }
                else if (move.getPromotionPiece() == ChessPiece.PieceType.ROOK) { piece = new Rook(piece.getTeamColor()); }
            }
            if (piece.getTeamColor() == TeamColor.BLACK && move.getEndPosition().getRow() == 0)
            {
                if (move.getPromotionPiece() == ChessPiece.PieceType.QUEEN) { piece = new Queen(piece.getTeamColor()); }
                else if (move.getPromotionPiece() == ChessPiece.PieceType.BISHOP) { piece = new Bishop(piece.getTeamColor()); }
                else if (move.getPromotionPiece() == ChessPiece.PieceType.KNIGHT) { piece = new Knight(piece.getTeamColor()); }
                else if (move.getPromotionPiece() == ChessPiece.PieceType.ROOK) { piece = new Rook(piece.getTeamColor()); }
            }
        }

        chessBoard.getBoard()[move.getStartPosition().getRow()][move.getStartPosition().getColumn()] = null;
        chessBoard.getBoard()[move.getEndPosition().getRow()][move.getEndPosition().getColumn()] = piece;
        teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK:TeamColor.WHITE;
    }

    @Override
    public boolean isInCheck(TeamColor teamColor)
    {
        ChessBoardImpl temp = (ChessBoardImpl) chessBoard;

        return temp.isInCheck(teamColor);
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor)
    {
        return isInCheck(teamColor) && isInStalemate(teamColor);
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor)
    {
        ChessPiece[][] board = chessBoard.getBoard();
        Collection<ChessMoveImpl> validMoves = new Vector<>();
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board.length; j++)
            {
                if (board[i][j] != null && board[i][j].getTeamColor() == teamColor)
                {
                    validMoves.addAll(validMoves(new ChessPositionImpl(i, j)));
                }
            }
        }

        return /*teamTurn == teamColor &&*/  validMoves.isEmpty();
    }

    @Override
    public void setBoard(ChessBoard board) { chessBoard = board; }

    @Override
    public ChessBoard getBoard() { return chessBoard; }
}
