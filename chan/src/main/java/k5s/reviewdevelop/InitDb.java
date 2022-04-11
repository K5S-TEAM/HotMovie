package k5s.reviewdevelop;

import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.service.MovieService;
import k5s.reviewdevelop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        private final ReviewService reviewService;
        private final MovieService movieService;

        public void dbInit1() {
            Member member = createMember("emrhssla@naver.com");
            em.persist(member);
            Long movie1 = movieService.register("Inception", "Its movie A");
            Long movie2 = movieService.register("Venom", "Its movie B");
            reviewService.register(member.getId(), movie1, "재미있습니다", 8);
            reviewService.register(member.getId(), movie2, "재미없습니다", 3);
            reviewService.register(member.getId(), movie2, "완전 재미있습니다", 9);
        }

        public void dbInit2() {

            Member member = createMember("emrhssla@gmail.com");
            em.persist(member);
            Long movie1 = movieService.register("Avatar", "Its movie C");
            Long movie2 = movieService.register("IronMan", "Its movie D");
            reviewService.register(member.getId(), movie1, "그저그래요",6);
            reviewService.register(member.getId(), movie2, "재미없습니다",4);

        }

        private Member createMember(String email) {
            Member member = new Member();
            member.setEmail(email);
            member.setBirthDate(LocalDate.now());
            em.persist(member);
            return member;
        }


    }
}