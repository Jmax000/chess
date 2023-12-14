package webSocketMessages.userCommands;

import chess.*;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        DRAW_BOARD,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;
    private final String authToken;
    private int gameID;
    private ChessGame.TeamColor teamColor;
    private ChessMove move;
    private ChessPiece.PieceType promoType;
    private ChessPosition highlightPosition;


    public String getAuthToken() {
        return authToken;
    }

    public int getGameID()
    {
        return gameID;
    }

    public void setGameID(int gameID)
    {
        this.gameID = gameID;
    }

    public ChessGame.TeamColor getTeamColor()
    {
        return teamColor;
    }

    public void setTeamColor(ChessGame.TeamColor teamColor)
    {
        this.teamColor = teamColor;
    }

    public ChessMove getMove()
    {
        return move;
    }

    public void setMove(ChessMove move)
    {
        this.move = move;
    }

    public void setPromoType(ChessPiece.PieceType promoType) {
        this.promoType = promoType;
    }

    public ChessPiece.PieceType getPromoType()
    {
        return promoType;
    }

    public CommandType getCommandType()
    {
        return this.commandType;
    }

    public void setCommandType(CommandType commandType)
    {
        this.commandType = commandType;
    }

    public ChessPosition getHighlightPosition()
    {
        return highlightPosition;
    }

    public void setHighlightPosition(ChessPosition highlightPosition)
    {
        this.highlightPosition = highlightPosition;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthToken(), that.getAuthToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthToken());
    }
}
