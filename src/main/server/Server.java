package server;

import com.google.gson.Gson;
import requestResultObjects.*;
import services.DataBaseService;
import services.GameService;
import services.SessionService;
import services.UserService;
import spark.Request;
import spark.Response;
import spark.Spark;

public class Server
{
    public static void main(String[] args) {
        new Server().run();
    }
    private void run() {
        // Specify the port you want the server to listen on
        Spark.port(8082);

        // Register a directory for hosting static files
        Spark.externalStaticFileLocation("web");

        // Register handlers for each endpoint using the method reference syntax
        Spark.delete("/db", this::handleClear);
        Spark.post("/user", this::handleRegister);
        Spark.post("/session", this::handleLogin);
        Spark.delete("/session", this::handleLogout);
        Spark.get("/game", this::handleListGames);
        Spark.post("/game", this::handleCreateGame);
        Spark.put("/game", this::handleJoinGame);
        Spark.init();
    }

    public Object handleClear(Request req, Response res)
    {
        DataBaseService dataBaseService = new DataBaseService();
        var serializer = new Gson();
        ClearApplicationResult clearApplicationResult = dataBaseService.clearApplication();
        res.status(statusCode(clearApplicationResult.getMessage()));
        return serializer.toJson(clearApplicationResult);
    }

    public Object handleRegister(Request req, Response res)
    {
        UserService userService = new UserService();
        var serializer = new Gson();
        RegisterResult registerResult = userService.register(serializer.fromJson(req.body(), RegisterRequest.class));
        res.status(statusCode(registerResult.getMessage()));
        return serializer.toJson(registerResult);
    }

    public Object handleLogin(Request req, Response res)
    {
        SessionService sessionService = new SessionService();
        var serializer = new Gson();
        LoginResult loginResult = sessionService.login(serializer.fromJson(req.body(), LoginRequest.class));
        res.status(statusCode(loginResult.getMessage()));
        return serializer.toJson(loginResult);
    }

    public Object handleLogout(Request req, Response res)
    {
        SessionService sessionService = new SessionService();
        var serializer = new Gson();
        LogoutResult logoutResult = sessionService.logout(req.headers("authorization"));
        res.status(statusCode(logoutResult.getMessage()));
        return serializer.toJson(logoutResult);
    }

    public Object handleCreateGame(Request req, Response res)
    {
        GameService gameService = new GameService();
        var serializer = new Gson();
        CreateGameResult createGameResult = gameService.createGame(serializer.fromJson(req.body(), CreateGameRequest.class), req.headers("authorization"));
        res.status(statusCode(createGameResult.getMessage()));
        return serializer.toJson(createGameResult);
    }

    public Object handleJoinGame(Request req, Response res)
    {
        GameService gameService = new GameService();
        var serializer = new Gson();
        String token = req.headers("authorization");
        JoinGameResult joinGameResult = gameService.joinGame(serializer.fromJson(req.body(), JoinGameRequest.class), token);
        res.status(statusCode(joinGameResult.getMessage()));
        return serializer.toJson(joinGameResult);
    }

    public Object handleListGames(Request req, Response res)
    {
        GameService gameService = new GameService();
        var serializer = new Gson();
        ListGameResult listGameResult = gameService.listGames(req.headers("authorization"));
        res.status(statusCode(listGameResult.getMessage()));
        return serializer.toJson(listGameResult);
    }

    private int statusCode(String message)
    {
        if (message == null) { return 200; }
        else if (message.equals("Error: bad request")) { return 400; }
        else if (message.equals("Error: unauthorized")) { return 401; }
        else if (message.equals("Error: already taken")) { return 403; }
        else { return 500; }
    }
}
