package k5s.reviewdevelop.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class ReviewForm {
    @NotNull(message = "점수를 선택해주세요")
    private Integer score;
    private String description;

}
