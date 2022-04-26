package k5s.reviewdevelop.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MemberNicknamesRequestDto {
    List<Long> ids;

    public MemberNicknamesRequestDto(List<Long> ids) {
        this.ids = ids;
    }
}
