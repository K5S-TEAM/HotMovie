package k5s.reviewdevelop.service;

import k5s.reviewdevelop.service.api.MovieAPI;
import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieService {


    private final MovieAPI movieAPI;
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

    public Long register(Long id){

        Movie movie = Movie.createMovie(id);
        movieRepository.save(movie);

        return movie.getId();

    }


    /**
     * 영화 이름요청
     */
    public String findMovieName(Long movieId){
        return movieAPI.requestMovieName(movieId);
    }

}