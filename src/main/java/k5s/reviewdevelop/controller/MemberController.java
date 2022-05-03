package k5s.reviewdevelop.controller;

import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.exception.NoLoginForHeaderException;
import k5s.reviewdevelop.service.LoginService;
import k5s.reviewdevelop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/reviews")
public class MemberController {

    private final ReviewService reviewService;
    private final LoginService loginService;

    @GetMapping("/my")
    public String memberReviewList(@CookieValue(value = "accessToken", required = false) String accessToken, Model model) {
        Long memberId = loginService.loadHeader(accessToken, model);
        List<Review> reviews = reviewService.findReviewsByMember(memberId);
        model.addAttribute("reviews", reviews);
        return "member/reviews/list";
    }

    @GetMapping("/short-my")
    public String memberShortReviewList(@CookieValue(value = "accessToken", required = false) String accessToken, Model model) {
        Long memberId = loginService.loadHeader(accessToken, model);
        List<Review> reviews = reviewService.findReviewsByMember(memberId);
        model.addAttribute("reviews", reviews);
        return "member/reviews/shortList";
    }

    @ExceptionHandler
    public String noLoginForHeaderExceptionHandler(NoLoginForHeaderException e) {
        return "redirect:/reviews/login";
    }
}
