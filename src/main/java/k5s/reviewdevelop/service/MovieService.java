package k5s.reviewdevelop.service;

import k5s.reviewdevelop.api.MovieAPI;
import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Long register(String name, Long id){

        Movie movie = Movie.createMovie(name, id);
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