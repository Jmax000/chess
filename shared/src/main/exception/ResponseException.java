package exception;

import static ui.EscapeSequences.*;

public class ResponseException extends Exception {
    final private int statusCode;

    public ResponseException(int statusCode, String message)
    {
        super(SET_TEXT_COLOR_BLUE + statusCode + ": " + message + "\n");

        this.statusCode = statusCode;
    }

    public int StatusCode() {
        return statusCode;
    }
}