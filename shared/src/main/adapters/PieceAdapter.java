package adapters;

import chess.ChessPiece;
import chess.chessPieces.*;
import com.google.gson.*;
import java.lang.reflect.Type;

public class PieceAdapter implements JsonDeserializer<ChessPiece> {
    @Override
    public ChessPiece deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String pieceType = jsonObject.get("pieceType").getAsString();

        if (pieceType.equals(ChessPiece.PieceType.QUEEN.toString()))
        {
            return new Gson().fromJson(jsonElement, Queen.class);
        }
        else if (pieceType.equals(ChessPiece.PieceType.KING.toString()))
        {
            return new Gson().fromJson(jsonElement, King.class);
        }
        else if (pieceType.equals(ChessPiece.PieceType.BISHOP.toString()))
        {
            return new Gson().fromJson(jsonElement, Bishop.class);
        }
        else if (pieceType.equals(ChessPiece.PieceType.KNIGHT.toString()))
        {
            return new Gson().fromJson(jsonElement, Knight.class);
        }
        else if (pieceType.equals(ChessPiece.PieceType.ROOK.toString()))
        {
            return new Gson().fromJson(jsonElement, Rook.class);
        }
        else
        {
            return new Gson().fromJson(jsonElement, Pawn.class);
        }
    }
}