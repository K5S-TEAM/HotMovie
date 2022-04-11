package k5s.reviewdevelop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie {

    @Id
    @GeneratedValue
    @Column(name = "movie_id")
    private Long id;

    private String name;

    private String description;

    private int num = 0;

    private float sumScore = 0;

    private float averageScore;

    //==Movie가 삭제되면 review들도 삭제된다==//
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @ManyToMany(mappedBy = "movies")
    private List<Member> members = new ArrayList<>();

    public void addMember(Member member) {
        members.add(member);
    }

    public void createAverageScore(int score){
        this.sumScore += score;
        this.num += 1;
        this.averageScore = (sumScore/num);
    }

    //== 생성 메서드 ==//
    public static Movie createMovie(String name, String description){
        Movie movie = new Movie();
        movie.name = name;
        movie.description = description;
        return movie;
    }


    //==비지니스 로직==//
    /** 리뷰 삭제 */
    public void deleteReview(int score) {
        this.num -= 1;
        this.sumScore -= score;

        if(num != 0) {
            this.averageScore = (sumScore / num);
        }
        else {
            this.averageScore = 0.00f; /**리뷰 갯수가 0일 경우 분모에 0이 올순 없다**/
        }
    }
}
