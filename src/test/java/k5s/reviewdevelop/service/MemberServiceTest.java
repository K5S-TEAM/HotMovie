package k5s.reviewdevelop.service;
import k5s.reviewdevelop.api.AuthAPI;
import k5s.reviewdevelop.api.MemberAPI;
import k5s.reviewdevelop.dto.MemberIdNicknameDto;
import lombok.extern.slf4j.Slf4j;
import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.persistence.EntityManager;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Autowired
    MemberAPI memberAPI;

    @Test
    @Rollback(value = false)
    public void 회원가입() throws Exception {
        //Given
        Member member = new Member();
        member.setEmail("emrhssla@gmail.com");

        //When
        Long saveId = memberService.join(member);

        //Then
        em.flush();
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {

        //Given
        Member member1 = new Member();
        member1.setEmail("emrhssla@gmail.com");
        Member member2 = new Member();
        member2.setEmail("emrhssla@gmail.com");

        //When
        memberService.join(member1);
        memberService.join(member2); //예외가 발생해야 한다.

        //Then
        fail("예외가 발생해야 한다.");
    }

    @Test
    public void 멤버조회() throws Exception {

        //Given
        Member member1 = new Member();
        member1.setEmail("emrhssla1@gmail.com");


        //When
        memberService.join(member1);

        //Then
        System.out.println("member1 = " + member1.getId()+"member1 email =" + member1.getEmail());


    }

    @Test
    public void 없는멤버조회() throws Exception {

        Member member = memberService.findOne(-2L);

        assertNull("멤버가 없을 경우 Null이어야한다", member);

    }

    @Test
    public void 닉네임조회리팩토링_적용전(){

        Movie movie = Movie.createMovie("인셉션", 2L);

        StopWatch stopWatch = new StopWatch();
        log.info("start - stream적용 전");
        stopWatch.start();


        List<Review> reviews = new ArrayList<>();
        for(Long i=1L ; i<4L ; i++) {
            reviews.add(Review.createReview(i, movie, "description"+i, 8));
        }
        List<Long> memberIdList = new ArrayList<>();
        reviews.forEach((review) ->{
            memberIdList.add(review.getMemberId());
        });
        log.info("memberIdList: {}", memberIdList);

        Map<Long, String> memberMap = new HashMap<>();
        memberAPI.requestNicknames(memberIdList).forEach((memberIdNicknameDto) ->{
            memberMap.put(memberIdNicknameDto.getId(), memberIdNicknameDto.getNickname());
        });
        log.info("memberMap: {}", memberMap);


        stopWatch.stop();
        log.info("result: {}", stopWatch.getTotalTimeSeconds());

        assertEquals("멤버의 id 수는 같다", memberMap.size(), memberIdList.size());

    }

    @Test
    public void 닉네임조회리팩토링_적용후(){

        Movie movie = Movie.createMovie("인셉션", 2L);

        StopWatch stopWatch = new StopWatch();
        log.info("start - stream적용 후");
        stopWatch.start();

        List<Review> reviews = new ArrayList<>();
        for(Long i=1L ; i<4L ; i++) {
            reviews.add(Review.createReview(i, movie, "description"+i, 8));
        }
        List<Long> memberIdList = reviews.stream().map(Review::getMemberId).collect(Collectors.toList());
        log.info("memberIdList: {}", memberIdList);


        Map<Long, String> memberMap = new HashMap<>();
        memberMap =  memberAPI.requestNicknames(memberIdList)
                .stream()
                .collect(Collectors.toMap(MemberIdNicknameDto::getId,MemberIdNicknameDto::getNickname));
        log.info("memberMap: {}", memberMap);
        stopWatch.stop();
        log.info("result: {}", stopWatch.getTotalTimeSeconds());

        assertEquals("멤버의 id 수는 같다", memberMap.size(), memberIdList.size());

    }

}