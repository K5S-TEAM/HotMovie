package k5s.reviewdevelop.controller;

import k5s.reviewdevelop.api.AuthAPI;
import k5s.reviewdevelop.exception.InvalidAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class LoginController {

    private final AuthAPI authAPI;

    @Value("${msa.member-login}")
    String loginURL;

    @GetMapping("/logout")
    public String logoutToLoginPage(@CookieValue(value = "accessToken", required = false) String accessToken, HttpServletRequest request,
                                    HttpServletResponse response) {
        authAPI.requestLogout(accessToken);
        String referer = request.getHeader("Referer");
        if(referer.matches(".+" + "/movies/"+"[0-9]+" + "/reviews")){
            return "redirect:"+ referer;
        }
        return "redirect:"+loginURL;
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:"+loginURL;
    }

    //인증서버에게 로그아웃을 요청했지만 인증서버와 연결이 끊키거나 redis중단됐을경우
    @ExceptionHandler
    public String invalidAuthenticationExceptionHandler(InvalidAuthenticationException e,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {
        Cookie myCookie = new Cookie("accessToken", null);
        myCookie.setMaxAge(0);
        myCookie.setPath("/");
        response.addCookie(myCookie);
        String referer = request.getHeader("Referer");
        if(referer.matches(".+" + "/movies/"+"[0-9]+" + "/reviews")){
            return "redirect:"+ referer;
        }
        return "redirect:"+loginURL;
    }
}
