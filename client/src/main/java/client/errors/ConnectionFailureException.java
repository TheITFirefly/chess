package client.errors;

public class ConnectionFailureException extends Exception {
    public ConnectionFailureException(String message) {
        super(message);
    }
}
