package dataAccess;

import chess.ChessGameImpl;
import models.Game;

import java.util.Vector;

public class GameDAO
{
    //A method for inserting a new game into the database.
    public boolean insert(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGameImpl game) throws DataAccessException
    {
        return false;
    }

    //A method for retrieving a specified game from the database by gameID.
    public Game Find(int gameID) throws DataAccessException
    {
        return null;
    }

    // A method for retrieving all games from the database
    public Vector<Game> FindAll() throws DataAccessException
    {
        return null;
    }

    //A method/methods for claiming a spot in the game. The player's username is provied and should be saved as either the whitePlayer or blackPlayer in the database.
    public boolean ClaimSpot(int gameID, String username) throws DataAccessException
    {
        return false;
    }

    //A method for updating a chessGame in the database. It should replace the chessGame string corresponding to a given gameID with a new chessGame string.
    public boolean UpdateGame() throws DataAccessException
    {
        return false;
    }

    //A method for removing a game from the database
    public boolean Remove(int gameID) throws DataAccessException
    {
        return false;
    }

    //A method for clearing all data from the database
    public boolean Clear() throws DataAccessException
    {
        return false;
    }
}
