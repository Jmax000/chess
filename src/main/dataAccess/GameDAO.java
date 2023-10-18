package dataAccess;

import chess.ChessGameImpl;
import models.Game;

import java.util.Vector;

public class GameDAO
{
    /**
     * A method for creating a new game into the database.
     * @param gameID - the gameID
     * @param whiteUsername - the username of the player using white
     * @param blackUsername - the username of the player using black
     * @param gameName - the name specified by the players
     * @param game - the game object
     * @return true if the game was created
     * @throws DataAccessException if bad stuff happens
     */
    public boolean createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGameImpl game) throws DataAccessException
    {
        return false;
    }

    /**
     * A method for retrieving a specified game from the database by gameID.
     * @param gameID of the game object
     * @return a game object from the corresponding gameID
     * @throws DataAccessException if bad stuff happens
     */
    public Game Find(int gameID) throws DataAccessException
    {
        return null;
    }

    /**
     * A method for retrieving all games from the database
     * @return A vector containing all games
     * @throws DataAccessException if bad stuff happens
     */
    public Vector<Game> FindAll() throws DataAccessException
    {
        return null;
    }

    /**
     * A method for claiming a spot in the game.
     * The player's username is provided and should be saved as either the whitePlayer or blackPlayer in the database.
     * @param gameID of the game object
     * @param username of the player
     * @return a true if claiming a spot was successful
     * @throws DataAccessException if bad stuff happens
     */
    //
    public boolean ClaimSpot(int gameID, String username) throws DataAccessException
    {
        return false;
    }

    /**
     * A method for updating a chessGame in the database.
     * It should replace the chessGame string corresponding to a given gameID with a new chessGame string.
     * @throws DataAccessException if bad stuff happens
     */
    public void UpdateGame() throws DataAccessException {}

    /**
     * A method for removing a game from the database
     * @param gameID of the game object
     * @return true if successful
     * @throws DataAccessException if bad stuff happens
     */
    public boolean Remove(int gameID) throws DataAccessException
    {
        return false;
    }

    /**
     * A method for clearing all data from the database
     * @throws DataAccessException if bad stuff happens
     */
    public void Clear() throws DataAccessException {}
}
