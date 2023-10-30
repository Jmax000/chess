package models;

import java.security.SecureRandom;
import java.util.Base64;

public class Authtoken
{
    private String token;
    private String username;

    /**
     * Constructor for Authtoken
     * @param username - Creates an Authtoken from the username
     */
    public Authtoken(String username)
    {
        this.username = username;
        String randomPart = Base64.getEncoder().encodeToString(new SecureRandom().generateSeed(16));
        token = username + "-" + randomPart;
    }

    public String getToken() { return token; }
    public String getUsername() { return username; }
    public void setToken(String token) { this.token = token; }
    public void setUsername(String username) { this.username = username; }
}
