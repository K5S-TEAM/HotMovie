package k5s.reviewdevelop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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

    public void setMember(Member member) {
        this.member = member;
        member.getReviews().add(this);
    }

    public void setMovie(Movie movie, int score) {
        this.score = score;
        movie.setAverageScore(score);
        this.movie = movie;
        movie.getReviews().add(this);
    }

    //==생성 메서드==//
    public static Review createReview(Member member, Movie movie, int score) {
        Review review = new Review();
        review.setMember(member);
        review.setMovie(movie, score);
        review.setDateTime(LocalDateTime.now());
        return review;
    }

    //==비지니스 로직==//
    /** 리뷰 삭제 */
    public void delete() {
        movie.deleteReview(score);
    }

}