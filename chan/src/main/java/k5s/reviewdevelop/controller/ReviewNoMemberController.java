package k5s.reviewdevelop.controller;

import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.dto.UpdateReviewDto;
import k5s.reviewdevelop.form.ReviewForm;
import k5s.reviewdevelop.repository.ReviewRepository;
import k5s.reviewdevelop.service.AuthService;
import k5s.reviewdevelop.service.MemberService;
import k5s.reviewdevelop.service.MovieService;
import k5s.reviewdevelop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/movies/{movieId}/reviews")
public class ReviewNoMemberController {

    private final ReviewService reviewService;
    private final MovieService movieService;
    private final MemberService memberService;
    private final ReviewRepository reviewRepository;
    private final AuthService authService;

    @GetMapping
    public String list(@CookieValue(value = "accessToken", required = false) String accessToken, @PathVariable("movieId") Long movieId, Model model) {

        Long memberId = authService.requestAuthentication(accessToken);
        if (memberId != null) {
            model.addAttribute("memberId", memberId);
        }
        String movieName = movieService.findMovieName(movieId);
        if (movieName == null)
        {
            return "movies/reviews/error";
        }

        List<Review> reviews = reviewService.findReviews(movieId);
        model.addAttribute("movieName", movieName);
        model.addAttribute("movieId", movieId);
        model.addAttribute("reviews", reviews);
        return "movies/reviews/reviewList";
    }

    @GetMapping("/new")
    public String write(@CookieValue(value = "accessToken", required = false) String accessToken, @PathVariable("movieId") Long movieId, Model model, ReviewForm form){

        Long memberId = authService.requestAuthentication(accessToken);
        if (memberId == -1L) {
            return "redirect:/movies/{movieId}/reviews/loginPage";
        }

        String movieName = movieService.findMovieName(movieId);
        model.addAttribute("movieName", movieName);
        //model.addAttribute("member", loginMember);
        return "movies/reviews/new";
    }

    @PostMapping("/new")
    public String register(@CookieValue(value = "accessToken", required = false) String accessToken,
                           @PathVariable("movieId") Long movieId, @Valid ReviewForm form, BindingResult bindingResult, Model model){
        String movieName = movieService.findMovieName(movieId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("movieName", movieName);
            return "movies/999/reviews/new";
        }
        Long memberId = authService.requestAuthentication(accessToken);
        if (memberId == -1) {
            return "redirect:/movies/{movieId}/reviews/loginPage";
        }
        reviewService.register(memberId, movieId, form.getDescription(), form.getScore());
        Movie movie = movieService.findOne(movieId);
        log.info("총 영화점수" + String.valueOf(movie.getSumScore()));
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

        //Movie movie = movieService.findOne(movieId);
        String movieName = movieService.findMovieName(movieId);
        //세션이 유지되면 로그인으로 이동
        model.addAttribute("movieName", movieName);
        return "movies/reviews/edit";
    }

    @PostMapping("/{reviewId}/edit")
    public String updateItem(@PathVariable Long movieId, @PathVariable Long reviewId, @Valid ReviewForm form, BindingResult bindingResult, Model model) {

        Movie movie = movieService.findOne(movieId);

        String movieName = movieService.findMovieName(movieId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("movieName", movieName);
            return "movies/reviews/edit";
        }

        reviewService.updateReview(new UpdateReviewDto(form));
        return "redirect:/movies/{movieId}/reviews";
    }
}