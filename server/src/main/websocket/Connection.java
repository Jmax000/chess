package websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public int gameID;
    //public String authString; Do I need? FIXME
    public Session session;
    public ChessGame.TeamColor teamColor;

    public Connection(int gameID, ChessGame.TeamColor teamColor, Session session)
    {
        this.gameID = gameID;
        this.teamColor = teamColor;
        this.session = session;
    }

    public void send(String msg) throws IOException
    {
        session.getRemote().sendString(msg);
    }
}
