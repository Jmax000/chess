package services;

import models.Game;

import java.util.Vector;

public class GameService
{
    //Creates a new game.
    public CreateGameResult createGame(CreateGameRequest request) { return null; }

    //Verifies that the specified game exists, and, if a color is specified, adds the caller as the requested color to the game. If no color is specified the user is joined as an observer. This request is idempotent.
    public JoinGameResult joinGame(JoinGameRequest request) { return null; }

    //Gives a list of all games.
    public ListGameResult listGame(ListGameRequest request) { return null; }

}
