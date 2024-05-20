package org.example.Exception;

public class GameException extends Exception {
    private ErrorCode errorCode;

    public GameException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }


    //diferite tipuri de erori
    public enum ErrorCode {
        INCORRECT_POSITIONS("Incorrect number of positions."),
        INVALID_POSITION_FORMAT("Invalid position format. Use format 'A1', 'B2', etc."),
        POSITIONS_NOT_EMPTY("All positions must be empty."),
        POSITIONS_NOT_CONSECUTIVE("Invalid position format. Must be consecutive.");

        private final String message;

        ErrorCode(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
