package models;

import chess.ChessGame;
import chess.ChessGameImpl;

public class Game
{
    private Integer gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGame game;

    /**
     * Constructor for the game obj
     * Creates a gameID and game object
     * @param gameName specified by the client
     */
    public Game(String gameName)
    {
        this.gameID = null;
        this.whiteUsername = null;
        this.blackUsername = null;
        this.gameName = gameName;
        this.game = new ChessGameImpl();
    }

    public Game(Integer gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game)
    {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public int getGameID() { return gameID; }
    public void setGameID(int gameID) { this.gameID = gameID; }
    public String getWhiteUsername() { return whiteUsername; }
    public void setWhiteUsername(String whiteUsername) { this.whiteUsername = whiteUsername; }
    public String getBlackUsername() { return blackUsername; }
    public void setBlackUsername(String blackUsername) { this.blackUsername = blackUsername; }
    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }
    public ChessGame getGame() { return game; }
    public void setGame(ChessGameImpl game) { this.game = game; }
}
