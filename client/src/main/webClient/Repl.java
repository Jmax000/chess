package webClient;
import chess.ChessGame;
import webSocketMessages.serverMessages.ServerMessage;
import websocket.NotificationHandler;

import java.util.Objects;
import java.util.Scanner;
import static ui.ChessBoardUI.drawChessBoard;
import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler
{
    private final ChessClient client;
    public Repl(String serverUrl)
    {
        client = new ChessClient(serverUrl, this);
    }

    public void run()
    {
        System.out.println(SET_TEXT_COLOR_WHITE + SET_TEXT_BOLD + WHITE_KING +
                            "Welcome to 240 chess. Type Help to get started."
                            + WHITE_KING + RESET_TEXT_BOLD_FAINT);

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit"))
        {
            printPrompt();
            String line = scanner.nextLine();

            while (line.isEmpty())
            {
                printPrompt();
                line = scanner.nextLine();
            }

            try
            {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            }
            catch (Throwable e)
            {
                System.out.print(e.getMessage());
            }
        }
    }

    public void notify(ServerMessage serverMessage)
    {
        if (serverMessage.getServerMessageType() == ServerMessage.Type.LOAD_GAME)
        {
            client.inGameState.game = serverMessage.getGame();
            ChessGame.TeamColor color = Objects.requireNonNullElse(client.inGameState.teamColor, ChessGame.TeamColor.WHITE);

            if (serverMessage.getHighlightMoves() == null)
            {
                System.out.println("\n" + drawChessBoard(client.inGameState.game.getBoard(), color, null));
            }
            else
            {
                System.out.println("\n" + drawChessBoard(client.inGameState.game.getBoard(), client.inGameState.teamColor, serverMessage.getHighlightMoves()));
            }
            printPrompt();
        }
        else
        {
            System.out.println(serverMessage);
            printPrompt();
        }
    }

    private void printPrompt()
    {
        System.out.print("\n" + SET_TEXT_COLOR_WHITE + "[" + client.state + "] >>> " + SET_TEXT_COLOR_GREEN);
    }
}
