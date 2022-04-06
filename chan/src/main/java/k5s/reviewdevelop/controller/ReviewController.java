package k5s.reviewdevelop.controller;


import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.repository.ReviewRepository;
import k5s.reviewdevelop.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/movies")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final MovieService movieService;

    @GetMapping("/{movieId}/reviews")
    public String list(@PathVariable("movieId") Long movieId,Model model) {

        Movie movie = movieService.findReviews(movieId);
        if (movie.getName() == "NoMovie"){
            return "movies/reviews/error";
        }
        model.addAttribute("movieName", movie.getName());
        model.addAttribute("reviews", movie.getReviews());
        return "movies/reviews/reviewList";
    }
}
