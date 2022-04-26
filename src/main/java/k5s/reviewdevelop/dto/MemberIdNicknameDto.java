package k5s.reviewdevelop.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberIdNicknameDto {
    Long id;
    String nickname;

    public MemberIdNicknameDto(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
