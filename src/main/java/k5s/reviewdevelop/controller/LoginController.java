package k5s.reviewdevelop.controller;

import k5s.reviewdevelop.api.AuthAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class LoginController {

    private final AuthAPI authAPI;

    @Value("${msa.member-login}")
    String loginURL;

    @GetMapping("/logout")
    public String logoutToLoginPage(@CookieValue(value = "accessToken", required = false) String accessToken, HttpServletRequest request) {
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
}
