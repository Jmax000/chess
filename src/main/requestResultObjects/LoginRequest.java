package requestResultObjects;

public class LoginRequest
{
    private String username;
    private String password;
    public LoginRequest() {}

    // … Getters and Setters for username and password properties
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
