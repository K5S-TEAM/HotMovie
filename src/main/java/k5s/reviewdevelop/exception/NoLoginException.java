package k5s.reviewdevelop.exception;


public class NoLoginException  extends InvalidAuthenticationException{
    public NoLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
