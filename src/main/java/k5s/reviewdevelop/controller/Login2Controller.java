package k5s.reviewdevelop.controller;

import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.exception.InvalidAuthenticationException;
import k5s.reviewdevelop.service.AuthService;
import k5s.reviewdevelop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
@RequiredArgsConstructor
public class Login2Controller {

    private final MemberService memberService;
    private final AuthService authService;

    @Value("${msa.member-login}")
    String loginURL;

    //@RequestMapping("/")
    public String home(){
        return "home";
    }

    @RequestMapping("/login")
    public String login(HttpServletResponse response){
        Cookie idCookie = new Cookie("accessToken", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY1MDQyMDgzNCwiZXhwIjoxNjUwNDI0NDM0fQ.CJX4rrjzkurs5zlsqUU7jN1nEA-_q1c4oHEAD8PfPH4");
        response.addCookie(idCookie);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(@CookieValue(value = "accessToken", required = false) String accessToken, HttpServletResponse response) {
        if (accessToken != null)
        {
            //authService.logout(accessToken);
        }
        return "redirect:/";
    }

    //@GetMapping("/")
    public String homeLoginV2(@CookieValue(value = "accessToken", required = false) String accessToken, Model model) {

        Member loginMember = memberService.findMember(accessToken);

        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/movies/{movieId}/reviews/loginPage")
    public String loginPage() {
        return "redirect:"+loginURL;
    }
}
