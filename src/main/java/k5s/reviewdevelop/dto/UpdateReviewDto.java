package k5s.reviewdevelop.dto;

import k5s.reviewdevelop.form.ReviewForm;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateReviewDto {
    private Long id;

    @NotNull(message = "점수를 선택해주세요")
    private int score;
    private String description;

    public UpdateReviewDto(Long id, int score, String description) {
        this.id = id;
        this.score = score;
        this.description = description;
    }

    public UpdateReviewDto(ReviewForm form) {
        this.id = form.getId();
        this.score = form.getScore();
        this.description = form.getDescription();
    }
}