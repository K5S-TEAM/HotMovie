package k5s.reviewdevelop.dto;

import lombok.Getter;

@Getter
public class MovieRequestDto {
    Long id;

    public MovieRequestDto(Long id) {
        this.id = id;
    }
}
