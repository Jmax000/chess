package requestResultObjects;

public class JoinGameRequest
{
    private String playerColor;
    private int gameID;

    public JoinGameRequest() {}
    // … Getters and Setters for message, authToken, and username properties


    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
