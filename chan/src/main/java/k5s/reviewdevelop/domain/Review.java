package k5s.reviewdevelop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Review {


    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    private int rating;

    private LocalDateTime dateTime;

    private int goodCount;

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

    public void setMovie(Movie movie) {
        this.movie = movie;
        movie.getReviews().add(this);
    }



}