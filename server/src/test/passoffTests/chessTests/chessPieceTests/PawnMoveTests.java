package passoffTests.chessTests.chessPieceTests;

import chess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoffTests.TestFactory;

import java.util.HashSet;
import java.util.Set;

public class PawnMoveTests {

    private ChessBoard board;
    private ChessPiece pawn;
    private ChessPositionImpl position;
    private Set<ChessMoveImpl> validMoves;

    @BeforeEach
    public void setup() {
        //empty board
        board = TestFactory.getNewBoard();
        validMoves = new HashSet<>();
    }


    @Test
    @DisplayName("White Single Move")
    public void emptyBoardWhite() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | |P| | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        //test if all correct positions show up with no obstacles
        pawn = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        position = TestFactory.getNewPosition(4, 4);
        board.addPiece(position, pawn);

        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(5, 4), null));

        Set<ChessMoveImpl> pieceMoves = new HashSet<>(pawn.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("Black Single Move")
    public void emptyBoardBlack() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | |p| | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        pawn = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        position = TestFactory.getNewPosition(4, 4);
        board.addPiece(position, pawn);

        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 4), null));

        Set<ChessMoveImpl> pieceMoves = new HashSet<>(pawn.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    //On a pawn's first move (from the starting position), they can move 2 spaces instead of just 1
    @Test
    @DisplayName("White Double Move")
    public void doubleMoveWhite() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | |P| | | |
		| | | | | | | | |
         */

        pawn = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        position = TestFactory.getNewPosition(2, 5);
        board.addPiece(position, pawn);

        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 5), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 5), null));

        Set<ChessMoveImpl> pieceMoves = new HashSet<>(pawn.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("Black Double Move")
    public void doubleMoveBlack() {

        /*
        | | | | | | | | |
		| | |p| | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        pawn = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        position = TestFactory.getNewPosition(7, 3);
        board.addPiece(position, pawn);

        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(5, 3), null));

        Set<ChessMoveImpl> pieceMoves = new HashSet<>(pawn.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("White Edge Promotion")
    public void edgePromotionWhite() {

        /*
        | | | | | | | | |
		| | |P| | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        ChessPositionImpl start = TestFactory.getNewPosition(7, 3);
        ChessPositionImpl end = TestFactory.getNewPosition(8, 3);

        pawn = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        board.addPiece(start, pawn);

        //add all promotions
        validMoves.add(TestFactory.getNewMove(start, end, ChessPiece.PieceType.QUEEN));
        validMoves.add(TestFactory.getNewMove(start, end, ChessPiece.PieceType.BISHOP));
        validMoves.add(TestFactory.getNewMove(start, end, ChessPiece.PieceType.ROOK));
        validMoves.add(TestFactory.getNewMove(start, end, ChessPiece.PieceType.KNIGHT));

        Set<ChessMoveImpl> pieceMoves = new HashSet<>(pawn.pieceMoves(board, start));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("Black Edge Promotion")
    public void edgePromotionBlack() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | |p| | | | | |
		| | | | | | | | |
         */

        ChessPositionImpl start = TestFactory.getNewPosition(2, 3);
        ChessPositionImpl end = TestFactory.getNewPosition(1, 3);

        pawn = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        board.addPiece(start, pawn);

        //add all promotions
        validMoves.add(TestFactory.getNewMove(start, end, ChessPiece.PieceType.QUEEN));
        validMoves.add(TestFactory.getNewMove(start, end, ChessPiece.PieceType.BISHOP));
        validMoves.add(TestFactory.getNewMove(start, end, ChessPiece.PieceType.ROOK));
        validMoves.add(TestFactory.getNewMove(start, end, ChessPiece.PieceType.KNIGHT));

        Set<ChessMoveImpl> pieceMoves = new HashSet<>(pawn.pieceMoves(board, start));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("White Move Blocked")
    public void piecesInWayWhite() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | |n| | | | |
		| | | |P| | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        pawn = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        position = TestFactory.getNewPosition(4, 4);
        board.addPiece(position, pawn);

        //other team obstacle
        board.addPiece(TestFactory.getNewPosition(5, 4),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));

        Set<ChessMoveImpl> pieceMoves = new HashSet<>(pawn.pieceMoves(board, position));
        Assertions.assertTrue(pieceMoves.isEmpty(),
                "ChessPiece pieceMoves returned valid moves for a trapped piece");
    }


    @Test
    @DisplayName("Black Move Blocked")
    public void piecesInWayBlack() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | |p| | | | |
		| | | |r| | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        pawn = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        position = TestFactory.getNewPosition(4, 4);
        board.addPiece(position, pawn);

        //same team obstacle
        board.addPiece(TestFactory.getNewPosition(3, 4),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        Set<ChessMoveImpl> pieceMoves = new HashSet<>(pawn.pieceMoves(board, position));
        Assertions.assertTrue(pieceMoves.isEmpty(),
                "ChessPiece pieceMoves returned valid moves for a trapped piece");
    }


    @Test
    @DisplayName("Double Move Blocked")
    public void doubleMoveBlocked() {

        /*
        | | | | | | | | |
		| | |p| | | | | |
		| | |p| | | | | |
		| | | | | | | | |
		| | | | | | |p| |
		| | | | | | | | |
		| | | | | | |P| |
		| | | | | | | | |
         */

        ChessPiece whitePawn = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPositionImpl whitePosition = TestFactory.getNewPosition(2, 7);
        board.addPiece(whitePosition, whitePawn);

        ChessPiece blackPawn = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPositionImpl blackPosition = TestFactory.getNewPosition(7, 3);
        board.addPiece(blackPosition, blackPawn);

        //white second space blocked
        board.addPiece(TestFactory.getNewPosition(4, 7), blackPawn);

        //black first space blocked
        board.addPiece(TestFactory.getNewPosition(6, 3), blackPawn);

        //valid move from white pawn
        validMoves.add(TestFactory.getNewMove(whitePosition, TestFactory.getNewPosition(3, 7), null));

        //test white
        Set<ChessMoveImpl> pieceMoves = new HashSet<>(whitePawn.pieceMoves(board, whitePosition));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");

        //test black
        pieceMoves = new HashSet<>(blackPawn.pieceMoves(board, blackPosition));
        Assertions.assertTrue(pieceMoves.isEmpty(),
                "ChessPiece pieceMoves returned valid moves for a trapped piece");
    }


    @Test
    @DisplayName("White Capture")
    public void capturingByWhite() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | |r| |N| | | |
		| | | |P| | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        pawn = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        position = TestFactory.getNewPosition(4, 4);
        board.addPiece(position, pawn);

        //same team cannot capture
        ChessPositionImpl allyPosition = TestFactory.getNewPosition(5, 5);
        board.addPiece(allyPosition, TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));

        // enemy can capture left
        ChessPositionImpl enemyPosition = TestFactory.getNewPosition(5, 3);
        board.addPiece(enemyPosition, TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        //get moves for pawn
        Set<ChessMoveImpl> pieceMoves = new HashSet<>(pawn.pieceMoves(board, position));

        //invalid move
        ChessMoveImpl badCapture = TestFactory.getNewMove(position, allyPosition, null);
        Assertions.assertFalse(pieceMoves.contains(badCapture),
                "Piece moves contained move: " + badCapture + " that would capture a ally piece");

        //expected moves
        ChessMoveImpl capture = TestFactory.getNewMove(position, enemyPosition, null);
        Assertions.assertTrue(pieceMoves.contains(capture),
                "Piece moves did not contain valid move: " + capture + " that would capture an enemy piece");
        validMoves.add(capture);
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(5, 4), null));

        //check
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("Black Capture")
    public void capturingByBlack() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | |p| | | | |
		| | | |n|R| | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        pawn = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        position = TestFactory.getNewPosition(4, 4);
        board.addPiece(position, pawn);

        //piece blocking forward
        ChessPositionImpl allyPosition = TestFactory.getNewPosition(3, 4);
        board.addPiece(allyPosition, TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));

        // can capture right
        ChessPositionImpl enemyPosition = TestFactory.getNewPosition(3, 5);
        board.addPiece(enemyPosition, TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        //get moves for pawn
        Set<ChessMoveImpl> pieceMoves = new HashSet<>(pawn.pieceMoves(board, position));

        //expected moves
        ChessMoveImpl capture = TestFactory.getNewMove(position, enemyPosition, null);
        Assertions.assertTrue(pieceMoves.contains(capture),
                "Piece moves did not contain valid move: " + capture + " that would capture an enemy piece");
        validMoves.add(capture);

        //invalid moves
        ChessMoveImpl badCapture = TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 3), null);
        Assertions.assertFalse(pieceMoves.contains(badCapture),
                "Piece moves contained move: " + badCapture + ", which is not a valid move");

        //check
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("Promote and Capture")
    public void pawnPromotionCapture() {

        /*
        | | | | | | | | |
        | | | | | | | | |
        | | | | | | | | |
        | | | | | | | | |
        | | | | | | | | |
        | | | | | | | | |
        | |p| | | | | | |
        |N| | | | | | | |
         */

        pawn = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        position = TestFactory.getNewPosition(2, 2);
        board.addPiece(position, pawn);

        //can capture
        board.addPiece(TestFactory.getNewPosition(1, 1),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));

        //capturing moves
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(1, 1), ChessPiece.PieceType.QUEEN));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(1, 1), ChessPiece.PieceType.BISHOP));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(1, 1), ChessPiece.PieceType.ROOK));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(1, 1), ChessPiece.PieceType.KNIGHT));

        //straight to promotion moves
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(1, 2), ChessPiece.PieceType.KNIGHT));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(1, 2), ChessPiece.PieceType.ROOK));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(1, 2), ChessPiece.PieceType.BISHOP));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(1, 2), ChessPiece.PieceType.QUEEN));

        Set<ChessMoveImpl> pieceMoves = new HashSet<>(pawn.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }

}
