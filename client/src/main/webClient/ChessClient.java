package webClient;

import java.util.Arrays;
import java.util.Vector;

import chess.ChessGame;
import exception.ResponseException;
import models.Game;
import requestResultObjects.*;

import static java.lang.Integer.parseInt;
import static ui.ChessBoardUI.drawChessBoard;

public class ChessClient
{
    private String username = null;
    private String authtoken = null;
    private Vector<Game> games = new Vector<>();
    private final ServerFacade server;
    private final String serverUrl;

    public State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl, Repl repl)
    {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
//        this.notificationHandler = notificationHandler;
    }

    public String eval(String input) throws ResponseException
    {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd)
        {
            case "register" -> register(params);
            case "login" -> login(params);
            case "create" -> createGame(params);
            case "join", "observe" -> joinGame(params);
            case "list" -> listGame();
            case "logout" -> logout();
            case "clear" -> clear();
            case "help" -> help();
            case "quit" -> "quit";
            default -> invalidCmd();
        };
    }

    public String register(String[] params) throws ResponseException
    {
        if (params.length == 3) {
            RegisterRequest request = new RegisterRequest();
            request.setUsername(params[0]);
            request.setPassword(params[1]);
            request.setEmail(params[2]);
            RegisterResult result = server.register(request);

            if (result.getMessage() == null)
            {
                username = result.getUsername();
                authtoken = result.getAuthToken();
                state = State.SIGNEDIN;

                return "Registered as " + username + ". \n";
            }
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String[] params) throws ResponseException
    {
        if (params.length == 2) {
            LoginRequest request = new LoginRequest();
            request.setUsername(params[0]);
            request.setPassword(params[1]);
            LoginResult result = server.login(request);

            if (result.getMessage() == null)
            {
                username = result.getUsername();
                authtoken = result.getAuthToken();
                state = State.SIGNEDIN;
                return "Logged in as "+  username + ". \n";
            }
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public String createGame(String[] params) throws ResponseException
    {
        assertSignedIn();
        if (params.length == 1) {
            CreateGameRequest request = new CreateGameRequest();
            request.setGameName(params[0]);
            CreateGameResult result = server.createGame(request, authtoken);

            if (result.getMessage() == null)
            {
                return request.getGameName() + "<" + result.getGameID() + "> created. \n";
            }
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    public String listGame() throws ResponseException
    {
        assertSignedIn();
        ListGameResult result = server.listGames(authtoken);
        games = result.getGames();

        if (games.isEmpty())
        {
            return "No games have been created yet. \n";
        }
        else
        {
            StringBuilder gamesToString = new StringBuilder();
            for (int i = 0; i < games.size(); i++) {
                Game game = games.get(i);
                gamesToString.append(i + 1).append(": ").append(game.getGameName()).append(" [WHITE: ");
                if (game.getWhiteUsername() == null || game.getWhiteUsername().isEmpty())
                {
                    gamesToString.append("available");
                }
                else
                {
                    gamesToString.append(game.getWhiteUsername());
                }
                gamesToString.append(", BLACK: ");
                if (game.getBlackUsername() == null || game.getBlackUsername().isEmpty()) {
                    gamesToString.append("available]");
                } else {
                    gamesToString.append(game.getBlackUsername()).append("]");
                }
                gamesToString.append("\n");
            }
            return gamesToString.toString();
        }
    }

    public String joinGame(String[] params) throws ResponseException
    {
        assertSignedIn();
        JoinGameRequest request = new JoinGameRequest();
        JoinGameResult result = null;
        Game game = null;
        if (params.length == 1 || params.length == 2)
        {
            game = findGame(params[0]);
            if (params.length == 2)
            {
                request.setPlayerColor(params[1]);
            }
            if (game != null)
            {
                request.setGameID(game.getGameID());
                result = server.joinGame(request, authtoken);
            }
        }

        if (game != null && result != null)
        {
            StringBuilder gameJoined = new StringBuilder();
            gameJoined.append("Joined ");
            gameJoined.append(game.getGameName());
            gameJoined.append(" as ");
            if (request.getPlayerColor() == null || request.getPlayerColor().isEmpty())
            {
                gameJoined.append("an observer.");
            }
            else
            {
                gameJoined.append(request.getPlayerColor());
                gameJoined.append(".");
            }
            game.getGame().getBoard().resetBoard(); // Temp FIXME
            gameJoined.append("\n");
            gameJoined.append(drawChessBoard(game.getGame().getBoard(), ChessGame.TeamColor.WHITE));
            gameJoined.append("\n");
            gameJoined.append(drawChessBoard(game.getGame().getBoard(), ChessGame.TeamColor.BLACK));

            return gameJoined.toString();
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE | BLACK | <empty>]");
    }

    private Game findGame(String listNumber) throws ResponseException
    {
        int index = parseInt(listNumber) - 1;
        if (index < games.size())
        {
            return games.get(index);
        }
        throw new ResponseException(400, "No game found. Try again.");
    }

    public String logout() throws ResponseException
    {
        assertSignedIn();
        server.logout(authtoken);
        state = State.SIGNEDOUT;
        authtoken = null;
        return "Logged out. \n";
    }

    public String clear() throws ResponseException
    {
        logout();
        server.clear();
        return "Database cleared. \n";
    }

    public String help()
    {
        if (state == State.SIGNEDOUT) {
            return """
                    - register <USERNAME> <PASSWORD> <EMAIL>
                    - login <USERNAME> <PASSWORD>
                    - quit
                    """;
        }
        return """
                - create <NAME>
                - join <ID> [WHITE | BLACK | <empty>]
                - observe <ID>
                - list
                - logout
                - help
                - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
    private String invalidCmd()
    {
        return "Invalid Command. Type Help for a list of valid commands. \n";
    }

}
