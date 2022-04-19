package k5s.reviewdevelop.exception;

public class InvalidAuthenticationException extends RuntimeException{
    public InvalidAuthenticationException(String message) {
        super(message);
    }
}