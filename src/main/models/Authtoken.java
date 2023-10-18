package models;

public class Authtoken
{
    private String authToken;
    private String username;

    /**
     * Constructor for Authtoken
     * @param username - Creates an Authtoken from the username
     */
    public Authtoken(String username)
    {
        this.username = username;
    }

    public String getAuthToken() { return authToken; }
    public String getUsername() { return username; }
    public void setAuthToken(String authToken) { this.authToken = authToken; }
    public void setUsername(String username) { this.username = username; }
}
