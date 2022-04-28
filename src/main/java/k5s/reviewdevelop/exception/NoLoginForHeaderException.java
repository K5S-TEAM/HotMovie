package k5s.reviewdevelop.exception;


public class NoLoginForHeaderException  extends InvalidAuthenticationException{
    public NoLoginForHeaderException(String message) {
        super(message);
    }
}
