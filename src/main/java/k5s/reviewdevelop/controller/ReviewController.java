package k5s.reviewdevelop.controller;


import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.dto.UpdateReviewDto;
import k5s.reviewdevelop.exception.NoLoginException;
import k5s.reviewdevelop.exception.NoLoginForHeaderException;
import k5s.reviewdevelop.form.ReviewForm;
import k5s.reviewdevelop.repository.ReviewRepository;
import k5s.reviewdevelop.service.*;
import k5s.reviewdevelop.service.api.MovieAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews/{reviewId}")
public class ReviewController {

    private final ReviewService reviewService;
    private final MovieService movieService;
    private final ReviewRepository reviewRepository;
    private final MovieAPI movieAPI;
    private final LoginService loginService;

    @GetMapping(value = {"/edit", "/edit/my"})
    public String updateReview(@CookieValue(value = "accessToken", required = false) String accessToken, @PathVariable Long reviewId, ReviewForm form, Model model) {
        loginService.findHeader(accessToken, model);
        getEditForm(reviewId, form, model);
        return "movies/reviews/edit";
    }

    @GetMapping("/edit/short-my")
    public String updateReviewByMemberInOtherServer(@PathVariable Long reviewId, ReviewForm form, Model model) {
        getEditForm(reviewId, form, model);
        return "member/reviews/short-edit";
    }


    @PostMapping("/edit")
    public String requestRegisterReview(@CookieValue(value = "accessToken", required = false) String accessToken,
                                        @PathVariable Long reviewId, @Valid ReviewForm form, BindingResult bindingResult,
                                        Model model) {
        loginService.findHeader(accessToken,model);
        Long movieId = reviewService.findMovieId(reviewId);
        Movie movie = movieService.findOne(movieId);
        if (bindingResult.hasErrors()) {
            String movieName = movieService.findMovieName(movieId);
            model.addAttribute("movieName", movieName);
            return "movies/reviews/edit";
        }
        reviewService.updateReview(new UpdateReviewDto(form));
        movieAPI.responseMovieAverageScore(movieId, movie.getAverageScore());
        return "redirect:/movies/"+ movieId +"/reviews";
    }

    @PostMapping("/edit/my")
    public String requestRegisterReviewByMember(@CookieValue(value = "accessToken", required = false) String accessToken,
                                                @PathVariable Long reviewId, @Valid ReviewForm form,
                                                BindingResult bindingResult, Model model) {

        loginService.findHeader(accessToken,model);
        Long movieId = reviewService.findMovieId(reviewId);
        Movie movie = movieService.findOne(movieId);
        model.addAttribute("movieId", movieId);
        if (bindingResult.hasErrors()) {
            String movieName = movieService.findMovieName(movieId);
            model.addAttribute("movieName", movieName);
            return "movies/reviews/edit";
        }
        reviewService.updateReview(new UpdateReviewDto(form));
        movieAPI.responseMovieAverageScore(movieId, movie.getAverageScore());
        return "redirect:/reviews/my";
    }

    @PostMapping("/edit/short-my")
    public String requestRegisterReviewByMemberInOtherServer(@PathVariable Long reviewId, @Valid ReviewForm form, BindingResult bindingResult, Model model) {
        Long movieId = reviewService.findMovieId(reviewId);
        Movie movie = movieService.findOne(movieId);
        model.addAttribute("movieId", movieId);
        if (bindingResult.hasErrors()) {
            String movieName = movieService.findMovieName(movieId);
            model.addAttribute("movieName", movieName);
            return "member/reviews/short-edit";
        }
        reviewService.updateReview(new UpdateReviewDto(form));
        movieAPI.responseMovieAverageScore(movieId, movie.getAverageScore());
        return "redirect:/reviews/short-my";
    }

    @PostMapping(value = "/cancel")
    public String cancelOrder(@PathVariable("reviewId") Long reviewId, HttpServletRequest request) {
        Long movieId = reviewService.findMovieId(reviewId);
        Movie movie = movieService.findOne(movieId);
        reviewService.deleteReview(reviewId);
        movieAPI.responseMovieAverageScore(movieId, movie.getAverageScore());
        String referer = request.getHeader("Referer");
        if (referer.contains("movies")){
            return "redirect:/movies/"+ movieId + "/reviews";
        }
        else if(referer.contains("short")){
            return "redirect:/reviews/short-my";
        }
        else{
            return "redirect:/reviews/my";
        }
    }

    private void getEditForm(Long reviewId, ReviewForm form, Model model) {
        Review review = reviewRepository.findOne(reviewId);
        form.setId(review.getId());
        form.setScore(0);
        form.setDescription(review.getDescription());
        String movieName = movieService.findMovieName(review.getMovie().getId());
        model.addAttribute("movieName", movieName);
    }

    @ExceptionHandler
    public String noLoginForHeaderExceptionHandler(NoLoginForHeaderException e) {
        return "redirect:/reviews/login";
    }

}
