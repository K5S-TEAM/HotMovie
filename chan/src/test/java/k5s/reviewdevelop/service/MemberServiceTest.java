package k5s.reviewdevelop.service;

import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

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

}