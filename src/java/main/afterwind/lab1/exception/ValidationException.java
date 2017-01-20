package afterwind.lab1.exception;

/**
 * Thrown when an invalid entity has been found
 */
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }

}
