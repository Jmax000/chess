package adapters;

import chess.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class PositionAdapter implements JsonDeserializer<ChessPosition>
{
    @Override
    public ChessPosition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int row = jsonObject.get("row").getAsInt();
        int col = jsonObject.get("col").getAsInt();

        return new ChessPositionImpl(row,col);
    }
}
