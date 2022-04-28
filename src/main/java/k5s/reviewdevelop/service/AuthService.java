package k5s.reviewdevelop.service;

import k5s.reviewdevelop.dto.AuthenticationRequestDto;
import k5s.reviewdevelop.dto.AuthenticationResponseDto;
import k5s.reviewdevelop.dto.MovieRequestDto;
import k5s.reviewdevelop.dto.MovieResponseDto;
import k5s.reviewdevelop.exception.InvalidAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${msa.auth}")
    String authServerUrl;


    public AuthenticationResponseDto requestAuthentication(String accessToken) {

        WebClient webClient = WebClient.builder().baseUrl(authServerUrl).build();

        if (accessToken == null)
        {
            AuthenticationResponseDto errorDto = new AuthenticationResponseDto(-1L, "오류");
            return errorDto;
        }

        AuthenticationRequestDto dto = new AuthenticationRequestDto(accessToken);

        AuthenticationResponseDto result = webClient.post()
                .uri("/auth/access-token-valid")
                .body(Mono.just(dto), AuthenticationRequestDto.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new InvalidAuthenticationException("인증 정보가 존재하지 않습니다.")))
                .bodyToMono(AuthenticationResponseDto.class)
                .block();



        return result;
    }

    public void requestLogout(String accessToken) {

        WebClient webClient = WebClient.builder().baseUrl(authServerUrl).build();
        AuthenticationRequestDto dto = new AuthenticationRequestDto(accessToken);

        AuthenticationResponseDto result = webClient.post()
                .uri("/auth/logout")
                .body(Mono.just(dto), AuthenticationRequestDto.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new InvalidAuthenticationException("인증 정보가 존재하지 않습니다.")))
                .bodyToMono(AuthenticationResponseDto.class)
                .onErrorReturn(new AuthenticationResponseDto(-1L))
                .block();

    }



}
