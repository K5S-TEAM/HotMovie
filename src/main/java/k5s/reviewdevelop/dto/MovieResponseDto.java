package k5s.reviewdevelop.dto;

import lombok.Getter;

@Getter
public class MovieResponseDto {
    String movieName;

    public MovieResponseDto() {
    }

    public MovieResponseDto(String movieName) {
        this.movieName = movieName;
    }
}
