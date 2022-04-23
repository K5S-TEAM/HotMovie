package k5s.reviewdevelop.service;

import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.dto.UpdateReviewDto;
import k5s.reviewdevelop.repository.MemberRepository;
import k5s.reviewdevelop.repository.MovieRepository;
import k5s.reviewdevelop.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final MovieService movieService;
 
    /**
     * 리뷰 등록
     */
    public Long register(Long memberId, Long movieId, String description, int score){

        //엔티티 조회
        if (movieRepository.findOne(movieId)==null) {
            movieService.register(movieId);
        }
        Movie movie = movieRepository.findOne(movieId);
        //리뷰 생성
        Review review = Review.createReview(memberId, movie, description, score);

        reviewRepository.save(review);
        return review.getId();

    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public void deleteReview(Long reviewId) {
        //리뷰 엔티티 조회
        Review review = reviewRepository.findOne(reviewId);
        //리뷰 삭제
        review.deleteScore();
        reviewRepository.delete(reviewId);
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public void updateReview(UpdateReviewDto updateReviewDto) {
        //리뷰 엔티티 조회
        Review review = reviewRepository.findOne(updateReviewDto.getId());
        Integer prevScore = review.getScore();
        review.updateReview(updateReviewDto);
        review.updateScore(prevScore);
    }

    public List<Review> findReviews(Long movieId) {
        return reviewRepository.findReviews(movieId);
    }

}
