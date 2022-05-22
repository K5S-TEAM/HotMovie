package k5s.reviewdevelop.service;

import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.repository.MovieRepository;
import k5s.reviewdevelop.repository.ReviewRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MovieTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MovieService movieService;

    @Autowired
    MovieRepository movieRepository;

    @Test
    public void 영화점수로직(){
        //Given
        Member member = createMember("emrhssla@naver.com", LocalDate.of(2019,11,12));
        Long movieId = movieService.register("Inception", 10L);
        Member member2 = createMember("emrhssla@gmail.com", LocalDate.of(2019,11,12));
        int score = 2;
        int score2 = 9;

        //When
        Long reviewId = reviewService.register("Inception", member.getId(), movieId, "이게 영화냐",score);
        Long reviewId2 = reviewService.register("Inception", member2.getId(), movieId, "너무재밌어요", score2);

        //Then
        Review getReview = reviewRepository.findOne(reviewId);
        Review getReview2 = reviewRepository.findOne(reviewId2);
        Movie movie = movieRepository.findOne(movieId);
        assertEquals("멤버2의 리뷰 점수는 9점과 같다", 9, getReview2.getScore());
        assertEquals("리뷰가 늘어나야한다.",2, movie.getReviews().size());
        assertEquals("리뷰의 점수 종합은 각 멤버 점수의 합이어야한다.",2 + 9, movie.getSumScore(),0.0001);
        assertEquals("리뷰의 평균 점수는 총점/갯수이다.",11.0/2, movie.getAverageScore(),0.0001);
    }
    private Member createMember(String email, LocalDate localDate) {
        Member member = new Member();
        member.setEmail(email);
        member.setBirthDate(localDate);
        em.persist(member);
        return member;
    }

}
