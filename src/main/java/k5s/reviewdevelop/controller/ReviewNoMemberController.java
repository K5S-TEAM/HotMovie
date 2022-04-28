package k5s.reviewdevelop.controller;

import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.dto.AuthenticationResponseDto;
import k5s.reviewdevelop.dto.UpdateReviewDto;
import k5s.reviewdevelop.exception.NoLoginException;
import k5s.reviewdevelop.exception.NoLoginForHeaderException;
import k5s.reviewdevelop.exception.NoLoginGoLoginException;
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
import java.util.ArrayList;
import java.util.HashMap;
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
    private final MovieAPI movieAPI;
    private final AuthService authService;
    private final LoginService loginService;

    @GetMapping
    public String list(@CookieValue(value = "accessToken", required = false) String accessToken, @PathVariable("movieId") Long movieId, Model model) {
        loginService.findMemberByAuth(accessToken, model);
        String movieName = movieService.findMovieName(movieId);
        if (movieName == null)
        {
            return "movies/reviews/error";
        }
        List<Review> reviews = reviewService.findReviews(movieId);
        HashMap<Long, String> nickNames = memberService.findNickNamesInHTML(reviews);
        model.addAttribute("nickNames", nickNames);
        model.addAttribute("movieName", movieName);
        model.addAttribute("movieId", movieId);
        model.addAttribute("reviews", reviews);
        return "movies/reviews/list";
    }

    @GetMapping("/new")
    public String write(@CookieValue(value = "accessToken", required = false) String accessToken, @PathVariable("movieId") Long movieId, Model model, ReviewForm form){
        loginService.findHeader(accessToken, model);
        String movieName = movieService.findMovieName(movieId);
        model.addAttribute("movieName", movieName);
        return "movies/reviews/new";
    }

    @PostMapping("/new")
    public String register(@CookieValue(value = "accessToken", required = false) String accessToken,
                           @PathVariable("movieId") Long movieId, @Valid ReviewForm form, BindingResult bindingResult, Model model){
        String movieName = movieService.findMovieName(movieId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("movieName", movieName);
            return "movies/reviews/new";
        }
        AuthenticationResponseDto authenticationResponseDto = authService.requestAuthentication(accessToken);
        reviewService.register(movieName, authenticationResponseDto.getId(), movieId, form.getDescription(), form.getScore());
        Movie movie = movieService.findOne(movieId);
        movieAPI.responseMovieAverageScore(movieId, movie.getAverageScore());
        return "redirect:/movies/{movieId}/reviews";
    }

    @PostMapping(value = "/{reviewId}/cancel")
    public String cancelOrder(@PathVariable("movieId") Long movieId, @PathVariable("reviewId") Long reviewId, HttpServletRequest request) {
        Movie movie = movieService.findOne(movieId);
        reviewService.deleteReview(reviewId);
        movieAPI.responseMovieAverageScore(movieId, movie.getAverageScore());
        String referer = request.getHeader("Referer");
        if (referer.contains("movies")){
            return "redirect:/movies/{movieId}/reviews";
        }
        else if(referer.contains("short")){
            return "redirect:/reviews/short-my";
        }
        else{
            return "redirect:/reviews/my";
        }
    }


    @GetMapping("/{reviewId}/edit")
    public String updateReview(@CookieValue(value = "accessToken", required = false) String accessToken, @PathVariable Long movieId, @PathVariable Long reviewId, ReviewForm form, Model model, HttpServletRequest request) {
        loginService.findHeader(accessToken, model);
        getEditForm(movieId, reviewId, form, model);
        return "movies/reviews/edit";
    }

    @GetMapping("/{reviewId}/edit/my")
    public String updateReviewByMember(@CookieValue(value = "accessToken", required = false) String accessToken, @PathVariable Long movieId, @PathVariable Long reviewId, ReviewForm form, Model model, HttpServletRequest request) {
        loginService.findHeader(accessToken, model);
        getEditForm(movieId, reviewId, form, model);
        return "member/reviews/edit";
    }

    @GetMapping("/{reviewId}/edit/short-my")
    public String updateReviewByMemberInOtherServer(@PathVariable Long movieId, @PathVariable Long reviewId, ReviewForm form, Model model, HttpServletRequest request) {
        getEditForm(movieId, reviewId, form, model);
        return "member/reviews/short-edit";
    }

    @PostMapping("/{reviewId}/edit")
    public String requestRegisterReview(@CookieValue(value = "accessToken", required = false) String accessToken,
                                        @PathVariable Long movieId,
                                        @PathVariable Long reviewId, @Valid ReviewForm form, BindingResult bindingResult,
                                        Model model) {
        loginService.findHeader(accessToken,model);
        Movie movie = movieService.findOne(movieId);
        String movieName = movieService.findMovieName(movieId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("movieName", movieName);
            return "movies/reviews/edit";
        }
        reviewService.updateReview(new UpdateReviewDto(form));
        movieAPI.responseMovieAverageScore(movieId, movie.getAverageScore());
        return "redirect:/movies/{movieId}/reviews";
    }

    @PostMapping("/{reviewId}/edit/short-my")
    public String requestRegisterReviewByMemberInOtherServer(@PathVariable Long movieId, @PathVariable Long reviewId, @Valid ReviewForm form, BindingResult bindingResult, Model model) {
        Movie movie = movieService.findOne(movieId);
        String movieName = movieService.findMovieName(movieId);
        model.addAttribute("movieId", movieId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("movieName", movieName);
            return "member/reviews/short-edit";
        }
        reviewService.updateReview(new UpdateReviewDto(form));
        movieAPI.responseMovieAverageScore(movieId, movie.getAverageScore());
        return "redirect:/reviews/short-my";
    }

    @PostMapping("/{reviewId}/edit/my")
    public String requestRegisterReviewByMember(@CookieValue(value = "accessToken", required = false) String accessToken,
                                                @PathVariable Long movieId, @PathVariable Long reviewId, @Valid ReviewForm form,
                                                BindingResult bindingResult, Model model) {

        loginService.findHeader(accessToken,model);
        Movie movie = movieService.findOne(movieId);
        String movieName = movieService.findMovieName(movieId);
        model.addAttribute("movieId", movieId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("movieName", movieName);
            return "member/reviews/edit";
        }
        reviewService.updateReview(new UpdateReviewDto(form));
        movieAPI.responseMovieAverageScore(movieId, movie.getAverageScore());
        return "redirect:/reviews/my";
    }

    private void getEditForm(Long movieId, Long reviewId, ReviewForm form, Model model) {
        Review review = reviewRepository.findOne(reviewId);
        form.setId(review.getId());
        form.setScore(0);
        form.setDescription(review.getDescription());
        String movieName = movieService.findMovieName(movieId);
        model.addAttribute("movieName", movieName);
    }

    @ExceptionHandler
    public String noLoginForHeaderExceptionHandler(NoLoginForHeaderException e) {
        return "redirect:/reviews/login";
    }


    @ExceptionHandler
    public String noLoginExceptionHandler(NoLoginException e) {
        return "redirect:/reviews/login";
    }

}