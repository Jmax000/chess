package webClient;

import java.util.Arrays;
import java.util.Vector;

import chess.*;
import exception.ResponseException;
import models.Game;
import requestResultObjects.*;
import webSocketMessages.userCommands.*;
import websocket.*;

import static java.lang.Integer.parseInt;

public class ChessClient
{
    private String username = null;
    private String authtoken = null;
    private Vector<Game> games = new Vector<>();
    private final ServerFacade server;
    private final String serverUrl;
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;

    record GameState(int gameID, ChessGame.TeamColor teamColor) {}
    private GameState inGameState;
    public State state = State.SIGNED_OUT;

    public ChessClient(String serverUrl, NotificationHandler notificationHandler)
    {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }

    public String eval(String input) throws ResponseException
    {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);

        return switch (state)
        {
            case SIGNED_OUT:
                yield switch (cmd)
                {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "help" -> help();
                    case "quit" -> "quit";
                    default -> invalidCmd();
                };
            case SIGNED_IN:
                yield switch (cmd)
                {
                    case "create" -> createGame(params);
                    case "join", "observe" -> joinGameHTTP(params);
                    case "list" -> listGame();
                    case "logout" -> logout();
                    case "help" -> help();
                    case "quit" -> "quit";
                    default -> invalidCmd();
                };
            case IN_GAME:
                yield switch (cmd)
                {
                    case "join" -> joinGameWS(params);
                    case "draw_board", "draw" -> drawBoard();
                    case "highlight_moves", "highlight" -> highlightMoves(params);
                    case "resign" -> resign();
                    case "leave" -> leave();
                    case "instructions" -> instructions();
                    case "help" -> help();
                    default -> makeMove(cmd);
                };
            case OBSERVING:
                yield switch (cmd)
                {
                    case "leave" -> leave();
                    case "help" -> help();
                    default -> invalidCmd();
                };

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
                state = State.SIGNED_IN;

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
                state = State.SIGNED_IN;
                return "Logged in as "+  username + ". \n";
            }
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public String createGame(String[] params) throws ResponseException
    {
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

    private Game findGame(String listNumber) throws ResponseException
    {
        int index = parseInt(listNumber) - 1;
        if (index < games.size())
        {
            return games.get(index);
        }
        throw new ResponseException(400, "No game found. Try again.");
    }
    public String joinGameHTTP(String[] params) throws ResponseException
    {
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
                state = State.OBSERVING;
                gameJoined.append("an observer.");
            }
            else
            {
                state = State.IN_GAME;
                gameJoined.append(request.getPlayerColor());
                gameJoined.append(".");
            }

            ws = new WebSocketFacade(serverUrl, notificationHandler);
            inGameState = new GameState(request.getGameID(), null);

            return gameJoined.toString();
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE | BLACK | <empty>]");
    }

    public String joinGameWS(String[] params) throws ResponseException
    {
        if (params.length == 1)
        {
            ChessGame.TeamColor color = null;
            String team = params[0];
            if (team != null)
            {
                color = switch (team.toLowerCase())
                {
                    case "white" -> ChessGame.TeamColor.WHITE;
                    case "black" -> ChessGame.TeamColor.BLACK;
                    default -> null;
                };
            }

            if (team == null)
            {
                UserGameCommand command = new UserGameCommand(authtoken);
                command.setGameID(inGameState.gameID);
                command.setCommandType(UserGameCommand.CommandType.JOIN_OBSERVER);
                ws.sendMessage(command);
                return "";
            }
            else if (color != null)
            {
                inGameState = new GameState(inGameState.gameID, color);
                UserGameCommand command = new UserGameCommand(authtoken);
                command.setGameID(inGameState.gameID);
                command.setTeamColor(color);
                command.setCommandType(UserGameCommand.CommandType.JOIN_PLAYER);
                ws.sendMessage(command);
                return "";
            }
        }
        throw new ResponseException(400, "Expected: [WHITE | BLACK | <empty>]");
    }

    String makeMove(String cmd) throws ResponseException
    {
        ChessPosition startPosition = null;
        ChessPosition endPosition = null;
        ChessPiece.PieceType promoType = null;

        if (cmd.length() == 5 && cmd.charAt(2) == '-')
        {
            int row = Character.getNumericValue(cmd.charAt(1)) - 1;
            int col = cmd.charAt(0) - 'a';
            startPosition = new ChessPositionImpl(row, col);
            row = Character.getNumericValue(cmd.charAt(4)) - 1;
            col = cmd.charAt(3) - 'a';
            endPosition = new ChessPositionImpl(row, col);
        }
        else if (cmd.length() == 7 && cmd.charAt(2) == '-' && cmd.charAt(5) == '=')
        {
            promoType = switch (cmd.charAt(6))
            {
                case 'q' -> ChessPiece.PieceType.QUEEN;
                case 'b' -> ChessPiece.PieceType.BISHOP;
                case 'n' -> ChessPiece.PieceType.KNIGHT;
                case 'r' -> ChessPiece.PieceType.ROOK;
                default -> throw new ResponseException(400, cmd.charAt(6) + " is not an available promotion piece.");
            };
            int row = Character.getNumericValue(cmd.charAt(1)) - 1;
            int col = cmd.charAt(0) - 'a';
            startPosition = new ChessPositionImpl(row, col);
            row = Character.getNumericValue(cmd.charAt(4)) - 1;
            col = cmd.charAt(3) - 'a';
            endPosition = new ChessPositionImpl(row, col);
        }

        if (startPosition != null)
        {
            UserGameCommand command = new UserGameCommand(authtoken);
            command.setGameID(inGameState.gameID);
            command.setTeamColor(inGameState.teamColor);
            command.setMove(new ChessMoveImpl(startPosition, endPosition));
            command.setPromoType(promoType);
            command.setCommandType(UserGameCommand.CommandType.MAKE_MOVE);
            ws.sendMessage(command);
            return "";
        }
        throw new ResponseException(400, "Expected: letterNumber-letterNumber (example: a2-a1)");
    }

    String drawBoard() throws ResponseException
    {
        UserGameCommand command = new UserGameCommand(authtoken);
        command.setGameID(inGameState.gameID);
        command.setTeamColor(inGameState.teamColor);
        command.setCommandType(UserGameCommand.CommandType.DRAW_BOARD);

        ws.sendMessage(command);
        return "";
    }

    private String highlightMoves(String[] params) throws ResponseException
    {
        if (params.length == 1 && params[0].length() == 2)
        {
            int row = Character.getNumericValue(params[0].charAt(1)) - 1;
            int col = params[0].charAt(0) - 'a';
            ChessPosition position = new ChessPositionImpl(row, col);
            if (position.validPos())
            {
                UserGameCommand command = new UserGameCommand(authtoken);
                command.setGameID(inGameState.gameID);
                command.setTeamColor(inGameState.teamColor);
                command.setHighlightPosition(position);
                command.setCommandType(UserGameCommand.CommandType.DRAW_BOARD);

                ws.sendMessage(command);
                return "";
            }
        }
        throw new ResponseException(400, "Expected: ROW_COLUMN (example: e1)");
    }

    String resign() throws ResponseException
    {
        UserGameCommand command = new UserGameCommand(authtoken);
        command.setGameID(inGameState.gameID);
        command.setCommandType(UserGameCommand.CommandType.RESIGN);
        ws.sendMessage(command);

        return "";
    }

    String leave() throws ResponseException
    {
        UserGameCommand command = new UserGameCommand(authtoken);
        command.setGameID(inGameState.gameID);
        command.setTeamColor(inGameState.teamColor);
        command.setCommandType(UserGameCommand.CommandType.LEAVE);
        ws.sendMessage(command);

        state = State.SIGNED_IN;
        return "";
    }

    public String logout() throws ResponseException
    {
        server.logout(authtoken);
        state = State.SIGNED_OUT;
        authtoken = null;
        return "Logged out. \n";
    }

    public String help()
    {
        if (state == State.SIGNED_OUT)
        {

            return """
                    - register <USERNAME> <PASSWORD> <EMAIL>
                    - login <USERNAME> <PASSWORD>
                    - quit
                    """;
        }
        else if (state == State.SIGNED_IN)
        {
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
        else if (state == State.IN_GAME)
        {

            return """
            - join [WHITE | BLACK | <empty>]
            - <YOUR_MOVE>
            - instructions
            - resign
            - leave
            - help
            """;
        }
        else
        {
            return """
            - leave
            - help
            """;
        }
    }

    private String instructions()
    {
        return """
                Chess Game Instructions: How to Input Moves

                Piece Abbreviations:
                - King: K
                - Queen: Q
                - Rook: R
                - Knight: N
                - Bishop: B
                - Pawn: P
                
                File and Rank Coordinates:
                - Files are labeled with letters from a to h, from left to right.
                - Ranks are labeled with numbers from 1 to 8, from bottom to top.
                
                Notation for Moves:
                - Each move is represented by the current square followed by a dash and the destination square.
                - Each square is described by the file followed by the rank.
                        Examples:
                            - Pawn moves: e2-e4 (moves pawn to e4)
                            - Pawn promotion: e7-e8=Q (promotes pawn to queen on e8)
                            - Knight moves: g1-f3 (moves knight to f3)
                            - Bishop moves: f1-b5 (moves bishop to b5)
                            - Rook moves: a8-a1 (moves rook to a1)
                            - Queen moves: d1-d4 (moves queen to d4)
                            - King moves: f1-g1 (moves king to g1)

                Special Moves:
                - Pawn promotion: e8=Q (promotes pawn to queen on e8)
                - Castling: O-O (kingside) or O-O-O (queenside)
                - En passant: exd6 (captures pawn en passant on d6)
                
                Entering a Move:
                - To make a move, simply type the algebraic notation for your desired move and press enter.

                If you have any questions, type "help" for assistance.
                
                """;
    }

    private String invalidCmd()
    {
        return "Invalid Command. Type Help for a list of valid commands. \n";
    }

}
