package services;

import dataAccess.*;
import requestResultObjects.*;

public class DataBaseService
{
    /**
     * Clears the database. Removes all users, games, and authTokens.
     */
    public ClearApplicationResult clearApplication()
    {
        try
        {
            AuthDAO.clear();
            GameDAO.clear();
            UserDAO.clear();

            return new ClearApplicationResult();
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}
