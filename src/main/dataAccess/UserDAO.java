package dataAccess;

import models.Authtoken;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class UserDAO
{
    /**
     * Creates a new user
     * @param username of the user
     * @param password of the user
     * @param email of the user
     * @throws DataAccessException if bad stuff happens
     */
    public static User createUser(String username, String password, String email) throws DataAccessException
    {
        Database db = new Database();
        Connection connection = db.getConnection();

        if (!username.matches("^[a-zA-Z0-9]+$")) {
            return null;
        }

        User user = new User(username, password, email);
        String sql = "insert into users (username, password, email) values (?, ?, ?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            AuthDAO.createAuthtoken(username); //NEED? FIXME

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
        return user;

//        Don't need right??? FIXME
//        Authtoken authtoken = new Authtoken(username);
//        LocalDatabase.addAuthtoken(authtoken);
    }

    /**
     * A method for retrieving a specified user from the database by username.
     * @param username of the user
     * @return User object
     * @throws DataAccessException if bad stuff happens
     */
    public static User find(String username) throws DataAccessException
    {
        Database db = new Database();
        Connection connection = db.getConnection();

        String sql = "SELECT password, email FROM users WHERE username = " + "\"" + username + "\"";
        try(PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery())
        {
            String password = null;
            String email = null;
            if(rs.next())
            {
                password = rs.getString(1);
                email = rs.getString(2);
            }


            if (password != null)
            {
                return new User(username, password, email);
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

    /**
     * A method for clearing all data from the database
     * @throws DataAccessException if bad stuff happens
     */
    public static void clear() throws DataAccessException
    {
        Database db = new Database();
        Connection connection = db.getConnection();

        String sql = "DELETE FROM users";
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
