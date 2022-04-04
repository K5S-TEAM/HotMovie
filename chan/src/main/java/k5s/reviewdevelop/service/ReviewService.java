package k5s.reviewdevelop.service;

import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.repository.MemberRepository;
import k5s.reviewdevelop.repository.MovieRepository;
import k5s.reviewdevelop.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;

    /**
     * 리뷰 등록
     */
    public Long register(Long memberId, Long movieId, int score){

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Movie movie = movieRepository.findOne(movieId);

        //리뷰 생성
        Review review = Review.createReview(member, movie, score);

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
        review.delete();
    }

}
