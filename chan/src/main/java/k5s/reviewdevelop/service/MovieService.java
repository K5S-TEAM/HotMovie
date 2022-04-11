package k5s.reviewdevelop.service;

import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    @Transactional
    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public List<Movie> findMovies() {
        return movieRepository.findAll();
    }

    public Movie findOne(Long movieId) {
        return movieRepository.findOne(movieId);
    }

    public Movie findReviews(Long movieId) {
        return movieRepository.findReviews(movieId);
    }

    public Long register(String name, String description){

        Movie movie = Movie.createMovie(name, description);
        movieRepository.save(movie);

        return movie.getId();

    }

}