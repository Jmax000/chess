package requestResultObjects;

public class CreateGameResult
{
    private String message;
    private Integer gameID;
    public CreateGameResult() {}
    // … Getters and Setters for message, authToken, and username properties

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
