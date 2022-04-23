package k5s.reviewdevelop.service.api;

import k5s.reviewdevelop.dto.*;
import k5s.reviewdevelop.exception.InvalidAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberAPI {

    @Value("${msa.member}")
    String memberServerUrl;

    @Transactional
    public List<MemberIdNicknameDto> requestNicknames(List<Long> ids) {


        WebClient webClient = WebClient.builder().baseUrl(memberServerUrl).build();

        if (ids == null)
        {
            return null;
        }

        MemberNicknamesRequestDto dto = new MemberNicknamesRequestDto(ids);

        MemberNicknamesResponseDto result = webClient.post()
                .uri("/member/nicknames")
                .body(Mono.just(dto), MemberNicknamesRequestDto.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new InvalidAuthenticationException("HTTP 4XX 오류")))
                .bodyToMono(MemberNicknamesResponseDto.class)
                .onErrorReturn(null)
                .block();

        return result.getMemberNicknames();
    }

}
