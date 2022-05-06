package k5s.reviewdevelop.service;

import k5s.reviewdevelop.dto.AuthenticationResponseDto;
import k5s.reviewdevelop.exception.InvalidAuthenticationException;
import k5s.reviewdevelop.exception.NoLoginForHeaderException;
import k5s.reviewdevelop.api.AuthAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
public class HeaderService {

    private final AuthAPI authAPI;

    public Long loadHeader(String accessToken, Model model){
        try {
            AuthenticationResponseDto authenticationResponseDto = authAPI.requestAuthentication(accessToken);
            model.addAttribute("memberName", authenticationResponseDto.getName());
            return authenticationResponseDto.getId();
        } catch(InvalidAuthenticationException e) {
            throw new NoLoginForHeaderException("비회원입니다");
        }
    }

}
