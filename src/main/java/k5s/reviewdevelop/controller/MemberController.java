package k5s.reviewdevelop.controller;

import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.exception.NoLoginException;
import k5s.reviewdevelop.service.HeaderService;
import k5s.reviewdevelop.service.MemberService;
import k5s.reviewdevelop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/reviews")
public class MemberController {

    private final ReviewService reviewService;
    private final HeaderService headerService;
    private final MemberService memberService;

    @GetMapping("/my")
    public String memberReviewList(@CookieValue(value = "accessToken", required = false) String accessToken, Model model) {
        Long memberId = memberService.findMemberId(accessToken, model);
        List<Review> reviews = reviewService.findReviewsByMember(memberId);
        model.addAttribute("reviews", reviews);
        return "member/reviews/list";
    }

    @GetMapping("/short-my")
    public String memberShortReviewList(@CookieValue(value = "accessToken", required = false) String accessToken, Model model) {
        Long memberId = memberService.findMemberId(accessToken, model);
        List<Review> reviews = reviewService.findReviewsByMember(memberId);
        model.addAttribute("reviews", reviews);
        return "member/reviews/shortList";
    }

    @ExceptionHandler
    public String noLoginForHeaderExceptionHandler(NoLoginException e) {
        return "redirect:/reviews/login";
    }
}
