package k5s.reviewdevelop.service.api;

import k5s.reviewdevelop.dto.MovieRequestDto;
import k5s.reviewdevelop.dto.MovieResponseDto;
import k5s.reviewdevelop.dto.ScoreUpdateRequestDto;
import k5s.reviewdevelop.exception.InvalidAuthenticationException;
import k5s.reviewdevelop.exception.NoMovieException;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
@Transactional
public class MovieAPI {

    @Value("${msa.movie}")
    String movieServerUrl;

    @Transactional
    public String requestMovieName(Long movieId) {


        WebClient webClient = WebClient.builder().baseUrl(movieServerUrl).build();

        if (movieId == null)
        {
            throw new NoMovieException("없는 영화입니다");
        }

        MovieRequestDto dto = new MovieRequestDto(movieId);

        MovieResponseDto result = webClient.post()
                .uri("/movies")
                .body(Mono.just(dto), MovieRequestDto.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new NoMovieException("영화오류")))
                .bodyToMono(MovieResponseDto.class)
                .block();

        if (result.getMovieName() == null) {
            throw new NoMovieException("삭제된 영화입니다");
        }

        return result.getMovieName();
    }

    @Transactional
    public String responseMovieAverageScore(Long movieId, double averageScore) {


        WebClient webClient = WebClient.builder().baseUrl(movieServerUrl).build();

        ScoreUpdateRequestDto dto = new ScoreUpdateRequestDto(averageScore);

        String result = webClient.patch().uri("/movies/{movieId}", movieId)
                .body(Mono.just(dto), ScoreUpdateRequestDto.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new InvalidAuthenticationException("영화서버 오류")))
                .bodyToMono(String.class)
                .block();
        return result;

    }

}
