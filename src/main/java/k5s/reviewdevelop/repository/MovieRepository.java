package k5s.reviewdevelop.repository;

import k5s.reviewdevelop.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MovieRepository {
    private final EntityManager em;
    public void save(Movie movie) {
        em.persist(movie);
    }
    public Movie findOne(Long id) {
        return em.find(Movie.class, id);
    }
    public List<Movie> findAll() {
        return em.createQuery("select m from Movie m", Movie.class).getResultList();
    }
    public Movie findReviews(Long id){
        return null;
    }
}