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
        Authtoken authtoken = AuthDAO.findByToken(command.getAuthToken());
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
        else if (command.getTeamColor() == ChessGame.TeamColor.WHITE && game.getWhiteUsername() != null && !game.getWhiteUsername().equals(authtoken.getUsername()))
        {
            sendError(command.getGameID(), session, "White username has been taken.");
            return;
        }
        else if (command.getTeamColor() == ChessGame.TeamColor.BLACK && game.getBlackUsername() != null &&!game.getBlackUsername().equals(authtoken.getUsername()))
        {
            sendError(command.getGameID(), session, "Black username has been taken.");
            return;
        }

        connections.add(command.getGameID(), command.getTeamColor(), session);

        // Server sends a LOAD_GAME message back to the root client.
        var notification = new ServerMessage(ServerMessage.Type.LOAD_GAME);
        notification.setGame(game.getGame());
        notification.setTeamColor(command.getTeamColor());
        connections.notifyRoot(command.getGameID(), session, notification);

        //Server sends a Notification message to all other clients in that game informing them what color the root client is joining as.
        notification = new ServerMessage(ServerMessage.Type.NOTIFICATION);
        notification.setMessage(authtoken.getUsername() + " has joined as " + command.getTeamColor());
        connections.notifyExcept(command.getGameID(), session, notification);
    }
    private void joinObserver(UserGameCommand command, Session session) throws IOException, DataAccessException {
        connections.add(command.getGameID(), command.getTeamColor(), session);

        Authtoken authtoken = AuthDAO.findByToken(command.getAuthToken());
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

        // Server sends a LOAD_GAME message back to the root client.
        var notification = new ServerMessage(ServerMessage.Type.LOAD_GAME);
        connections.notifyRoot(command.getGameID(), session, notification);

        //Server sends a Notification message to all other clients in that game informing them the root client joined as an observer.
        notification = new ServerMessage(ServerMessage.Type.NOTIFICATION);
        notification.setMessage(authtoken.getUsername() + "has joined as an observer.");
        connections.notifyExcept(command.getGameID(), session, notification);
    }
    private void makeMove(UserGameCommand command, Session session) throws IOException, DataAccessException
    {
        Authtoken authtoken = AuthDAO.findByToken(command.getAuthToken());
        Game game = GameDAO.find(command.getGameID());

        if (authtoken == null)
        {
            sendError(command.getGameID(), session, "Not authorized.");
            return;
        }
        else if (game == null)
        {
            sendError(command.getGameID(), session, "Game could not be found");
            return;
        }
        else if (command.getMove() != null)
        {
            if (!command.getMove().getStartPosition().validPos() && !command.getMove().getEndPosition().validPos())
            {
                sendError(command.getGameID(), session, "Your move is out of bounds.");
                return;
            }
        }

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
            return;
        }

        //Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
        var notification = new ServerMessage(ServerMessage.Type.LOAD_GAME);
        notification.setGame(game.getGame());
        connections.notify(command.getGameID(), notification);

        //Server sends a Notification message to all other clients in that game informing them what move was made.
        notification = new ServerMessage(ServerMessage.Type.NOTIFICATION);
        notification.setMessage(authtoken.getUsername() + " played " + displayMove(game.getGame(), command.getMove().getEndPosition()));
        connections.notifyExcept(command.getGameID(), session, notification);

    }

    private static String displayMove(ChessGame game, ChessPosition endPos)
    {
        ChessPiece piece = game.getBoard().getBoard()[endPos.getRow()][endPos.getCol()];
        String pieceType = switch (piece.getPieceType())
        {
            case KING -> "K";
            case QUEEN -> "Q";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case ROOK -> "R";
            case PAWN -> "P";
        };
        int row = endPos.getRow() + 1;
        char col = (char)(endPos.getCol() + 'a');

        return pieceType + col + row;
    }

    private void drawBoard(UserGameCommand command, Session session) throws DataAccessException, IOException
    {
        Authtoken authtoken = AuthDAO.findByToken(command.getAuthToken());
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
        connections.notifyRoot(command.getGameID(), session, notification);
    }
    private void resign(UserGameCommand command, Session session) throws IOException, DataAccessException
    {
        Authtoken authtoken = AuthDAO.findByToken(command.getAuthToken());
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

        game.getGame().setTeamTurn(null);
        updateGame(game);

        var notification = new ServerMessage(ServerMessage.Type.NOTIFICATION);
        notification.setMessage(authtoken.getUsername() + " has resigned. " + game.getBlackUsername() + " wins!");
        connections.notify(command.getGameID(), notification);
    }
    private void leave(UserGameCommand command, Session session) throws IOException, DataAccessException {
        Authtoken authtoken = AuthDAO.findByToken(command.getAuthToken());
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
        connections.notify(command.getGameID(), notification);
    }

    void sendError(int gameID, Session session, String message) throws IOException
    {
        var notification = new ServerMessage(ServerMessage.Type.ERROR);
        notification.setMessage(message);
        connections.notifyRoot(gameID, session, notification);
    }
}
