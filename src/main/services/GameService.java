package services;

import models.Game;

import java.util.Vector;

public class GameService
{
    /**
     * Creates a new game.
     * @param request takes in a CreateGameRequest obj
     * @return returns a CreateGameResult obj
     */
    public CreateGameResult createGame(CreateGameRequest request) { return null; }

    /**
     * Verifies that the specified game exists, and, if a color is specified,
     * adds the caller as the requested color to the game.
     * If no color is specified the user is joined as an observer. This request is idempotent.
     * @param request takes in a JoinGameRequest obj
     * @return returns a JoinGameResult obj
     */
    //
    public JoinGameResult joinGame(JoinGameRequest request) { return null; }

    /**
     * Gives a list of all games.
     * @param request takes in a ListGameRequest obj
     * @return returns a ListGameResult obj
     */
    public ListGameResult listGame(ListGameRequest request) { return null; }

}
