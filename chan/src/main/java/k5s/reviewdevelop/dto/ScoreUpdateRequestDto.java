package k5s.reviewdevelop.dto;

import lombok.Getter;

@Getter
public class ScoreUpdateRequestDto {
    double averageScore;

    public ScoreUpdateRequestDto(double averageScore) {
        this.averageScore = averageScore;
    }
}
