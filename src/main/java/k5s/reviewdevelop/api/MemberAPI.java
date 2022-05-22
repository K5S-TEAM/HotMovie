package k5s.reviewdevelop.api;

import k5s.reviewdevelop.dto.*;
import k5s.reviewdevelop.exception.InvalidAuthenticationException;
import k5s.reviewdevelop.exception.NoNicknamesException;
import k5s.reviewdevelop.service.WebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberAPI {

    private final WebClientService webClientService;

    @Transactional
    public List<MemberIdNicknameDto> requestNicknames(List<Long> ids) {

        if (ids == null)
        {
            return null;
        }

        MemberNicknamesRequestDto dto = new MemberNicknamesRequestDto(ids);

        MemberNicknamesResponseDto result = webClientService.setMemberWebClient().post()
                .uri("/member/nicknames")
                .body(Mono.just(dto), MemberNicknamesRequestDto.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new NoNicknamesException("HTTP 4XX 오류")))
                .bodyToMono(MemberNicknamesResponseDto.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(ExceptionControl::ConnectionError, ex -> new NoNicknamesException("서버와 연결이 초과"))
                .block();

        return result.getMemberNicknames();
    }

}
