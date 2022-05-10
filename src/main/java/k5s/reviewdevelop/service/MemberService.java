package k5s.reviewdevelop.service;

import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.dto.AuthenticationResponseDto;
import k5s.reviewdevelop.dto.MemberDto;
import k5s.reviewdevelop.dto.MemberIdNicknameDto;
import k5s.reviewdevelop.exception.InvalidAuthenticationException;
import k5s.reviewdevelop.exception.NoLoginException;
import k5s.reviewdevelop.exception.NoMovieException;
import k5s.reviewdevelop.exception.NoNicknamesException;
import k5s.reviewdevelop.repository.MemberRepository;
import k5s.reviewdevelop.api.AuthAPI;
import k5s.reviewdevelop.api.MemberAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthAPI authAPI;
    private final MemberAPI memberAPI;
    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증

        memberRepository.save(member);

        return member.getId();
    }

    @Transactional
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
    @Transactional
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }


    /**
     *
     * 리뷰 리스트에서 review를 쓴 멤버 id 값에 따라 멤버 name이 보이게 한다
     */
    public HashMap<Long, String> findNickNamesInHTML(List<Review> reviews){

        //리뷰 리스트에 있는 id값을 리스트로 담기
        List<Long> ids = new ArrayList<>();
        reviews.forEach((review) ->{
            ids.add(review.getMemberId());
        });

        HashMap<Long, String> memberMap = new HashMap<>();

        //member Server에게 Request(id list)하고 Response(Map[id, nickName])을 받는다
        try {
            memberAPI.requestNicknames(ids).forEach((memberIdNicknameDto) ->{
                memberMap.put(memberIdNicknameDto.getId(), memberIdNicknameDto.getNickname());
            });
        }
        catch (NoNicknamesException e){
            ids.forEach((id) ->{
                memberMap.put(id, String.valueOf(id));
            });
        }

        return memberMap;
    }


    @Transactional
    public MemberDto findMember(String accessToken) {
        try {
            AuthenticationResponseDto authenticationResponseDto = authAPI.requestAuthentication(accessToken);
            return new MemberDto(authenticationResponseDto.getId(),authenticationResponseDto.getName());
        } catch (InvalidAuthenticationException e) {
            return new MemberDto(null, null);
        }
    }


    @Transactional
    public Long findMemberId(String accessToken, Model model){
        try {
            AuthenticationResponseDto authenticationResponseDto = authAPI.requestAuthentication(accessToken);
            model.addAttribute("memberName", authenticationResponseDto.getName());
            return authenticationResponseDto.getId();
        } catch(InvalidAuthenticationException e) {
            throw new NoLoginException("비회원입니다", e);
        }
    }

}