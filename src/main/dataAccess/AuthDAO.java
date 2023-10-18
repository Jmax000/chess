package dataAccess;

import models.Authtoken;
import models.Game;
import models.User;

import java.util.Vector;

public class AuthDAO
{
    /**
     * Creates a new Authtoken
     * @param username takes in an Authtoken object
     * @return the new Authtoken
     * @throws DataAccessException if bad stuff happens
     */
    Authtoken createAuthtoken(String username) throws DataAccessException
    {
        return null;
    }

    /**
     * A method for retrieving a specified authtoken from the database by username.
     * @param username of the user
     * @return Authtoken for the user
     * @throws DataAccessException if bad stuff happens
     */
    public Authtoken Find(String username) throws DataAccessException
    {
        return null;
    }

    /**
     * A method for clearing all data from the database
     * @throws DataAccessException if bad stuff happens
     */
    public void Clear() throws DataAccessException {}
}
