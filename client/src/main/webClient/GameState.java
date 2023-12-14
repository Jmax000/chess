package webClient;

import chess.ChessGame;

public class GameState
{
    int gameID;
    ChessGame game;
    chess.ChessGame.TeamColor teamColor;

    public GameState(int gameID, chess.ChessGame.TeamColor teamColor)
    {
        this.gameID = gameID;
        this.teamColor = teamColor;
    }
}
