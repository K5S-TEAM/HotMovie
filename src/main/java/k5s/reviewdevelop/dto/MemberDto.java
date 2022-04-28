package k5s.reviewdevelop.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {
    Long id;
    String name;

    public MemberDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
