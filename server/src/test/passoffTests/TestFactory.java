package passoffTests;

import chess.*;
import chess.chessPieces.*;

/**
 * Used for testing your code
 * Add in code using your classes for each method for each FIXME
 */
public class TestFactory {

    //Chess Functions
    //------------------------------------------------------------------------------------------------------------------
    public static ChessBoard getNewBoard(){ return new ChessBoardImpl(); }

    public static ChessGame getNewGame(){ return new ChessGameImpl(); }

    public static ChessPiece getNewPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type)
    {
		if (type == ChessPiece.PieceType.KING) { return new King(pieceColor); }
        else if (type == ChessPiece.PieceType.QUEEN) { return new Queen(pieceColor); }
        else if (type == ChessPiece.PieceType.BISHOP) { return new Bishop(pieceColor); }
        else if (type == ChessPiece.PieceType.KNIGHT) { return new Knight(pieceColor); }
        else if (type == ChessPiece.PieceType.ROOK) { return new Rook(pieceColor); }
        else { return new Pawn(pieceColor); }
    }

    public static ChessPosition getNewPosition(Integer row, Integer col)
    {
        return new ChessPositionImpl(row - 1, col - 1);
    }

    public static ChessMove getNewMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece)
    {
		return new ChessMoveImpl(startPosition, endPosition, promotionPiece);
    }
    //------------------------------------------------------------------------------------------------------------------


    //Server API's
    //------------------------------------------------------------------------------------------------------------------
    public static String getServerPort(){
        return "8082";
    }
    //------------------------------------------------------------------------------------------------------------------


    //Websocket Tests
    //------------------------------------------------------------------------------------------------------------------
    public static Long getMessageTime(){
        /*
        Changing this will change how long tests will wait for the server to send messages.
        3000 Milliseconds (3 seconds) will be enough for most computers. Feel free to change as you see fit,
        just know increasing it can make tests take longer to run.
        (On the flip side, if you've got a good computer feel free to decrease it)
         */
        return 3000L;
    }
    //------------------------------------------------------------------------------------------------------------------
}
