package dataAccess;

import models.Authtoken;
import database.LocalDatabase;

import java.util.Vector;

public class AuthDAO
{
    /**
     * Creates a new Authtoken
     * @param username takes in an Authtoken object
     * @return the new Authtoken
     * @throws DataAccessException if bad stuff happens
     */
    public static Authtoken createAuthtoken(String username) throws DataAccessException
    {
        Authtoken authtoken = new Authtoken(username);
        LocalDatabase.addAuthtoken(authtoken);
        return authtoken;
    }

    /**
     * A method for retrieving a specified token from the database by username.
     * @param token of the user
     * @return Authtoken for the user
     * @throws DataAccessException if bad stuff happens
     */
    public static Authtoken findByToken(String token) throws DataAccessException
    {
        Vector<Authtoken> authtokenList = LocalDatabase.getAuthtokenList();
        for (Authtoken authtoken : authtokenList)
        {
            if (authtoken.getToken().equals(token))
            {
                return authtoken;
            }
        }
        return null;
    }

    public static Authtoken findByName(String username) throws DataAccessException
    {
        Vector<Authtoken> authtokenList = LocalDatabase.getAuthtokenList();
        for (Authtoken authtoken : authtokenList)
        {
            if (authtoken.getUsername().equals(username))
            {
                return authtoken;
            }
        }
        return null;
    }

    public static void remove(String username) throws DataAccessException
    {
        Vector<Authtoken> authtokenList = LocalDatabase.getAuthtokenList();
        authtokenList.removeIf(authtoken -> authtoken.getUsername().equals(username));
    }

    public static Authtoken setNewAuthtoken(String username) throws DataAccessException
    {
        remove(username);
        return createAuthtoken(username);
    }

    /**
     * A method for clearing all data from the database
     * @throws DataAccessException if bad stuff happens
     */
    public static void clear() throws DataAccessException { LocalDatabase.clearAuthtokenList(); }
}
