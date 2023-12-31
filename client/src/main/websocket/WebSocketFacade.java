package websocket;

import adapters.GameAdapter;
import adapters.MoveAdapter;
import adapters.PositionAdapter;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint
{
    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>()
            {
                @Override
                public void onMessage(String message)
                {
                    var builder = new GsonBuilder();
                    builder.registerTypeAdapter(ChessGame.class, new GameAdapter());
                    builder.registerTypeAdapter(ChessPosition.class, new PositionAdapter());
                    builder.registerTypeAdapter(ChessMove.class, new MoveAdapter());
                    ServerMessage notification = builder.create().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void sendMessage(UserGameCommand command) throws ResponseException
    {
        try
        {
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException ex)
        {
            throw new ResponseException(500, ex.getMessage());
        }
    }

}
