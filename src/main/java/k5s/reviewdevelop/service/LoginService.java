package k5s.reviewdevelop.service;

import k5s.reviewdevelop.dto.AuthenticationResponseDto;
import k5s.reviewdevelop.exception.InvalidAuthenticationException;
import k5s.reviewdevelop.exception.NoLoginForHeaderException;
import k5s.reviewdevelop.exception.NoLoginGoLoginException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthService authService;


    public void findHeader(String accessToken, Model model){
        try {
            AuthenticationResponseDto authenticationResponseDto = authService.requestAuthentication(accessToken);
            model.addAttribute("memberName", authenticationResponseDto.getName());
        } catch(InvalidAuthenticationException e) {
            throw new NoLoginForHeaderException("비회원입니다");
        }
    }



    public void findMemberByAuth(String accessToken, Model model) {
        try {
            AuthenticationResponseDto authenticationResponseDto = authService.requestAuthentication(accessToken);
            model.addAttribute("memberId", authenticationResponseDto.getId());
            model.addAttribute("memberName", authenticationResponseDto.getName());
        } catch (InvalidAuthenticationException e) {

        }
    }


    public void findMemberByAuthForLogin(String accessToken, Model model){
        try {
            AuthenticationResponseDto authenticationResponseDto = authService.requestAuthentication(accessToken);
            model.addAttribute("memberId", authenticationResponseDto.getId());
            model.addAttribute("memberName", authenticationResponseDto.getName());
        } catch (InvalidAuthenticationException e) {
            throw new NoLoginGoLoginException("비회원입니다");
        }
    }


}
