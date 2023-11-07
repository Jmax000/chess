package passoffTests.serverTests;

import models.Authtoken;
import models.Game;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dataAccess.*;
import services.DataBaseService;

import java.util.Vector;

public class TestsPhase4
{
    @BeforeEach
    public void setUp()
    {
        DataBaseService dataBaseService = new DataBaseService();
        dataBaseService.clearApplication();
    }
    @Test
    public void testCreateTokenValid()
    {
        try
        {
            AuthDAO.createAuthtoken("username1");
            Assertions.assertNotNull(AuthDAO.findByName("username1"), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testCreateTokenInvalid()
    {
        try
        {
            Authtoken authtoken = AuthDAO.createAuthtoken("DROP TABLE games");
            Assertions.assertNull(authtoken, "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testFindByNameValid()
    {
        try
        {
            AuthDAO.createAuthtoken("username1");
            Assertions.assertNotNull(AuthDAO.findByName("username1"), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testFindByNameInvalid()
    {
        try
        {
            Authtoken authtoken = AuthDAO.createAuthtoken("DROP TABLE games");
            Assertions.assertNull(authtoken, "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testFindByTokenValid()
    {
        try
        {
            Authtoken authtoken = AuthDAO.createAuthtoken("username1");
            Assertions.assertNotNull(authtoken);
            Assertions.assertNotNull(AuthDAO.findByToken(authtoken.getToken()), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testFindByTokenInvalid()
    {
        try
        {
            Authtoken authtoken = AuthDAO.createAuthtoken("username1");
            Assertions.assertNotNull(authtoken);
            Assertions.assertNull(AuthDAO.findByToken("username2"), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testRemoveAuthtokenValid()
    {
        try
        {
            AuthDAO.createAuthtoken("username1");
            Assertions.assertNotNull(AuthDAO.findByName("username1"), "message is not null");
            AuthDAO.remove("username1");
            Assertions.assertNull(AuthDAO.findByName("username1"), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testRemoveAuthtokenInvalid()
    {
        try
        {
            AuthDAO.createAuthtoken("username1");
            Assertions.assertNotNull(AuthDAO.findByName("username1"), "message is not null");
            AuthDAO.remove("username2");
            Assertions.assertNotNull(AuthDAO.findByName("username1"), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testClearAuthDAO()
    {
        try
        {
            AuthDAO.createAuthtoken("username1");
            Assertions.assertNotNull(AuthDAO.findByName("username1"), "message is not null");
            AuthDAO.clear();
            Assertions.assertNull(AuthDAO.findByName("username1"), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testCreateUserValid()
    {
        try
        {
            UserDAO.createUser("username1", "password", "email@gmail.com");
            Assertions.assertNotNull(UserDAO.find("username1"), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testCreateUserInvalid()
    {
        try
        {
            UserDAO.createUser("DROP TABLE users", "password", "email@gmail.com");
            Assertions.assertNull(UserDAO.find("username1"), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testUserFindValid()
    {
        try
        {
            UserDAO.createUser("username1", "password", "email@gmail.com");
            Assertions.assertNotNull(UserDAO.find("username1"), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testUserFindInvalid()
    {
        try
        {
            UserDAO.createUser("DROP TABLE users", "password", "email@gmail.com");
            Assertions.assertNull(UserDAO.find("username1"), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testUserDAO_Clear()
    {
        try
        {
            UserDAO.createUser("username1", "password", "email@gmail.com");
            UserDAO.clear();
            Assertions.assertNull(UserDAO.find("username1"), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testCreateGame()
    {
        try
        {
            Game game = GameDAO.createGame("gameName");
            Assertions.assertNotNull(GameDAO.find(game.getGameID()), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testCreateGameInvalid()
    {
        try
        {
            int invalidGameID = 0;
            Game game = GameDAO.createGame("gameName");
            Assertions.assertNull(GameDAO.find(invalidGameID), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFindGameValid()
    {
        try
        {
            Game game = GameDAO.createGame("gameName");
            Assertions.assertNotNull(GameDAO.find(game.getGameID()), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testFindGameInvalid()
    {
        try
        {
            int invalidGameID = 0;
            Game game = GameDAO.createGame("gameName");
            Assertions.assertNull(GameDAO.find(invalidGameID), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFindAllGameValid()
    {
        try
        {
            GameDAO.createGame("gameName");
            GameDAO.createGame("gameName");
            GameDAO.createGame("gameName");
            GameDAO.createGame("gameName");
            GameDAO.createGame("gameName");

            Assertions.assertEquals(GameDAO.findAll().size(),5,  "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testFindAllGameInvalid()
    {
        try
        {
            GameDAO.createGame("gameName");
            GameDAO.createGame("gameName");
            GameDAO.createGame("gameName");
            GameDAO.createGame("gameName");
            GameDAO.createGame("gameName");

            Assertions.assertNotEquals(GameDAO.findAll().size(),4,  "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testClaimSpotValid()
    {
        try
        {
            Game game = GameDAO.createGame("gameName");
            User user = UserDAO.createUser("username1", "password", "email@gmail.com");
            GameDAO.claimSpot(game.getGameID(), user.getUsername(), "WHITE");

            Assertions.assertEquals(GameDAO.find(game.getGameID()).getWhiteUsername(),user.getUsername(),  "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testClaimSpotInvalid()
    {
        try
        {
            Game game = GameDAO.createGame("gameName");
            User user1 = UserDAO.createUser("username1", "password", "email@gmail.com");
            GameDAO.claimSpot(game.getGameID(), user1.getUsername(), "WHITE");

            User user2 = UserDAO.createUser("username3", "password", "email@gmail.com");
            GameDAO.claimSpot(game.getGameID(), user2.getUsername(), "WHITE");

            Assertions.assertEquals(GameDAO.find(game.getGameID()).getWhiteUsername(),user1.getUsername(),  "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGameDAO_Clear()
    {
        try
        {
            GameDAO.createGame("gameName");
            GameDAO.clear();
            Assertions.assertEquals(GameDAO.findAll(), new Vector<>(), "message is not null");
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

}
