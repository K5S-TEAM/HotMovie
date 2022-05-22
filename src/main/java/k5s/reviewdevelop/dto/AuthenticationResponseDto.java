package k5s.reviewdevelop.dto;

import lombok.Getter;

@Getter
public class AuthenticationResponseDto {
    Long id;
    String name;

    public AuthenticationResponseDto() {
    }

    public AuthenticationResponseDto(Long id) {
        this.id = id;
    }

}
