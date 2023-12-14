package webSocketMessages.serverMessages;

import chess.ChessGame;
import chess.ChessMove;

import java.util.Collection;
import java.util.Objects;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    Type serverMessageType;
    String message;
    ChessGame game;
    ChessGame.TeamColor teamColor;
    Collection<ChessMove> highlightMoves;

    public enum Type {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(Type type) {
        this.serverMessageType = type;
    }

    public Type getServerMessageType() {
        return this.serverMessageType;
    }
    public void setServerMessageType(Type type)
    {
        this.serverMessageType = type;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public ChessGame getGame()
    {
        return game;
    }

    public void setGame(ChessGame game)
    {
        this.game = game;
    }

    public ChessGame.TeamColor getTeamColor()
    {
        return teamColor;
    }

    public void setTeamColor(ChessGame.TeamColor teamColor)
    {
        this.teamColor = teamColor;
    }

    public Collection<ChessMove> getHighlightMoves()
    {
        return highlightMoves;
    }

    public void setHighlightMoves(Collection<ChessMove> highlightMoves)
    {
        this.highlightMoves = highlightMoves;
    }

    @Override
    public String toString()
    {
        return SET_TEXT_COLOR_BLUE + "[" + serverMessageType + "]: " + message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ServerMessage))
            return false;
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
