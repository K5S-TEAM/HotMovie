package k5s.reviewdevelop.controller;


import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.dto.UpdateReviewDto;
import k5s.reviewdevelop.exception.NoLoginException;
import k5s.reviewdevelop.form.ReviewForm;
import k5s.reviewdevelop.repository.ReviewRepository;
import k5s.reviewdevelop.service.*;
import k5s.reviewdevelop.api.MovieAPI;
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
    private final HeaderService headerService;

    @GetMapping("/edit")
    public String updateReview(@CookieValue(value = "accessToken", required = false) String accessToken, @PathVariable Long reviewId, ReviewForm form, Model model) {
        headerService.loadHeader(accessToken, model);
        getEditForm(reviewId, form, model);
        return "movies/reviews/edit";
    }

    @PostMapping("/edit")
    public String requestRegisterReview(@CookieValue(value = "accessToken", required = false) String accessToken,
                                        @PathVariable Long reviewId, @Valid ReviewForm form, BindingResult bindingResult,
                                        Model model) {
        headerService.loadHeader(accessToken,model);
        Movie movie = reviewService.findMovie(reviewId);
        model.addAttribute("movieId", movie.getId());
        if (bindingResult.hasErrors()) {
            String movieName = movieService.
                    findMovieName(movie.getId());
            model.addAttribute("movieName", movieName);
            return "movies/reviews/edit";
        }
        reviewService.updateReview(new UpdateReviewDto(form));
        movieAPI.sendMovieAverageScore(movie.getId(), movie.getAverageScore());
        if(form.getReferer().contains("short")){
            return "redirect:/reviews/short-my";
        }else if(form.getReferer().contains("my")){
            return "redirect:/reviews/my";
        }else{
            return "redirect:/movies/"+ movie.getId() +"/reviews";
        }
    }


    @PostMapping("/cancel")
    public String cancelOrder(@PathVariable("reviewId") Long reviewId, HttpServletRequest request) {
        Movie movie = reviewService.findMovie(reviewId);
        reviewService.deleteReview(reviewId);
        movieAPI.sendMovieAverageScore(movie.getId(), movie.getAverageScore());
        String referer = request.getHeader("Referer");
        if (referer.contains("movies")){
            return "redirect:/movies/"+ movie.getId() + "/reviews";
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
    public String noLoginExceptionHandler(NoLoginException e) {
        return "redirect:/reviews/login";
    }

}
