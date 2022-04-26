package k5s.reviewdevelop.controller;

import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.dto.AuthenticationResponseDto;
import k5s.reviewdevelop.service.AuthService;
import k5s.reviewdevelop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/reviews")
public class MemberController {

    private final AuthService authService;
    private final ReviewService reviewService;

    @GetMapping("/my")
    public String memberReviewList(@CookieValue(value = "accessToken", required = false) String accessToken, Model model) {

        AuthenticationResponseDto authenticationResponseDto = authService.requestAuthentication(accessToken);
        Long memberId = authenticationResponseDto.getId();
        if (memberId != null) {
            model.addAttribute("memberId", memberId);
        }
        else {
            System.out.println("잘못된페이지입니다");
        }

        List<Review> reviews = reviewService.findReviewsByMember(memberId);
        //model.addAttribute("movieName", movieName);
        //model.addAttribute("movieId", movieId);
        model.addAttribute("reviews", reviews);
        return "member/reviews/list";
    }

    @GetMapping("/short-my")
    public String memberShortReviewList(@CookieValue(value = "accessToken", required = false) String accessToken, Model model) {
        AuthenticationResponseDto authenticationResponseDto = authService.requestAuthentication(accessToken);
        Long memberId = authenticationResponseDto.getId();
        if (memberId != null) {
            model.addAttribute("memberId", memberId);
        }

        List<Review> reviews = reviewService.findReviewsByMember(memberId);
        //model.addAttribute("movieName", movieName);
        //model.addAttribute("movieId", movieId);
        model.addAttribute("reviews", reviews);
        return "member/reviews/shortList";
    }
}
