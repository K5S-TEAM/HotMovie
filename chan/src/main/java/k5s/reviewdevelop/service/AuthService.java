package k5s.reviewdevelop.service;

import k5s.reviewdevelop.dto.AuthenticationRequestDto;
import k5s.reviewdevelop.dto.AuthenticationResponseDto;
import k5s.reviewdevelop.exception.InvalidAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final WebClient webClient;


    @Transactional
    public Long requestAuthentication(String accessToken) {
        AuthenticationRequestDto dto = new AuthenticationRequestDto(accessToken);

        AuthenticationResponseDto result = webClient.post()
                .uri("/auth/access-token-valid")
                .body(Mono.just(dto), AuthenticationRequestDto.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new InvalidAuthenticationException("인증 정보가 존재하지 않습니다.")))
                .bodyToMono(AuthenticationResponseDto.class)
                .block();

        if (result.getId() == null) {
            throw new InvalidAuthenticationException("인증 정보가 존재하지 않습니다.");
        }

        return result.getId();
    }


}
