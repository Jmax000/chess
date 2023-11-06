package adapters;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessGameImpl;
import com.google.gson.*;
import java.lang.reflect.Type;

public class GameAdapter implements JsonDeserializer<ChessGame>
{
    @Override
    public ChessGame deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessBoard.class, new BoardAdapter());

        return builder.create().fromJson(jsonElement, ChessGameImpl.class);
    }
}
