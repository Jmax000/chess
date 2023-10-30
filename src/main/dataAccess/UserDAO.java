package dataAccess;

import database.LocalDatabase;
import models.Authtoken;
import models.User;

import java.util.Vector;

public class UserDAO
{
    /**
     * Creates a new user
     * @param username of the user
     * @param password of the user
     * @param email of the user
     * @return a new user
     * @throws DataAccessException if bad stuff happens
     */
    public static User createUser(String username, String password, String email) throws DataAccessException
    {
        User user = new User(username, password, email);
        Authtoken authtoken = new Authtoken(username);
        LocalDatabase.addUser(user);
        LocalDatabase.addAuthtoken(authtoken);
        return user;
    }

    /**
     * A method for retrieving a specified user from the database by username.
     * @param username of the user
     * @return User object
     * @throws DataAccessException if bad stuff happens
     */
    public static User find(String username) throws DataAccessException
    {
        Vector<User> userList = LocalDatabase.getUserList();
        for (User user : userList)
        {
            if (user.getUsername().equals(username))
            {
                return user;
            }
        }
        return null;
    }

    /**
     * A method for clearing all data from the database
     * @throws DataAccessException if bad stuff happens
     */
    public static void clear() throws DataAccessException { LocalDatabase.clearUserList(); }
}
