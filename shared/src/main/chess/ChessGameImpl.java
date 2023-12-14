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
    public Collection<ChessMove> validMoves(ChessPosition startPosition)
    {
        ChessPiece piece = chessBoard.getBoard()[startPosition.getRow()][startPosition.getCol()];
        if (piece == null) { return null; }
        Collection<ChessMove> possibleMoves = piece.pieceMoves(chessBoard, startPosition);
        Collection<ChessMove> validMoves = new Vector<>();

        for (ChessMove move : possibleMoves)
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
        ChessPiece piece = chessBoard.getBoard()[startPos.getRow()][startPos.getCol()];
        ChessPiece deletedPiece = chessBoard.getBoard()[endPos.getRow()][endPos.getCol()];
        chessBoard.getBoard()[startPos.getRow()][startPos.getCol()] = null;
        chessBoard.getBoard()[endPos.getRow()][endPos.getCol()] = piece;
        return deletedPiece;
    }

    private void undoMove(ChessPosition startPos, ChessPosition endPos, ChessPiece deletedPiece)
    {
        ChessPiece piece = chessBoard.getBoard()[endPos.getRow()][endPos.getCol()];
        chessBoard.getBoard()[startPos.getRow()][startPos.getCol()] = piece;
        chessBoard.getBoard()[endPos.getRow()][endPos.getCol()] = deletedPiece;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException
    {
        if (teamTurn == null)
        {
            throw new InvalidMoveException("The game has ended. No more moves can be made.");
        }

        ChessPiece piece = chessBoard.getBoard()[move.getStartPosition().getRow()][move.getStartPosition().getCol()];
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

        if (validMoves == null || !validMoves.contains(move))
        {
            throw new InvalidMoveException("Invalid move. Try again.");
        }
        if (piece == null || teamTurn != piece.getTeamColor())
        {
            throw new InvalidMoveException("It is currently " + teamTurn + "'s move.");
        }



        //promotePawnCheck
        if (move.getPromotionPiece() != null && piece.getPieceType() == ChessPiece.PieceType.PAWN)
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

        chessBoard.getBoard()[move.getStartPosition().getRow()][move.getStartPosition().getCol()] = null;
        chessBoard.getBoard()[move.getEndPosition().getRow()][move.getEndPosition().getCol()] = piece;
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
        Collection<ChessMove> validMoves = new Vector<>();
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
