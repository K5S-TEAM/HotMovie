package k5s.reviewdevelop.controller;

import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SessionHomeController {

    private final MemberRepository memberRepository;

    @RequestMapping("/")
    public String home(){
        return "home";
    }

    @RequestMapping("/login")
    public String login(HttpServletResponse response, HttpServletRequest request){

        Long id = 1L;
        Member loginMember = memberRepository.findOne(id);
        HttpSession session = request.getSession(); //세션에 로그인 회원 정보 보관
        session.setAttribute("loginMemberTEST", loginMember);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        //세션을 삭제한다.
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }


//    @GetMapping("/")
    public String homeLoginV1(HttpServletRequest request, Model model) {

        //세션이 없으면 home
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }

        Member loginMember = (Member)session.getAttribute("loginMemberTEST");
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV2(@SessionAttribute(name = "loginMemberTEST", required = false) Member loginMember, Model model) {

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        return "loginPage";
    }
}
