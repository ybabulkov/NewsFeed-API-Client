package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class NewsClientException extends Exception {
    public NewsClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public NewsClientException(String message) {
        super(message);
    }
}
