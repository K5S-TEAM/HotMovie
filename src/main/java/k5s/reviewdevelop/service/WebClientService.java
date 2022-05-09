package k5s.reviewdevelop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebClientService {

    @Value("${msa.auth}")
    String authServerUrl;

    @Value("${msa.movie}")
    String movieServerUrl;

    @Value("${msa.member}")
    String memberServerUrl;

    public WebClient setAuthWebClient(){
        WebClient webClient = WebClient.builder().baseUrl(authServerUrl).build();
        return webClient;
    }

    public WebClient setMovieWebClient(){
        WebClient webClient = WebClient.builder().baseUrl(movieServerUrl).build();
        return webClient;
    }

    public WebClient setMemberWebClient(){
        WebClient webClient = WebClient.builder().baseUrl(memberServerUrl).build();
        return webClient;
    }

}
