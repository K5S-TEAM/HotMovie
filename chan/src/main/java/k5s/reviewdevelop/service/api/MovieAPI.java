package k5s.reviewdevelop.service.api;

import k5s.reviewdevelop.dto.MovieRequestDto;
import k5s.reviewdevelop.dto.MovieResponseDto;
import k5s.reviewdevelop.exception.InvalidAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
            return null;
        }

        MovieRequestDto dto = new MovieRequestDto(movieId);

        MovieResponseDto result = webClient.post()
                .uri("/movies")
                .body(Mono.just(dto), MovieRequestDto.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new InvalidAuthenticationException("영화오류")))
                .bodyToMono(MovieResponseDto.class)
                .onErrorReturn(new MovieResponseDto("없는영화입니다"))
                .block();

        if (result.getMovieName() == null) {
            throw new InvalidAuthenticationException("삭제된 영화입니다");
        }

        return result.getMovieName();
    }
}
