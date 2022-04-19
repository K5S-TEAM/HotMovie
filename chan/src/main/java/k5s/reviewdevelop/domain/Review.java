package k5s.reviewdevelop.domain;

import k5s.reviewdevelop.dto.UpdateReviewDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {


    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    private int score;

    private LocalDateTime dateTime;

    private int goodCount = 0;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public void mappingMember(Member member) {
        this.member = member;
        member.getReviews().add(this);
    }

    public void mappingMovie(Movie movie, int score) {
        this.score = score;
        movie.createAverageScore(score);
        this.movie = movie;
        movie.getReviews().add(this);
    }

    //==생성 메서드==//
    public static Review createReview(Member member, Movie movie, String description, int score) {
        Review review = new Review();
        review.mappingMember(member);
        review.mappingMovie(movie, score);
        review.description = description;
        review.dateTime =  LocalDateTime.now();
        return review;
    }

    //==비지니스 로직==//
    /** 리뷰 점수 삭제 */
    public void deleteScore() {
        movie.deleteReview(score);
    }


    /**
     * 리뷰 수정
     */
    public void updateReview(UpdateReviewDto reviewDto) {
        this.id = reviewDto.getId();
        this.score = reviewDto.getScore();
        this.dateTime = LocalDateTime.now();
        this.description = reviewDto.getDescription();
    }


    /** 리뷰 점수 삭제 */
    public void updateScore(int prevScore) {
        movie.updateReview(prevScore, score);
    }
}