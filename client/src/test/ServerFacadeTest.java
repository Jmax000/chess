import exception.ResponseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requestResultObjects.*;
import webClient.ServerFacade;

import java.util.Vector;

public class ServerFacadeTest
{
    private final ServerFacade server = new ServerFacade("http://localhost:8082");
    @BeforeEach
    public void setUp()
    {
        try
        {
            server.clear();
        }
        catch (ResponseException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testClear()
    {
        try
        {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername("user1");
            registerRequest.setPassword("password");
            registerRequest.setEmail("email");
            RegisterResult registerResult = server.register(registerRequest);

            CreateGameRequest gameRequest = new CreateGameRequest();
            gameRequest.setGameName("Game");
            server.createGame(gameRequest, registerResult.getAuthToken());

            server.clear();

            registerRequest = new RegisterRequest();
            registerRequest.setUsername("user1");
            registerRequest.setPassword("password");
            registerRequest.setEmail("email");
            registerResult = server.register(registerRequest);

            ListGameResult listGameResult = server.listGames(registerResult.getAuthToken());

            Assertions.assertEquals(listGameResult.getGames(), new Vector<>(), "Error message was not null");
        }
        catch (ResponseException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUserValid()
    {
        try
        {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername("user1");
            registerRequest.setPassword("password");
            registerRequest.setEmail("email");
            RegisterResult registerResult = server.register(registerRequest);

            Assertions.assertNotNull(registerResult.getUsername(), "username is null");
        }
        catch (ResponseException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUserInvalid()
    {
        try
        {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername("user1");
            registerRequest.setPassword("password");
            registerRequest.setEmail("email");
            RegisterResult registerResult = server.register(registerRequest);

            Assertions.assertNotNull(registerResult.getUsername(), "username is null");
        }
        catch (ResponseException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateValid()
    {
        try
        {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername("user1");
            registerRequest.setPassword("password");
            registerRequest.setEmail("email");
            RegisterResult registerResult = server.register(registerRequest);

            CreateGameRequest gameRequest = new CreateGameRequest();
            gameRequest.setGameName("Game");
            CreateGameResult gameResult = server.createGame(gameRequest, registerResult.getAuthToken());

            Assertions.assertNull(gameResult.getMessage(), "error code is not null");
        }
        catch (ResponseException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateInvalid()
    {
        CreateGameRequest gameRequest = new CreateGameRequest();
        Assertions.assertThrows(ResponseException.class, () -> server.createGame(gameRequest, "InvalidAuthtoken"));
    }

    @Test
    public void testLoginValid()
    {
        try
        {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername("user1");
            registerRequest.setPassword("password");
            registerRequest.setEmail("email");
            RegisterResult registerResult = server.register(registerRequest);

            server.logout(registerResult.getAuthToken());

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername("user1");
            loginRequest.setPassword("password");
            LoginResult loginResult = server.login(loginRequest);

            Assertions.assertNull(loginResult.getMessage(), "error code is not null");
        }
        catch (ResponseException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLoginInvalid()
    {
        LoginRequest loginRequest = new LoginRequest();
        Assertions.assertThrows(ResponseException.class, () -> server.login(loginRequest));
    }

    @Test
    public void testLogoutValid()
    {
        try
        {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername("user1");
            registerRequest.setPassword("password");
            registerRequest.setEmail("email");
            RegisterResult registerResult = server.register(registerRequest);

            LogoutResult logoutResult = server.logout(registerResult.getAuthToken());

            Assertions.assertNull(logoutResult.getMessage(), "error code is not null");
        }
        catch (ResponseException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testLogoutInvalid()
    {
        Assertions.assertThrows(ResponseException.class, () -> server.logout("InvalidAuthtoken"));
    }

    @Test
    public void testListValid()
    {
        try
        {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername("user1");
            registerRequest.setPassword("password");
            registerRequest.setEmail("email");
            RegisterResult registerResult = server.register(registerRequest);

            CreateGameRequest gameRequest = new CreateGameRequest();
            gameRequest.setGameName("Game");
            server.createGame(gameRequest, registerResult.getAuthToken());

            ListGameResult listGameResult = server.listGames(registerResult.getAuthToken());

            Assertions.assertNull(listGameResult.getMessage(), "error code is not null");
        }
        catch (ResponseException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testListInvalid()
    {
        Assertions.assertThrows(ResponseException.class, () -> server.listGames("InvalidAuthtoken"));
    }

    @Test
    public void testJoinValid()
    {
        try
        {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername("user1");
            registerRequest.setPassword("password");
            registerRequest.setEmail("email");
            RegisterResult registerResult = server.register(registerRequest);

            CreateGameRequest gameRequest = new CreateGameRequest();
            gameRequest.setGameName("Game");
            CreateGameResult createGameResult = server.createGame(gameRequest, registerResult.getAuthToken());

            JoinGameRequest joinGameRequest = new JoinGameRequest();
            joinGameRequest.setGameID(createGameResult.getGameID());
            JoinGameResult joinGameResult = server.joinGame(joinGameRequest,registerResult.getAuthToken());

            Assertions.assertNull(joinGameResult.getMessage(), "error code is not null");
        }
        catch (ResponseException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testJoinInvalid()
    {
        JoinGameRequest joinGameRequest = new JoinGameRequest();
        Assertions.assertThrows(ResponseException.class, () -> server.joinGame(joinGameRequest, ""));
    }
}