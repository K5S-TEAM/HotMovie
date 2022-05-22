package k5s.reviewdevelop.api;

import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

public class ExceptionControl {

    protected static boolean ConnectionError(Throwable throwable) {
        return throwable instanceof TimeoutException || throwable instanceof WebClientRequestException;
    }
}
