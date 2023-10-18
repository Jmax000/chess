package dataAccess;

import models.Authtoken;
import models.User;

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
    User createUser(String username, String password, String email) throws DataAccessException
    {
        return null;
    }

    /**
     * A method for retrieving a specified user from the database by username.
     * @param username of the user
     * @return User object
     * @throws DataAccessException if bad stuff happens
     */
    public User Find(String username) throws DataAccessException
    {
        return null;
    }

    /**
     * A method for clearing all data from the database
     * @throws DataAccessException if bad stuff happens
     */
    public void Clear() throws DataAccessException {}
}
