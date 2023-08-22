package ui.exception;

public class InvalidIntegerException extends Exception {

    public InvalidIntegerException() {
        super("Please enter a positive whole number value!");
    }
}
