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

    Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGameImpl game)
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
    public void setWhiteUsername(int gameID) { this.whiteUsername = whiteUsername; }
    public String getBlackUsername() { return blackUsername; }
    public void setBlackUsername(int gameID) { this.blackUsername = blackUsername; }
    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }
    public ChessGameImpl getGame() { return game; }
    public void setGame(ChessGameImpl game) { this.game = game; }
}
