package dataAccess;

import models.Authtoken;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        Database db = new Database();
        Connection connection = db.getConnection();

        if (!username.matches("^[a-zA-Z0-9]+$")) {
            return null;
        }

        Authtoken authtoken = new Authtoken(username);

        String sql = "insert into authtokens (token, username) values (?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, authtoken.getToken());
            stmt.setString(2, authtoken.getUsername());
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            db.returnConnection(connection);
        }

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
        Database db = new Database();
        Connection connection = db.getConnection();

        String sql = "SELECT username FROM authtokens WHERE token = " + "\"" + token + "\"";
        try(PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery())
        {
            String username = null;
            if (rs.next())
            {
                username = rs.getString(1);
            }


            if (username != null)
            {
                return new Authtoken(username, token);
            }
            else
            {
                return null;
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            db.returnConnection(connection);
        }
    }

    public static Authtoken findByName(String username) throws DataAccessException
    {
        Database db = new Database();
        Connection connection = db.getConnection();

        String sql = "SELECT token FROM authtokens WHERE username = " + "\"" + username + "\"";
        try(PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery())
        {
            String token = null;
            if (rs.next())
            {
                token = rs.getString(1);
            }

            if (token != null)
            {
                return new Authtoken(username, token);
            }
            else
            {
                return null;
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            db.returnConnection(connection);
        }
    }

    public static void remove(String username) throws DataAccessException
    {
        Database db = new Database();
        Connection connection = db.getConnection();

        String sql = "DELETE FROM authtokens WHERE username = " + "\"" + username + "\"";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            db.returnConnection(connection);
        }
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
    public static void clear() throws DataAccessException
    {
        Database db = new Database();
        Connection connection = db.getConnection();

        String sql = "DELETE FROM authtokens";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            db.returnConnection(connection);
        }
    }
}
