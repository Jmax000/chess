package requestResultObjects;

import models.Game;

import java.util.Vector;

public class ListGameResult
{
    private String message;
    private Vector<Game> games = new Vector<>();
    public ListGameResult() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Vector<Game> getGames() {
        return games;
    }

    public void setGames(Vector<Game> games) {
        this.games = games;
    }
}
