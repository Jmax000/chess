package passoffTests.serverTests;

        import chess.ChessGameImpl;
        import dataAccess.*;
        import requestResultObjects.*;
        import services.*;
        import models.*;
        import org.junit.jupiter.api.*;
        import java.util.Vector;

public class TestsPhase3 {

    User validUser1;
    User validUser2;
    User invalidUser;
    Authtoken authtoken1;
    Authtoken authtoken2;

    @BeforeEach
    public void setUp() throws DataAccessException {
        DataBaseService dataBaseService = new DataBaseService();
        dataBaseService.clearApplication();

        validUser1 = new User("validUser1", "password", "email@gmail.com");
        validUser2 = new User("validUser2", "password", "email@gmail.com");
        invalidUser = new User("invalidUser", "", "");
        authtoken1 = new Authtoken("username1");
        authtoken2 = new Authtoken("username2");

        LocalDatabase.addUser(validUser1);
        LocalDatabase.addUser(validUser2);
        LocalDatabase.addUser(invalidUser);
        LocalDatabase.addAuthtoken(authtoken1);
        LocalDatabase.addAuthtoken(authtoken2);
    }

    @Test
    public void testClear()
    {
        DataBaseService dataBaseService = new DataBaseService();
        dataBaseService.clearApplication();

        Assertions.assertEquals(LocalDatabase.getAuthtokenList(), new Vector<>(), "getAuthtokenList is not empty");
        Assertions.assertEquals(LocalDatabase.getUserList(), new Vector<>(), "getUserList is not empty");
        Assertions.assertEquals(LocalDatabase.getGameList(), new Vector<>(), "getGameList is not empty");
    }

    @Test
    public void testListGamesValid()
    {
        GameService gameService = new GameService();
        Game validGame1 = new Game(1, "username1", "username2", "gameName", new ChessGameImpl());
        Game validGame2 = new Game(2, "username1", "username2", "gameName", new ChessGameImpl());
        Game validGame3 = new Game(3, "username1", "username2", "gameName", new ChessGameImpl());
        Game validGame4 = new Game(4, "username1", "username2", "gameName", new ChessGameImpl());

        LocalDatabase.addGame(validGame1);
        LocalDatabase.addGame(validGame2);
        LocalDatabase.addGame(validGame3);
        LocalDatabase.addGame(validGame4);

        Assertions.assertEquals(LocalDatabase.getGameList(), gameService.listGames(authtoken1.getToken()).getGames(), "Did not add all valid games");
    }

    @Test
    public void testListGamesInvalid()
    {
        GameService gameService = new GameService();
        Game validGame1 = new Game(1, "username1", "username2", "gameName", new ChessGameImpl());
        Game validGame2 = new Game(2, "username1", "username2", "gameName", new ChessGameImpl());
        Game validGame3 = new Game(3, "username1", "username2", "gameName", new ChessGameImpl());
        Game validGame4 = new Game(4, "username1", "username2", "gameName", new ChessGameImpl());

        LocalDatabase.addGame(validGame1);
        LocalDatabase.addGame(validGame2);
        LocalDatabase.addGame(validGame3);
        LocalDatabase.addGame(validGame4);

        Assertions.assertEquals(new Vector<>(), gameService.listGames("BAD_AUTHTOKEN").getGames(), "Returned list when list should be empty");
    }

    @Test
    public void testJoinValid()
    {
        GameService gameService = new GameService();
        Game validGame = new Game(1, "username1", "username2", "gameName", new ChessGameImpl());
        LocalDatabase.addGame(validGame);

        JoinGameRequest request = new JoinGameRequest();
        request.setGameID(1);
        request.setPlayerColor("WHITE");

        JoinGameResult result = gameService.joinGame(request, authtoken1.getToken());

        Assertions.assertNull(result.getMessage(), "message is not null");
    }

    @Test
    public void testJoinInvalid() {
        GameService gameService = new GameService();

        JoinGameRequest request = new JoinGameRequest();
        request.setGameID(1);
        request.setPlayerColor("WHITE");
        JoinGameResult result = gameService.joinGame(request, authtoken1.getToken());

        Assertions.assertEquals(result.getMessage(), "Error: bad request", "message is not 'Error: bad request'");

        Game validGame = new Game(1, "username1", "username2", "gameName", new ChessGameImpl());
        LocalDatabase.addGame(validGame);
        result = gameService.joinGame(request, "BAD_AUTHTOKEN");

        Assertions.assertEquals(result.getMessage(), "Error: unauthorized", "message is not 'Error: unauthorized'");

    }

    @Test
    public void testCreateValid()
    {
        GameService gameService = new GameService();
        CreateGameRequest request = new CreateGameRequest();
        request.setGameName("gameName");
        CreateGameResult result = gameService.createGame(request, authtoken1.getToken());

        Assertions.assertNull(result.getMessage(), "message is not null");
    }

    @Test
    public void testCreateInvalid()
    {
        GameService gameService = new GameService();
        CreateGameRequest request = new CreateGameRequest();
        request.setGameName("gameName");
        CreateGameResult result = gameService.createGame(request, "BAD_AUTHTOKEN");

        Assertions.assertEquals(result.getMessage(), "Error: unauthorized", "message is not 'Error: unauthorized'");
    }

    @Test
    public void testLoginValid()
    {
        SessionService sessionService = new SessionService();
        LoginRequest request = new LoginRequest();
        request.setUsername("validUser1");
        request.setPassword("password");
        LoginResult result = sessionService.login(request);

        Assertions.assertNull(result.getMessage(), "message is not null");
    }

    @Test
    public void testLoginInvalid()
    {
        SessionService sessionService = new SessionService();
        LoginRequest request = new LoginRequest();
        request.setUsername("validUser1");
        request.setPassword("notThePassword");
        LoginResult result = sessionService.login(request);

        Assertions.assertEquals(result.getMessage(), "Error: unauthorized", "message is not 'Error: unauthorized'");

        request.setUsername("unknownUser");
        request.setPassword("notThePassword");
        result = sessionService.login(request);

        Assertions.assertEquals(result.getMessage(), "Error: unauthorized", "message is not 'Error: unauthorized'");

    }

    @Test
    public void testLogoutValid()
    {
        SessionService sessionService = new SessionService();
        LogoutResult result = sessionService.logout(authtoken1.getToken());
        Assertions.assertNull(result.getMessage(), "message is not null");
    }

    @Test
    public void testLogoutInvalid()
    {
        SessionService sessionService = new SessionService();
        LogoutResult result = sessionService.logout("BAD_AUTHTOKEN");
        Assertions.assertEquals(result.getMessage(), "Error: unauthorized", "message is not 'Error: unauthorized'");
    }

    @Test
    public void testRegisterValid()
    {
        UserService userService = new UserService();
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newRegister");
        request.setPassword("password");
        request.setEmail("email@gmail.com");
        RegisterResult result = userService.register(request);

        Assertions.assertNull(result.getMessage(), "message is not null");
    }

    @Test
    public void testRegisterInvalid()
    {
        UserService userService = new UserService();
        RegisterRequest request = new RegisterRequest();
        request.setUsername("validUser1");
        request.setPassword("password");
        request.setEmail("email@gmail.com");
        RegisterResult result = userService.register(request);

        Assertions.assertEquals(result.getMessage(),"Error: already taken", "message is not 'Error: already taken'");

        request.setUsername("invalidRequest");
        request.setPassword("");
        request.setEmail("email@gmail.com");
        result = userService.register(request);

        Assertions.assertEquals(result.getMessage(),"Error: bad request", "message is not 'Error: bad request'");
    }

}

