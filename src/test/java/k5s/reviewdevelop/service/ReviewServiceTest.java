package k5s.reviewdevelop.service;

import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.dto.UpdateReviewDto;
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
import java.util.List;

import static org.junit.Assert.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
public class ReviewServiceTest {

    /*
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
    public void 리뷰없는영화(){
        Long movieId = movieService.register("Inception", "Its a very Hot Movie. So, I recommended this Movie to you");
        //String name = movieService.findReviews(movieId).getName();
        //assertEquals("영화의 리뷰가 없어도 영화 fetch join 조회는 가능해야한다", "Inception", name);
    }

    @Test
    public void 한개리뷰등록() throws Exception{

        //Given
        Member member = createMember("emrhssla@naver.com", LocalDate.of(2019,11,12));
        Long movieId = movieService.register("Inception", "Its a very Hot Movie. So, I recommended this Movie to you");
        int score = 2;

        //When
        Long reviewId = reviewService.register(member.getId(), movieId, "재미없다", score);


        //Then
        Movie movie = movieRepository.findOne(movieId);
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
        Long movieId = movieService.register("Inception", "Its a very Hot Movie. So, I recommended this Movie to you");
        Member member2 = createMember("emrhssla@gmail.com", LocalDate.of(2019,11,12));
        int score = 2;
        int score2 = 9;

        //When
        Long reviewId = reviewService.register(member.getId(), movieId, "이게 영화냐",score);
        Long reviewId2 = reviewService.register(member2.getId(), movieId, "너무 재밌다",score2);

        //Then
        Review getReview = reviewRepository.findOne(reviewId);
        Review getReview2 = reviewRepository.findOne(reviewId2);
        Movie movie = movieRepository.findOne(movieId);
        assertEquals("멤버2의 리뷰 점수는 9점과 같다", 9, getReview2.getScore());
        assertEquals("리뷰가 늘어나야한다.",2, movie.getReviews().size());
        assertEquals("리뷰의 점수 종합은 각 멤버 점수의 합이어야한다.",2 + 9, movie.getSumScore(),0.0001);
        assertEquals("리뷰의 평균 점수는 총점/갯수이다.",11.0/2, movie.getAverageScore(),0.0001);

    }

    @Test
    public void 리뷰점수삭제() {
        //Given
        Member member = createMember("emrhssla@naver.com", LocalDate.of(2019,11,12));
        Long movieId = movieService.register("Inception", "Its a very Hot Movie. So, I recommended this Movie to you");
        int score = 8;
        int score2 = 4;
        Long reviewId = reviewService.register(member.getId(), movieId, "아주 재밌어요",score);
        Long reviewId2 = reviewService.register(member.getId(), movieId, "재미 없다",score2);

        //When
        reviewService.deleteReview(reviewId2);

        //Then
        Review getReview = reviewRepository.findOne(reviewId);
        Movie movie = movieRepository.findOne(movieId);
        assertEquals("영화에 등록된 리뷰의 갯수는 줄어야한다",2-1, movie.getNum());
        assertEquals("리뷰의 점수 종합은 삭제된 리뷰의 점수가 빠져야한다",12-4, movie.getSumScore(),0.0001);
        assertEquals("리뷰의 평균 점수는 총점/갯수이다",8/1, movie.getAverageScore(),0.0001);

        reviewService.deleteReview(reviewId);
        assertEquals("영화에 등록된 리뷰의 갯수는 줄어야한다",1-1, movie.getNum());
        assertEquals("리뷰의 점수 종합은 삭제된 리뷰의 점수가 빠져야한다",8-8, movie.getSumScore(),0.0001);
        assertEquals("리뷰 갯수가 0개일 경우 평균 점수는 0이어야한다",0, movie.getAverageScore(),0.0001);
    }

    @Test(expected = NullPointerException.class)
    public void 리뷰삭제() throws Exception{
        //Given
        Member member = createMember("emrhssla@naver.com", LocalDate.of(2019,11,12));
        Long movieId = movieService.register("Inception", "Its a very Hot Movie. So, I recommended this Movie to you");
        int score = 8;
        int score2 = 4;
        Long reviewId = reviewService.register(member.getId(), movieId, "아주 재밌어요",score);
        Long reviewId2 = reviewService.register(member.getId(), movieId, "재미 없다",score2);

        //When
        reviewService.deleteReview(reviewId2);
        em.clear(); //영속성컨텍스트를 비운다

        //Then
        String description = reviewRepository.findOne(reviewId2).getDescription();
        fail("삭제한 리뷰를 조회할시 NULL 값이어야한다");
    }

    @Test
    public void 리뷰삭제후_영화상태() throws Exception{
        //Given
        Member member = createMember("emrhssla@naver.com", LocalDate.of(2019,11,12));
        Long movieId = movieService.register("Inception", "Its a very Hot Movie. So, I recommended this Movie to you");
        int score = 8;
        int score2 = 4;
        Long reviewId = reviewService.register(member.getId(), movieId, "아주 재밌어요",score);
        Long reviewId2 = reviewService.register(member.getId(), movieId, "재미 없다",score2);

        //When
        reviewService.deleteReview(reviewId2);
        em.clear(); //영속성컨텍스트를 비운다

        //Then
        Movie movie = movieService.findOne(movieId);
        assertEquals("리뷰 삭제후 해당 영화가 갖는 리뷰의 갯수는 줄어야한다", 2-1, movie.getReviews().size());
        assertEquals("리뷰의 종합 점수는 줄어야한다", 12-4, movie.getSumScore(), 0.0001);
    }

    @Test
    public void 리뷰수정(){
        //Given
        Member member = createMember("emrhssla@naver.com", LocalDate.of(2019,11,12));
        Long movieId = movieService.register("Inception", "Its a very Hot Movie. So, I recommended this Movie to you");
        int score = 8;
        Long reviewId = reviewService.register(member.getId(), movieId, "아주 재밌어요",score);

        //When
        UpdateReviewDto updateReviewDto = new UpdateReviewDto(reviewId, 4, "바뀐 description");
        reviewService.updateReview(updateReviewDto);

        //Then
        Review review = reviewRepository.findOne(reviewId);
         assertEquals("리뷰 수정 후 리뷰 점수는 바뀐 점수어야한다", 4, review.getScore());
        assertEquals("리뷰 수정 후 리뷰 점수는 바뀐 점수어야한다", "바뀐 description", review.getDescription());


        Movie movie = movieService.findOne(movieId);
        assertEquals("리뷰 수정 후 영화 평균 점수는 바뀌어야한다", 4, movie.getAverageScore(), 0.0001);

    }

    @Test
    public void 영화에맞는리뷰찾기(){

        //Given
        Member member = createMember("emrhssla@naver.com", LocalDate.of(2019,11,12));
        Long movieId = movieService.register("Inception", "Its a very Hot Movie. So, I recommended this Movie to you");

        //When
        Movie movie = movieService.findOne(10L);

        //Then
        assertNull("영화가 없을 경우 Null이어야한다", movie);

    }

    @Test
    public void 영화가없을경우(){

        //Given
        Member member = createMember("emrhssla@naver.com", LocalDate.of(2019,11,12));
        Long movieId = movieService.register("Inception", "Its a very Hot Movie. So, I recommended this Movie to you");


        //When
        int score = 8;
        int score2 = 4;
        reviewService.register(member.getId(), movieId, "아주 재밌어요",score);
        reviewService.register(member.getId(), movieId, "재미 없다",score2);

        //Then
        List<Review> reviews = reviewService.findReviews(movieId);
        assertEquals("리뷰의 갯수는 정확해야한다", 2, reviews.size());
    }

    private Member createMember(String email, LocalDate localDate) {
        Member member = new Member();
        member.setEmail(email);
        member.setBirthDate(localDate);
        em.persist(member);
        return member;
    }



     */
}