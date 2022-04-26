package k5s.reviewdevelop.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MemberNicknamesResponseDto {
    List<MemberIdNicknameDto> memberNicknames;

    public MemberNicknamesResponseDto() {
    }

    public MemberNicknamesResponseDto(List<MemberIdNicknameDto> memberNicknames) {
        this.memberNicknames = memberNicknames;
    }
}
