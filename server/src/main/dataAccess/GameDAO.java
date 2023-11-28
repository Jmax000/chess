package dataAccess;

import adapters.GameAdapter;
import chess.ChessGame;
import com.google.gson.GsonBuilder;
import models.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class GameDAO
{
    /**
     * A method for creating a new game into the database.
     * @param gameName - the name specified by the players
     * @return the game model
     * @throws DataAccessException if bad stuff happens
     */
    public static Game createGame(String gameName) throws DataAccessException
    {
        Database db = new Database();
        Connection connection = db.getConnection();

        Game game = new Game(gameName);
        //game.getGame().getBoard().resetBoard(); Why doesn't this serialize???? FIXME

        String sql = "insert into games (whiteUsername, blackUsername, gameName, game) values (?, ?, ?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql, RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, game.getWhiteUsername());
            stmt.setString(2, game.getBlackUsername());
            stmt.setString(3, game.getGameName());

            String chessGame = serializeGame(game.getGame());
            stmt.setString(4, chessGame);

            stmt.executeUpdate();

            var resultSet = stmt.getGeneratedKeys();
            if (resultSet.next()) {
                int gameID = resultSet.getInt(1);
                game.setGameID(gameID);
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

        return game;
    }

    /**
     * A method for retrieving a specified game from the database by gameID.
     * @param gameID of the game object
     * @return a game object from the corresponding gameID
     * @throws DataAccessException if bad stuff happens
     */
    public static Game find(int gameID) throws DataAccessException
    {
        Database db = new Database();
        Connection connection = db.getConnection();

        String sql = "SELECT whiteUsername, blackUsername, gameName, game from games WHERE gameID = " + "\"" + gameID + "\"";
        try(PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery())
        {
            if(rs.next())
            {
                String whiteUsername = rs.getString(1);
                String blackUsername = rs.getString(2);
                String gameName = rs.getString(3);
                String game = rs.getString(4);

                ChessGame chessGame = deserializeGame(game);
                return new Game(gameID, whiteUsername, blackUsername, gameName, chessGame);
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
     * A method for retrieving all games from the database
     * @return A vector containing all games
     * @throws DataAccessException if bad stuff happens
     */
    public static Vector<Game> findAll() throws DataAccessException
    {
        Database db = new Database();
        Connection connection = db.getConnection();

        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game from games";
        try(PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery())
        {
            Vector<Game> games = new Vector<>();

            while(rs.next())
            {
                int gameID = rs.getInt(1);
                String whiteUsername = rs.getString(2);
                String blackUsername = rs.getString(3);
                String gameName = rs.getString(4);
                String game = rs.getString(5);

                ChessGame chessGame = deserializeGame(game);
                games.add(new Game(gameID, whiteUsername, blackUsername, gameName, chessGame));
            }
            return games;
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
     * A method for claiming a spot in the game.
     * The player's username is provided and should be saved as either the whitePlayer or blackPlayer in the database.
     * @param gameID of the game object
     * @param username of the player
     * @throws DataAccessException if bad stuff happens
     */
    public static boolean claimSpot(int gameID, String username, String color) throws DataAccessException
    {
        Game game = GameDAO.find(gameID);

        if(game != null)
        {
            if (color == null || color.isEmpty())
            {
                return true;
            }

            color = color.toUpperCase();

            if (color.equals("WHITE") && game.getWhiteUsername() != null)
            {
                return false;
            }
            else if (color.equals("WHITE") && game.getWhiteUsername() == null)
            {
                game.setWhiteUsername(username);
                updateGame(game);
                return true;
            }

            if (color.equals("BLACK") && game.getBlackUsername() != null)
            {
                return false;
            }
            else if (color.equals("BLACK") && game.getBlackUsername() == null)
            {
                game.setBlackUsername(username);
                updateGame(game);
                return true;
            }
        }
        else
        {
            throw new DataAccessException("Error: bad request");
        }
        return false;
    }

    public static void clear() throws DataAccessException
    {
        Database db = new Database();
        Connection connection = db.getConnection();

        String sql = "DELETE FROM games";
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

    /**
     * A method for updating a chessGame in the database.
     * It should replace the chessGame string corresponding to a given gameID with a new chessGame string.
     * @throws DataAccessException if bad stuff happens
     */
    public static void updateGame(Game game) throws DataAccessException
    {
        Database db = new Database();
        Connection connection = db.getConnection();

        String sql = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, game.getWhiteUsername());
            stmt.setString(2, game.getBlackUsername());
            stmt.setString(3, game.getGameName());
            String chessGame = serializeGame(game.getGame());
            stmt.setString(4, chessGame);
            stmt.setInt(5, game.getGameID());
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

    /**
     * A method for removing a game from the database
     * @param gameID of the game object
     * @throws DataAccessException if bad stuff happens
     */
    public static void remove(int gameID) throws DataAccessException
    {
        Database db = new Database();
        Connection connection = db.getConnection();

        String sql = "DELETE FROM games WHERE gameID = " + "\"" + gameID + "\"";
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

    private static ChessGame deserializeGame(String jsnStr)
    {
        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessGame.class, new GameAdapter());

        return builder.create().fromJson(jsnStr, ChessGame.class);
    }

    private static String serializeGame(ChessGame gameObj)
    {
        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessGame.class, new GameAdapter());

        return builder.create().toJson(gameObj);
    }

}
