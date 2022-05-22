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

    /**
     * 회원 비회원 식별
     * 비회원일 경우 NoLoginExcetion
     * 회원일 경우 return memberName
     */
    public void loadHeader(String accessToken, Model model){
        try {
            /** 인증서버와 API 통신 **/
            AuthenticationResponseDto authenticationResponseDto = authAPI.requestAuthentication(accessToken);
            //성공시
            model.addAttribute("memberName", authenticationResponseDto.getName());
        } catch(InvalidAuthenticationException e) {
            //실패시
            throw new NoLoginException("비회원입니다", e);
        }
    }

}
