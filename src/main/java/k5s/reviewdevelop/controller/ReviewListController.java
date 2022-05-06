package k5s.reviewdevelop.controller;

import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.dto.AuthenticationResponseDto;
import k5s.reviewdevelop.dto.MemberDto;
import k5s.reviewdevelop.exception.NoLoginForHeaderException;
import k5s.reviewdevelop.exception.NoMovieException;
import k5s.reviewdevelop.form.ReviewForm;
import k5s.reviewdevelop.repository.ReviewRepository;
import k5s.reviewdevelop.service.*;
import k5s.reviewdevelop.api.AuthAPI;
import k5s.reviewdevelop.api.MovieAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/movies/{movieId}/reviews")
public class ReviewListController {

    private final ReviewService reviewService;
    private final MovieService movieService;
    private final MemberService memberService;
    private final MovieAPI movieAPI;
    private final AuthAPI authAPI;
    private final HeaderService headerService;

    @GetMapping
    public String list(@CookieValue(value = "accessToken", required = false) String accessToken, @PathVariable("movieId") Long movieId, Model model) {
        MemberDto member = memberService.findMember(accessToken);
        String movieName = movieService.findMovieName(movieId);
        List<Review> reviews = reviewService.findReviews(movieId);
        HashMap<Long, String> nickNames = memberService.findNickNamesInHTML(reviews);
        model.addAttribute("memberName", member.getName());
        model.addAttribute("memberId", member.getId());
        model.addAttribute("nickNames", nickNames);
        model.addAttribute("movieName", movieName);
        model.addAttribute("movieId", movieId);
        model.addAttribute("reviews", reviews);
        return "movies/reviews/list";
    }

    @GetMapping("/new")
    public String write(@CookieValue(value = "accessToken", required = false) String accessToken, @PathVariable("movieId") Long movieId, Model model, ReviewForm form){
        headerService.loadHeader(accessToken, model);
        String movieName = movieService.findMovieName(movieId);
        model.addAttribute("movieName", movieName);
        return "movies/reviews/new";
    }

    @PostMapping("/new")
    public String register(@CookieValue(value = "accessToken", required = false) String accessToken,
                           @PathVariable("movieId") Long movieId, @Valid ReviewForm form, BindingResult bindingResult, Model model){
        Movie movie = movieService.findOne(movieId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("movieName", movie.getName());
            return "movies/reviews/new";
        }
        AuthenticationResponseDto authenticationResponseDto = authAPI.requestAuthentication(accessToken);
        reviewService.register(movie.getName(), authenticationResponseDto.getId(), movieId, form.getDescription(), form.getScore());
        movieAPI.sendMovieAverageScore(movieId, movie.getAverageScore());
        return "redirect:/movies/{movieId}/reviews";
    }


    @ExceptionHandler
    public String noLoginForHeaderExceptionHandler(NoLoginForHeaderException e) {
        return "redirect:/reviews/login";
    }


    @ExceptionHandler
    public String noMovieExceptionHandler(NoMovieException e) {
        return "movies/reviews/error";
    }
}
