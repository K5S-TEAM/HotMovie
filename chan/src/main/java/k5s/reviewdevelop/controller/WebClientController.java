package k5s.reviewdevelop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class WebClientController {

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

}