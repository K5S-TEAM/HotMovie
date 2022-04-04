package k5s.reviewdevelop.repository;

import k5s.reviewdevelop.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {

    private final EntityManager em;
    public void save(Review review) {
        em.persist(review);
    }
    public Review findOne(Long id) {
        return em.find(Review.class, id);
    }

}