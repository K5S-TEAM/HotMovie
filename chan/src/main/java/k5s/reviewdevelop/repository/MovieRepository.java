package k5s.reviewdevelop.repository;

import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
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
        if (movie.getId() == null) {
            em.persist(movie);
        } else {
            em.merge(movie);
        }
    }
    public Movie findOne(Long id) {
        return em.find(Movie.class, id);
    }
    public List<Movie> findAll() {
        return em.createQuery("select m from Movie m", Movie.class).getResultList();
    }
    public Movie findReviews(Long id){
        TypedQuery<Movie> movieTypedQuery = em.createQuery("select distinct m from Movie m join fetch m.reviews where m.id = :movieId", Movie.class).setParameter("movieId", id);
        try{
            movieTypedQuery.getSingleResult();
        } catch(NoResultException e){
            Movie movie = new Movie();
            movie.setName("NoMovie");
            movie.setDescription("NoMovie");
            em.persist(movie);
            return movie;
        }
        return movieTypedQuery.getSingleResult();
    }
}