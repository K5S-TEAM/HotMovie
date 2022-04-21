package k5s.reviewdevelop.dto;

import lombok.Getter;

@Getter
public class AuthenticationResponseDto {
    Long id;

    public AuthenticationResponseDto() {
    }

    public AuthenticationResponseDto(Long id) {
        this.id = id;
    }
}
