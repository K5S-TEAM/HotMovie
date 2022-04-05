package k5s.reviewdevelop.service;

import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
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
import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ReviewServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewRepository reviewRepository;


    @Test
    public void 한개리뷰등록() throws Exception{

        //Given
        Member member = createMember("emrhssla@naver.com", LocalDate.of(2019,11,12));
        Movie movie = createMovie("Inception", "Its a very Hot Movie. So, I recommended this Movie to you");
        int score = 2;

        //When
        Long reviewId = reviewService.register(member.getId(), movie.getId(), "재미없다", score);


        //Then
        Review getReview = reviewRepository.findOne(reviewId);
        assertEquals("멤버의 리뷰 점수는 2점과 같다", 2, getReview.getScore());
        assertEquals("리뷰는 한개여야한다.",1, movie.getReviews().size());
        assertEquals("리뷰의 점수 종합은 멤버1의 점수이어야한다.",2, movie.getSumScore(),0.0001);
        assertEquals("리뷰의 평균 점수는 총점/갯수이다.",2/1, movie.getAverageScore(),0.0001);

    }

    @Test
    public void 여러리뷰등록() throws Exception{

        //Given
        Member member = createMember("emrhssla@naver.com", LocalDate.of(2019,11,12));
        Movie movie = createMovie("Inception", "Its a very Hot Movie. So, I recommended this Movie to you");
        Member member2 = createMember("emrhssla@gmail.com", LocalDate.of(2019,11,12));
        int score = 2;
        int score2 = 9;

        //When
        Long reviewId = reviewService.register(member.getId(), movie.getId(), "이게 영화냐",score);
        Long reviewId2 = reviewService.register(member2.getId(), movie.getId(), "너무 재밌다",score2);

        //Then
        Review getReview = reviewRepository.findOne(reviewId);
        Review getReview2 = reviewRepository.findOne(reviewId2);
        assertEquals("멤버2의 리뷰 점수는 9점과 같다", 9, getReview2.getScore());
        assertEquals("리뷰가 늘어나야한다.",2, movie.getReviews().size());
        assertEquals("리뷰의 점수 종합은 각 멤버 점수의 합이어야한다.",2 + 9, movie.getSumScore(),0.0001);
        assertEquals("리뷰의 평균 점수는 총점/갯수이다.",11.0/2, movie.getAverageScore(),0.0001);

    }

    @Test
    public void 리뷰삭제() {
        //Given
        Member member = createMember("emrhssla@naver.com", LocalDate.of(2019,11,12));
        Movie movie = createMovie("Inception", "Its a very Hot Movie. So, I recommended this Movie to you");
        int score = 8;
        int score2 = 4;
        Long reviewId = reviewService.register(member.getId(), movie.getId(), "아주 재밌어요",score);
        Long reviewId2 = reviewService.register(member.getId(), movie.getId(), "재미 없다",score2);

        //When
        reviewService.deleteReview(reviewId2);

        //Then
        Review getReview = reviewRepository.findOne(reviewId);
        assertEquals("영화에 등록된 리뷰의 갯수는 줄어야한다",2-1, movie.getNum());
        assertEquals("리뷰의 점수 종합은 삭제된 리뷰의 점수가 빠져야한다",12-4, movie.getSumScore(),0.0001);
        assertEquals("리뷰의 평균 점수는 총점/갯수이다",8/1, movie.getAverageScore(),0.0001);

        reviewService.deleteReview(reviewId);
        assertEquals("영화에 등록된 리뷰의 갯수는 줄어야한다",1-1, movie.getNum());
        assertEquals("리뷰의 점수 종합은 삭제된 리뷰의 점수가 빠져야한다",8-8, movie.getSumScore(),0.0001);
        assertEquals("리뷰 갯수가 0개일 경우 평균 점수는 0이어야한다",0, movie.getAverageScore(),0.0001);
    }

    private Member createMember(String email, LocalDate localDate) {
        Member member = new Member();
        member.setEmail(email);
        member.setBirthDate(localDate);
        em.persist(member);
        return member;
    }

    private Movie createMovie(String name, String description){
        Movie movie = new Movie();
        movie.setName(name);
        movie.setDescription(description);
        em.persist(movie);
        return movie;
    }

}