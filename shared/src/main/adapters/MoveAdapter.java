package adapters;

import chess.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class MoveAdapter implements JsonDeserializer<ChessMove>
{
    @Override
    public ChessMove deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessPosition.class, new PositionAdapter());

        return builder.create().fromJson(jsonElement, ChessMoveImpl.class);
    }
}
