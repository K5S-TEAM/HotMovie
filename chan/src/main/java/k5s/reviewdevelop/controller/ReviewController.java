package k5s.reviewdevelop.controller;


import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.domain.UpdateReviewDto;
import k5s.reviewdevelop.form.ReviewForm;
import k5s.reviewdevelop.repository.ReviewRepository;
import k5s.reviewdevelop.service.MemberService;
import k5s.reviewdevelop.service.MovieService;
import k5s.reviewdevelop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/movies/{movieId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final MovieService movieService;
    private final MemberService memberService;
    private final ReviewRepository reviewRepository;

    @GetMapping
    public String list(@SessionAttribute(name = "loginMemberTEST", required = false) Member member, @PathVariable("movieId") Long movieId,Model model) {

        Movie movie = movieService.findReviews(movieId);
        if (movie.getName() == "NoMovie"){
            return "movies/reviews/error";
        }
        log.info(String.valueOf(member));
        if (member != null) {
            model.addAttribute("memberId", member.getId());
        }
        model.addAttribute("movieName", movie.getName());
        model.addAttribute("movieId", movie.getId());
        model.addAttribute("reviews", movie.getReviews());
        return "movies/reviews/reviewList";
    }

    @GetMapping("/new")
    public String write(@SessionAttribute(name = "loginMemberTEST", required = false) Member loginMember, @PathVariable("movieId") Long movieId, Model model, ReviewForm form){

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "redirect:/loginPage";
        }
        Movie movie = movieService.findOne(movieId);
        //세션이 유지되면 로그인으로 이동
        model.addAttribute("movieName", movie.getName());
        model.addAttribute("member", loginMember);
        return "movies/reviews/new";
    }

    @PostMapping("/new")
    public String register(@SessionAttribute(name = "loginMemberTEST", required = false) Member loginMember,
                           @PathVariable("movieId") Long movieId, @Valid ReviewForm form, BindingResult bindingResult, Model model){
        Movie movie = movieService.findOne(movieId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("movieName", movie.getName());
            return "movies/reviews/new";
        }
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "redirect:/loginPage";
        }
        reviewService.register(loginMember.getId(), movieId, form.getDescription(), form.getScore());
        return "redirect:/movies/{movieId}/reviews";
    }

    @PostMapping(value = "/{reviewId}/cancel")
    public String cancelOrder(@PathVariable("movieId") Long movieId, @PathVariable("reviewId") Long reviewId) {
        Movie movie = movieService.findOne(movieId);
        log.info("총 영화점수" + String.valueOf(movie.getSumScore()));
        reviewService.deleteReview(reviewId);
        log.info("총 영화점수" + String.valueOf(movie.getSumScore()));
        return "redirect:/movies/{movieId}/reviews";
    }


    @GetMapping("/{reviewId}/edit")
    public String updateReview(@PathVariable Long movieId, @PathVariable Long reviewId, ReviewForm form, Model model) {
        Review review = reviewRepository.findOne(reviewId);
        form.setId(review.getId());
        form.setScore(review.getScore());
        form.setDescription(review.getDescription());

        Movie movie = movieService.findOne(movieId);
        //세션이 유지되면 로그인으로 이동
        model.addAttribute("movieName", movie.getName());
        return "movies/reviews/edit";
    }

    @PostMapping("/{reviewId}/edit")
    public String updateItem(@PathVariable Long movieId, @PathVariable Long reviewId, @Valid ReviewForm form, BindingResult bindingResult, Model model) {

        Movie movie = movieService.findOne(movieId);

        if (bindingResult.hasErrors()) {
            model.addAttribute("movieName", movie.getName());
            return "movies/reviews/edit";
        }

        reviewService.updateReview(new UpdateReviewDto(form));
        return "redirect:/movies/{movieId}/reviews";
    }
}
