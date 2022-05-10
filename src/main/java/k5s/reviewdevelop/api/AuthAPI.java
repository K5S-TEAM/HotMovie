package k5s.reviewdevelop.api;

import k5s.reviewdevelop.dto.AuthenticationRequestDto;
import k5s.reviewdevelop.dto.AuthenticationResponseDto;
import k5s.reviewdevelop.exception.InvalidAuthenticationException;
import k5s.reviewdevelop.service.WebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class AuthAPI {

    private final WebClientService webClientService;

    public AuthenticationResponseDto requestAuthentication(String accessToken) {

        if (accessToken == null)
        {
            throw new InvalidAuthenticationException("토큰 값이 없습니다");
        }

        AuthenticationRequestDto dto = new AuthenticationRequestDto(accessToken);

        AuthenticationResponseDto result = webClientService.setAuthWebClient().post()
                .uri("/auth/access-token-valid")
                .body(Mono.just(dto), AuthenticationRequestDto.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new InvalidAuthenticationException("인증 정보가 존재하지 않습니다.")))
                .onStatus(HttpStatus::is5xxServerError, error -> Mono.error(new InvalidAuthenticationException("인증 정보가 존재하지 않습니다.")))
                .bodyToMono(AuthenticationResponseDto.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(TimeoutException.class, ex -> new InvalidAuthenticationException("연결시간초과"))
                .block();

        return result;
    }

    public void requestLogout(String accessToken) {

        AuthenticationRequestDto dto = new AuthenticationRequestDto(accessToken);

        AuthenticationResponseDto result = webClientService.setAuthWebClient().post()
                .uri("/auth/logout")
                .body(Mono.just(dto), AuthenticationRequestDto.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new InvalidAuthenticationException("인증 정보가 존재하지 않습니다.")))
                .bodyToMono(AuthenticationResponseDto.class)
                .onErrorReturn(new AuthenticationResponseDto(-1L))
                .block();

    }



}
