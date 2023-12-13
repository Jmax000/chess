package websocket;

import adapters.GameAdapter;
import adapters.MoveAdapter;
import adapters.PositionAdapter;
import chess.*;
import com.google.gson.GsonBuilder;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import exception.ResponseException;
import models.Authtoken;
import models.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Vector;

import static dataAccess.GameDAO.updateGame;

@WebSocket
public class WebSocketHandler
{
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, ResponseException
    {
        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessGame.class, new GameAdapter());
        builder.registerTypeAdapter(ChessPosition.class, new PositionAdapter());
        builder.registerTypeAdapter(ChessMove.class, new MoveAdapter());
        var serializer = builder.create();

        UserGameCommand command = serializer.fromJson(message, UserGameCommand.class);
        switch (command.getCommandType())
        {
            case JOIN_PLAYER -> joinPlayer(command, session);
            case JOIN_OBSERVER -> joinObserver(command, session);
            case MAKE_MOVE -> makeMove(command, session);
            case DRAW_BOARD -> drawBoard(command, session);
            case RESIGN -> resign(command, session);
            case LEAVE -> leave(command, session);
        }
    }

    private void joinPlayer(UserGameCommand command, Session session) throws IOException, DataAccessException
    {
        connections.add(command.getGameID(), command.getTeamColor(), session);
        // Get username by authtoken through AuthDAO
        // Get gameID (gameDAO.getGame()
        // if not null continue
        // make sure that the username trying to get it is right
        Authtoken authtoken = AuthDAO.findByToken(command.getAuthtoken());
        Game game = GameDAO.find(command.getGameID());
        if(game == null)
        {
            sendError(command.getGameID(), session, "Game could not be found");
            return;
            /*throw new ResponseException(500, "Game could not be found");*/
        }
        else if (authtoken == null)
        {
            sendError(command.getGameID(), session, "Username could not be found");
            return;
            /*throw new ResponseException(500, "Username could not be found");*/
        }

        // Server sends a LOAD_GAME message back to the root client.
        var notification = new ServerMessage(ServerMessage.Type.LOAD_GAME);
        notification.setGame(game.getGame());
        notification.setTeamColor(command.getTeamColor());
        connections.notify(command.getGameID(), session, notification);

        //Server sends a Notification message to all other clients in that game informing them what color the root client is joining as.
        notification = new ServerMessage(ServerMessage.Type.NOTIFICATION);
        notification.setMessage(authtoken.getUsername() + "has joined as " + command.getTeamColor());
        connections.broadcast(command.getGameID(), opponent(command.getTeamColor()), notification);
        connections.broadcast(command.getGameID(), null, notification);
    }
    private void joinObserver(UserGameCommand command, Session session) throws IOException, DataAccessException {
        connections.add(command.getGameID(), command.getTeamColor(), session);

        Authtoken authtoken = AuthDAO.findByToken(command.getAuthtoken());
        Game game = GameDAO.find(command.getGameID());
        if(game == null)
        {
            sendError(command.getGameID(), session, "Game could not be found");
            return;
            /*throw new ResponseException(500, "Game could not be found");*/
        }
        else if (authtoken == null)
        {
            sendError(command.getGameID(), session, "Username could not be found");
            return;
            /*throw new ResponseException(500, "Username could not be found");*/
        }

        // Server sends a LOAD_GAME message back to the root client.
        var notification = new ServerMessage(ServerMessage.Type.LOAD_GAME);
        connections.notify(command.getGameID(), session, notification);

        //Server sends a Notification message to all other clients in that game informing them the root client joined as an observer.
        notification = new ServerMessage(ServerMessage.Type.NOTIFICATION);
        notification.setMessage(authtoken.getUsername() + "has joined as an observer.");
        connections.broadcast(command.getGameID(), ChessGame.TeamColor.WHITE, notification);
        connections.broadcast(command.getGameID(), ChessGame.TeamColor.BLACK, notification);
        connections.broadcast(command.getGameID(), null, notification);
    }
    private void makeMove(UserGameCommand command, Session session) throws IOException, DataAccessException {
        Game game = GameDAO.find(command.getGameID());
        if (game != null)
        {
            try
            {
                ChessGame chessGame = game.getGame();
                ChessMove move = command.getMove();
                chessGame.makeMove(move);
                game.setGame(chessGame);
                GameDAO.updateGame(game);
            }
            catch (InvalidMoveException e)
            {
                sendError(command.getGameID(), session, e.getMessage());
            }
        }
        else
        {
            sendError(command.getGameID(), session, "Game could not be found");
            return;
        }

        //Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
        var notification = new ServerMessage(ServerMessage.Type.LOAD_GAME);
        notification.setGame(game.getGame());
        connections.broadcast(command.getGameID(), ChessGame.TeamColor.WHITE, notification);
        connections.broadcast(command.getGameID(), ChessGame.TeamColor.BLACK, notification);
        connections.broadcast(command.getGameID(), null, notification);

        //Server sends a Notification message to all other clients in that game informing them what move was made.
        notification = new ServerMessage(ServerMessage.Type.NOTIFICATION);
        notification.setMessage("Enter player move here");
        connections.broadcast(command.getGameID(), opponent(command.getTeamColor()), notification);
        connections.broadcast(command.getGameID(), null, notification);
    }

    private void drawBoard(UserGameCommand command, Session session) throws DataAccessException, IOException
    {
        Authtoken authtoken = AuthDAO.findByToken(command.getAuthtoken());
        Game game = GameDAO.find(command.getGameID());
        if(game == null)
        {
            sendError(command.getGameID(), session, "Game could not be found");
            return;
        }
        else if (authtoken == null)
        {
            sendError(command.getGameID(), session, "Not authorized.");
            return;
        }

        var notification = new ServerMessage(ServerMessage.Type.LOAD_GAME);
        if (command.getHighlightPosition() != null)
        {
            if(game.getGame().getBoard().getPiece(command.getHighlightPosition()) != null)
            {
                notification.setHighlightMoves(game.getGame().validMoves(command.getHighlightPosition()));
            }
            else
            {
                sendError(command.getGameID(), session, "There is no piece at that position. Try again.");
                return;
            }

        }
        notification.setGame(game.getGame());
        notification.setTeamColor(command.getTeamColor());
        connections.notify(command.getGameID(), session, notification);

    }
    private void resign(UserGameCommand command, Session session) throws IOException, DataAccessException
    {
        Authtoken authtoken = AuthDAO.findByToken(command.getAuthtoken());
        Game game = GameDAO.find(command.getGameID());
        if(game == null)
        {
            sendError(command.getGameID(), session, "Game could not be found");
            return;
            /*throw new ResponseException(500, "Game could not be found");*/
        }
        else if (authtoken == null)
        {
            sendError(command.getGameID(), session, "Username could not be found");
            return;
            /*throw new ResponseException(500, "Username could not be found");*/
        }

        game.getGame().setTeamTurn(null);
        updateGame(game);

        connections.remove(command.getGameID(), session);

        var notification = new ServerMessage(ServerMessage.Type.NOTIFICATION);
        notification.setMessage(authtoken.getUsername() + "has resigned. " + opponent(command.getTeamColor()) + " won!");
        connections.broadcast(command.getGameID(), ChessGame.TeamColor.WHITE, notification);
        connections.broadcast(command.getGameID(), ChessGame.TeamColor.BLACK, notification);
        connections.broadcast(command.getGameID(), null, notification);
    }
    private void leave(UserGameCommand command, Session session) throws IOException, DataAccessException {
        Authtoken authtoken = AuthDAO.findByToken(command.getAuthtoken());
        Game game = GameDAO.find(command.getGameID());
        if(game == null)
        {
            sendError(command.getGameID(), session, "Game could not be found");
            return;
            /*throw new ResponseException(500, "Game could not be found");*/
        }
        else if (authtoken == null)
        {
            sendError(command.getGameID(), session, "Username could not be found");
            return;
            /*throw new ResponseException(500, "Username could not be found");*/
        }

        if (command.getTeamColor() == ChessGame.TeamColor.WHITE)
        {
            game.setWhiteUsername(null);
        }
        else if (command.getTeamColor() == ChessGame.TeamColor.BLACK)
        {
            game.setBlackUsername(null);
        }
        updateGame(game);


        connections.remove(command.getGameID(), session);

        var notification = new ServerMessage(ServerMessage.Type.NOTIFICATION);
        notification.setMessage(authtoken.getUsername() + "has left.");
        connections.broadcast(command.getGameID(), ChessGame.TeamColor.WHITE, notification);
        connections.broadcast(command.getGameID(), ChessGame.TeamColor.BLACK, notification);
        connections.broadcast(command.getGameID(), null, notification);
    }

    private ChessGame.TeamColor opponent(ChessGame.TeamColor playerColor)
    {
        if (playerColor == ChessGame.TeamColor.WHITE)
        {
            return ChessGame.TeamColor.BLACK;
        }
        else if (playerColor == ChessGame.TeamColor.BLACK)
        {
            return ChessGame.TeamColor.WHITE;
        }
        else
        {
            return null;
        }
    }

    void sendError(int gameID, Session session, String message) throws IOException
    {
        var notification = new ServerMessage(ServerMessage.Type.ERROR);
        notification.setMessage(message);
        connections.notify(gameID, session, notification);
    }
}
