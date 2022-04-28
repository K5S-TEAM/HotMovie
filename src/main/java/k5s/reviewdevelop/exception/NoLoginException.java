package k5s.reviewdevelop.exception;

public class NoLoginException  extends InvalidAuthenticationException{
    public NoLoginException(String message) {
        super(message);
    }
}
