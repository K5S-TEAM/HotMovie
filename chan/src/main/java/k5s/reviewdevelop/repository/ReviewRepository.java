package k5s.reviewdevelop.repository;

import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

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

    //리뷰 삭제
    @Transactional
    public int delete(Long id) {
        String jpql = "delete from Review r where r.id = :id";
        Query query = em.createQuery(jpql).setParameter("id",id);
        int rows =query.executeUpdate();
        return rows;
    }


}