package services;

import dataAccess.*;
import models.Authtoken;
import requestResultObjects.*;
import models.Game;
import java.util.Vector;

public class GameService
{
    /**
     * Creates a new game.
     * @param request takes in a CreateGameRequest obj
     * @return returns a CreateGameResult obj
     */
    public CreateGameResult createGame(CreateGameRequest request, String token)
    {
        try
        {
            if (AuthDAO.findByToken(token) == null)
            {
                CreateGameResult invalidGame = new CreateGameResult();
                invalidGame.setMessage("Error: unauthorized");
                return invalidGame;
            }
            else
            {
                Game game = GameDAO.createGame(request.getGameName());
                CreateGameResult validGame = new CreateGameResult();
                validGame.setGameID(game.getGameID());
                return validGame;
            }
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies that the specified game exists, and, if a color is specified,
     * adds the caller as the requested color to the game.
     * If no color is specified the user is joined as an observer. This request is idempotent.
     * @param request takes in a JoinGameRequest obj
     * @return returns a JoinGameResult obj
     */
    public JoinGameResult joinGame(JoinGameRequest request, String token)
    {
        try
        {
            Game game = GameDAO.find(request.getGameID());
            Authtoken authtoken = AuthDAO.findByToken(token);
            if (game == null)
            {
                JoinGameResult invalidGame = new JoinGameResult();
                invalidGame.setMessage("Error: bad request");
                return invalidGame;
            }
            else if (authtoken == null)
            {
                JoinGameResult invalidGame = new JoinGameResult();
                invalidGame.setMessage("Error: unauthorized");
                return invalidGame;
            }
            else if(!GameDAO.claimSpot(request.getGameID(), authtoken.getUsername(), request.getPlayerColor()))
            {
                JoinGameResult invalidGame = new JoinGameResult();
                invalidGame.setMessage("Error: already taken");
                return invalidGame;
            }
            else
            {
                return new JoinGameResult();
            }
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gives a list of all games.
     * @return returns a ListGameResult obj
     */
    public ListGameResult listGames(String token)
    {
        try
        {
            if (AuthDAO.findByToken(token) == null)
            {
                ListGameResult invalidGame = new ListGameResult();
                invalidGame.setMessage("Error: unauthorized");
                return invalidGame;
            }
            else
            {
                ListGameResult validListGame = new ListGameResult();
                Vector<Game> gameList = new Vector<>();
                if (GameDAO.findAll() != null)
                {
                    gameList = GameDAO.findAll();
                }
                validListGame.setGames(gameList);
                return validListGame;
            }
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}
