package models;

public class User {
    String username;
    String password;
    String email;

    /**
     * Constructor for a User obj
     * @param username given by the user
     * @param password given by the user
     * @param email given by the user
     */
    User(String username, String password, String email)
    {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
