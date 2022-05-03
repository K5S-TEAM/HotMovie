package k5s.reviewdevelop.controller;

import k5s.reviewdevelop.service.AuthService;
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

    private final AuthService authService;

    @Value("${msa.member-login}")
    String loginURL;

    @GetMapping("/list/logout")
    public String logout(@CookieValue(value = "accessToken", required = false) String accessToken, HttpServletRequest request) {
        authService.requestLogout(accessToken);
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @GetMapping("/logout")
    public String logoutToLoginPage(@CookieValue(value = "accessToken", required = false) String accessToken) {
        authService.requestLogout(accessToken);
        return "redirect:"+loginURL;
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:"+loginURL;
    }
}
