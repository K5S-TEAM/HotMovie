package k5s.reviewdevelop.controller;

import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.exception.InvalidAuthenticationException;
import k5s.reviewdevelop.service.AuthService;
import k5s.reviewdevelop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class WebClientController {

    private final AuthService authService;

    @GetMapping("/test")
    public Mono<String> doTest() {
        WebClient client = WebClient.create();
        return client.get()
                .uri("http://localhost:8080/webclient/test-create")
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping("/webclienttest")
    public String testWebClient() {
        return "hi its Client`";
    }

    @GetMapping("/movies/webclient")
    public String myPage(@CookieValue(value = "accessToken", required = false) String accessToken, Model model) {
        if (accessToken == null) {
            throw new InvalidAuthenticationException("인증 정보가 존재하지 않습니다.");
        }

        Long aLong = authService.requestAuthentication(accessToken);
        return String.valueOf(aLong);
    }

}