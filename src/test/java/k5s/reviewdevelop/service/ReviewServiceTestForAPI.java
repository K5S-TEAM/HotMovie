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
public class ReviewServiceTestForAPI {


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

    /*
    @Test
    public void 한개리뷰등록() throws Exception{

        //Given
        //Member member = createMember("emrhssla@naver.com", LocalDate.of(2019,11,12));
        //Long movieId = movieService.register("Inception", "Its a very Hot Movie. So, I recommended this Movie to you");
        Long movieId = 3L;
        int score = 2;

        //When
        Long reviewId = reviewService.register(1L, movieId, "재미없다", score);


        //Then
        Movie movie = movieRepository.findOne(movieId);
        Review getReview = reviewRepository.findOne(reviewId);
        assertEquals("멤버의 리뷰 점수는 2점과 같다", 2, getReview.getScore());
        assertEquals("리뷰는 한개여야한다.",1, movie.getReviews().size());
        assertEquals("리뷰의 점수 종합은 멤버1의 점수이어야한다.",2, movie.getSumScore(),0.0001);
        assertEquals("리뷰의 평균 점수는 총점/갯수이다.",2/1, movie.getAverageScore(),0.0001);

    }
     */



    private Member createMember(String email, LocalDate localDate) {
        Member member = new Member();
        member.setEmail(email);
        member.setBirthDate(localDate);
        em.persist(member);
        return member;
    }
}
