package k5s.reviewdevelop.service;

import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.dto.MemberIdNicknameDto;
import k5s.reviewdevelop.repository.MemberRepository;
import k5s.reviewdevelop.service.api.MemberAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthService authService;
    private final MemberAPI memberAPI;
    /**
     * 회원가입
     */
    @Transactional //변경
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers =
                memberRepository.findByEmail(member.getEmail());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public Member findMember(String accessToken) {
        Long id = authService.requestAuthentication(accessToken);
        return memberRepository.findOne(id);
    }

    /**
     * 영화 이름요청
     * @return
     */
    /*
    public HashMap<Long, String> findNickNames(List<Long> ids){
        List<MemberIdNicknameDto> memberIdNicknameDtos = memberAPI.requestNicknames(ids);
        HashMap<Long, String> memberMap = new HashMap<>();
        memberIdNicknameDtos.forEach((memberIdNicknameDto) ->{
            memberMap.put(memberIdNicknameDto.getId(), memberIdNicknameDto.getNickname());
        });
        return memberMap;
    }

     */

    /**
     *
     * 리뷰 리스트에서 review를 쓴 멤버 id 값에 따라 멤버 name이 보이게 한다
     */

    public HashMap<Long, String> findNickNamesInHTML(List<Review> reviews){

        //리뷰 리스트에 있는 id값을 리스트로 담기
        List<Long> ids = new ArrayList<>();
        for (Review review : reviews) {
            ids.add(review.getMemberId());
        }

        //member Server에게 Request(id list)하고 Response(Map[id, nickName])을 받는다
        List<MemberIdNicknameDto> memberIdNicknameDtos = memberAPI.requestNicknames(ids);
        HashMap<Long, String> memberMap = new HashMap<>();
        memberIdNicknameDtos.forEach((memberIdNicknameDto) ->{
            memberMap.put(memberIdNicknameDto.getId(), memberIdNicknameDto.getNickname());
        });
        
        return memberMap;
    }

}