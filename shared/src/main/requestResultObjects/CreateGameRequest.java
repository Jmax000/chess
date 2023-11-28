package requestResultObjects;

public class CreateGameRequest
{
    private String gameName;
    private String authtoken;
    public CreateGameRequest() {}

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
