package k5s.reviewdevelop.service;

import k5s.reviewdevelop.dto.AuthenticationResponseDto;
import k5s.reviewdevelop.exception.InvalidAuthenticationException;
import k5s.reviewdevelop.exception.NoLoginException;
import k5s.reviewdevelop.api.AuthAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
public class HeaderService {

    private final AuthAPI authAPI;

    public void loadHeader(String accessToken, Model model){
        try {
            AuthenticationResponseDto authenticationResponseDto = authAPI.requestAuthentication(accessToken);
            model.addAttribute("memberName", authenticationResponseDto.getName());
        } catch(InvalidAuthenticationException e) {
            throw new NoLoginException("비회원입니다", e);
        }
    }

}
