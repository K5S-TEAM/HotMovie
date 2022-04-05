package k5s.reviewdevelop;

import k5s.reviewdevelop.domain.Member;
import k5s.reviewdevelop.domain.Movie;
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

        public void dbInit1() {
            Member member = createMember("emrhssla@naver.com");
            em.persist(member);
            Movie movie1 = createMovie("Inception", "Its movie A");
            em.persist(movie1);
            Movie movie2 = createMovie("Venom", "Its movie B");
            em.persist(movie2);
            reviewService.register(member.getId(), movie1.getId(), "재미있습니다", 8);
            reviewService.register(member.getId(), movie2.getId(), "재미없습니다", 3);

        }

        public void dbInit2() {

            Member member = createMember("emrhssla@gmail.com");
            em.persist(member);
            Movie movie1 = createMovie("Avatar", "Its movie C");
            em.persist(movie1);
            Movie movie2 = createMovie("IronMan", "Its movie D");
            em.persist(movie2);
            reviewService.register(member.getId(), movie1.getId(), "그저그래요",6);
            reviewService.register(member.getId(), movie2.getId(), "재미없습니다",4);

        }

        private Member createMember(String email) {
            Member member = new Member();
            member.setEmail(email);
            member.setBirthDate(LocalDate.now());
            em.persist(member);
            return member;
        }

        private Movie createMovie(String name, String description){
            Movie movie = new Movie();
            movie.setName(name);
            movie.setDescription(description);
            em.persist(movie);
            return movie;
        }

    }
}