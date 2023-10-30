package models;

import chess.ChessGame;
import chess.ChessGameImpl;

public class Game
{
    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGameImpl game;

    /**
     * Constructor for the game obj
     * Creates a gameID and game object
     * @param gameName specified by the client
     */
    public Game(int gameID, String gameName)
    {
        this.gameID = gameID;
        this.whiteUsername = null;
        this.blackUsername = null;
        this.gameName = gameName;
        this.game = new ChessGameImpl();
    }

    public int getGameID() { return gameID; }
    public void setGameID(int gameID) { this.gameID = gameID; }
    public String getWhiteUsername() { return whiteUsername; }
    public void setWhiteUsername(String whiteUsername) { this.whiteUsername = whiteUsername; }
    public String getBlackUsername() { return blackUsername; }
    public void setBlackUsername(String blackUsername) { this.blackUsername = blackUsername; }
    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }
    public ChessGameImpl getGame() { return game; }
    public void setGame(ChessGameImpl game) { this.game = game; }
}
