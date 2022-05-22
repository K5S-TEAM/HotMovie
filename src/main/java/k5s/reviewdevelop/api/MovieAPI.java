package k5s.reviewdevelop.api;

import k5s.reviewdevelop.dto.MovieRequestDto;
import k5s.reviewdevelop.dto.MovieResponseDto;
import k5s.reviewdevelop.dto.ScoreUpdateRequestDto;
import k5s.reviewdevelop.exception.InvalidAuthenticationException;
import k5s.reviewdevelop.exception.NoMovieException;
import k5s.reviewdevelop.service.WebClientService;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeoutException;


@Service
@RequiredArgsConstructor
@Transactional
public class MovieAPI {

    private final WebClientService webClientService;

    @Transactional
    public String requestMovieName(Long movieId) {

        if (movieId == null)
        {
            throw new NoMovieException("없는 영화입니다");
        }

        MovieRequestDto dto = new MovieRequestDto(movieId);

        MovieResponseDto result = webClientService.setMovieWebClient().post()
                .uri("/movies")
                .body(Mono.just(dto), MovieRequestDto.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new NoMovieException("영화오류")))
                .bodyToMono(MovieResponseDto.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(TimeoutException.class, ex -> new NoMovieException("연결시간초과"))
                .block();

        if (result.getMovieName() == null) {
            throw new NoMovieException("삭제된 영화입니다");
        }

        return result.getMovieName();
    }

    @Transactional
    public String sendMovieAverageScore(Long movieId, double averageScore) {


        ScoreUpdateRequestDto dto = new ScoreUpdateRequestDto(averageScore);

        String result = webClientService.setMovieWebClient().patch().uri("/movies/{movieId}", movieId)
                .body(Mono.just(dto), ScoreUpdateRequestDto.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new NoMovieException("영화점수오류")))
                .bodyToMono(String.class)
                .onErrorMap(ExceptionControl::ConnectionError, ex -> new NoMovieException("서버와 연결이 초과"))
                .block();
        return result;

    }

}
