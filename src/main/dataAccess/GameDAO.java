package dataAccess;

import chess.ChessGame;
import database.LocalDatabase;
import models.Game;

import java.util.Vector;

import static chess.ChessGame.TeamColor;

public class GameDAO
{
    /**
     * A method for creating a new game into the database.
     * @param gameName - the name specified by the players
     * @return the game model
     * @throws DataAccessException if bad stuff happens
     */
    public static Game createGame(String gameName) throws DataAccessException
    {
        Game game = new Game(LocalDatabase.getGameID(), gameName);
        LocalDatabase.addGame(game);
        return game;
    }

    /**
     * A method for retrieving a specified game from the database by gameID.
     * @param gameID of the game object
     * @return a game object from the corresponding gameID
     * @throws DataAccessException if bad stuff happens
     */
    public static Game find(int gameID) throws DataAccessException
    {
        Vector<Game> gameList = LocalDatabase.getGameList();
        for (Game game : gameList)
        {
            if (game.getGameID() == gameID)
            {
                return game;
            }
        }
        return null;
    }

    /**
     * A method for retrieving all games from the database
     * @return A vector containing all games
     * @throws DataAccessException if bad stuff happens
     */
    public static Vector<Game> findAll() throws DataAccessException { return LocalDatabase.getGameList(); }

    /**
     * A method for claiming a spot in the game.
     * The player's username is provided and should be saved as either the whitePlayer or blackPlayer in the database.
     * @param gameID of the game object
     * @param username of the player
     * @throws DataAccessException if bad stuff happens
     */
    //
    public static boolean claimSpot(int gameID, String username, String color) throws DataAccessException
    {
        Game game = find(gameID);
        if(game != null)
        {
            if (color == null)
            {
                return true;
            }
            if (color.equals("WHITE") && game.getWhiteUsername() != null)
            {
                return false;
            }
            else if (color.equals("WHITE") && game.getWhiteUsername() == null)
            {
                game.setWhiteUsername(username);
                return true;
            }

            if (color.equals("BLACK") && game.getBlackUsername() != null)
            {
                return false;
            }
            else if (color.equals("BLACK") && game.getBlackUsername() == null)
            {
                game.setBlackUsername(username);
                return true;
            }
        }
        else
        {
            throw new DataAccessException("Error: bad request"); //Right error????
        }
        return false;
    }

    public static void clear() { LocalDatabase.clearGameList(); }

    /**
     * A method for updating a chessGame in the database.
     * It should replace the chessGame string corresponding to a given gameID with a new chessGame string.
     * @throws DataAccessException if bad stuff happens
     */
    public void UpdateGame(ChessGame newGame) throws DataAccessException
    {
        //FIXME
    }

    /**
     * A method for removing a game from the database
     * @param gameID of the game object
     * @throws DataAccessException if bad stuff happens
     */
    public void Remove(int gameID) throws DataAccessException
    {
        Vector<Game> gameList = LocalDatabase.getGameList();
        for (Game game : gameList)
        {
            if (game.getGameID() == gameID)
            {
                gameList.remove(game);
                return;
            }
        }
    }

    /**
     * A method for clearing all data from the database
     * @throws DataAccessException if bad stuff happens
     */
    public void Clear() throws DataAccessException { LocalDatabase.clearGameList(); }
}
