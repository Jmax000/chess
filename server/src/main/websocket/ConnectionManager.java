package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Vector<Connection>> connectedGames = new ConcurrentHashMap<>();
    public void add(int gameID, ChessGame.TeamColor teamColor, Session session)
    {
        var connection = new Connection(gameID, teamColor, session);
        Vector<Connection> connections = connectedGames.get(gameID);
        if (connections == null)
        {
            connections = new Vector<>();
        }
        connections.add(connection);
        connectedGames.put(gameID, connections);
    }

    public void remove(int gameID, Session session)
    {
        Vector<Connection> connections = connectedGames.get(gameID);
        connections.removeIf(c -> c.session == session);
    }

    final public boolean verifySession(int gameID, ChessGame.TeamColor sessionColor, Session session) throws IOException
    {
        Vector<Connection> connections = connectedGames.get(gameID);
        for (var c : connections)
        {
            if (c.session.isOpen() && c.session == session)
            {
                return c.teamColor == sessionColor;
            }
        }
        return true;
    }

    public void notify(int gameID, ServerMessage notification) throws IOException
    {
        Vector<Connection> connections = connectedGames.get(gameID);
        for (var c : connections) {
            if (c.session.isOpen())
            {
                c.send(new Gson().toJson(notification));
            }
            else
            {
                connections.remove(c);
            }
        }
    }
    public void notifyExcept(int gameID, Session exclude, ServerMessage notification) throws IOException
    {
        Vector<Connection> connections = connectedGames.get(gameID);
        for (var c : connections) {
            if (c.session.isOpen())
            {
                if (c.session != exclude)
                {
                    c.send(new Gson().toJson(notification));
                }
            }
            else
            {
                connections.remove(c);
            }
        }
    }

    public void notifyRoot(int gameID, Session session, ServerMessage notification) throws IOException
    {
        Vector<Connection> connections = connectedGames.get(gameID);
        for (var c : connections) {
            if (c.session.isOpen())
            {
                if (c.session == session) {
                    c.send(new Gson().toJson(notification));
                }
            }
            else
            {
                connections.remove(c);
            }
        }
    }
}
