package k5s.reviewdevelop.controller;


import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.dto.MemberIdNicknameDto;
import k5s.reviewdevelop.form.ReviewForm;
import k5s.reviewdevelop.repository.ReviewRepository;
import k5s.reviewdevelop.service.MemberService;
import k5s.reviewdevelop.service.MovieService;
import k5s.reviewdevelop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/moviess/{memberId}/reviews")
public class TestController {

    private final ReviewService reviewService;
    private final MovieService movieService;
    private final MemberService memberService;


    @GetMapping
    public String memberReviewList(@PathVariable("memberId") Long memberId, Model model) {
        if (memberId != null) {
            model.addAttribute("memberId", memberId);
        }

        List<Review> reviews = reviewService.findReviewsByMember(memberId);
        //model.addAttribute("movieName", movieName);
        //model.addAttribute("movieId", movieId);
        model.addAttribute("reviews", reviews);
        return "member/reviews/list";
    }


}
