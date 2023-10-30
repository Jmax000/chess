package requestResultObjects;

public class ClearApplicationResult
{
    private String message;
    private int gameID;
    public ClearApplicationResult() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
