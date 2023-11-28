package webClient;

import adapters.GameAdapter;
import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.ResponseException;
import requestResultObjects.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;


public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void clear() throws ResponseException
    {
        var path = "/db";
        this.makeRequest("DELETE", path, null, ClearApplicationResult.class, null);
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException
    {
        var path = "/user";
        return this.makeRequest("POST", path, request, RegisterResult.class, null);
    }

    public LoginResult login(LoginRequest request) throws ResponseException
    {
        var path = "/session";
        return this.makeRequest("POST", path, request, LoginResult.class, null);
    }

    public LogoutResult logout(String authtoken) throws ResponseException
    {
        var path = "/session";
        return this.makeRequest("DELETE", path, null, LogoutResult.class, authtoken);
    }

    public ListGameResult listGames(String authtoken) throws ResponseException
    {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListGameResult.class, authtoken);
    }

    public CreateGameResult createGame(CreateGameRequest request, String authtoken) throws ResponseException
    {
        var path = "/game";
        return this.makeRequest("POST", path, request, CreateGameResult.class, authtoken);
    }

    public JoinGameResult joinGame(JoinGameRequest request, String authtoken) throws ResponseException
    {
        var path = "/game";
        return this.makeRequest("PUT", path, request, JoinGameResult.class, authtoken);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authtoken) throws ResponseException
    {
        try
        {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if (authtoken != null) { http.addRequestProperty("authorization", authtoken); }
            http.setDoOutput(true);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        }
        catch (Exception ex)
        {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException
    {
        if (request != null)
        {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream())
            {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException
    {
        T response = null;
        if (http.getContentLength() < 0)
        {
            try (InputStream respBody = http.getInputStream())
            {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null)
                {
                    response = deserialize(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException
    {
        var status = http.getResponseCode();
        if (!isSuccessful(status))
        {
            throw new ResponseException(status, http.getResponseMessage());
        }
    }

    private boolean isSuccessful(int status) { return status / 100 == 2; }

    private static <T> T deserialize(Reader reader, Class<T> responseClass)
    {
        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessGame.class, new GameAdapter());

        return builder.create().fromJson(reader, responseClass);
    }
}