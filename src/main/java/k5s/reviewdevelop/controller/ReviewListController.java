package k5s.reviewdevelop.controller;

import k5s.reviewdevelop.domain.Movie;
import k5s.reviewdevelop.domain.Review;
import k5s.reviewdevelop.dto.AuthenticationResponseDto;
import k5s.reviewdevelop.dto.MemberDto;
import k5s.reviewdevelop.exception.NoLoginException;
import k5s.reviewdevelop.exception.NoMovieException;
import k5s.reviewdevelop.form.ReviewForm;
import k5s.reviewdevelop.repository.ReviewRepository;
import k5s.reviewdevelop.service.*;
import k5s.reviewdevelop.api.AuthAPI;
import k5s.reviewdevelop.api.MovieAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/movies/{movieId}/reviews")
public class ReviewListController {

    private final ReviewService reviewService;
    private final MovieService movieService;
    private final MemberService memberService;
    private final MovieAPI movieAPI;
    private final AuthAPI authAPI;
    private final HeaderService headerService;

    /**
     * 영화에 따른 리뷰 리스트
     */
    @GetMapping
    public String list(@CookieValue(value = "accessToken", required = false) String accessToken, @PathVariable("movieId") Long movieId, Model model) {

        /**
         * 회원 찾기
         * 회원 인증 API
         * NavgationBar에서 표기될 memberName
         * 자신이 쓴 리뷰를 판단할 수 있는 memberId
         **/
        MemberDto member = memberService.findMember(accessToken);
        model.addAttribute("memberName", member.getName());
        model.addAttribute("memberId", member.getId());

        /**
         *  영화이름 찾기
         *  영화이름찾기 API
         *  영화가 없거나 영화 서버 연결이 안될 경우 영화찾기 에러 ViewPage 이동
         * */
        String movieName = movieService.findMovieName(movieId);
        model.addAttribute("movieName", movieName);
        model.addAttribute("movieId", movieId);

        /** 리뷰 리스트 조회 **/
        List<Review> reviews = reviewService.findReviews(movieId);
        model.addAttribute("reviews", reviews);

        /**
         * 리뷰 작성자의 닉네임찾기
         * 회원 닉네임찾기 API
         * 회원서버로부터 응답을 못 받는 경우 닉네임 필드값은 id값으로 출력
         **/
        Map<Long, String> nickNames = memberService.findNickNamesInHTML(reviews);
        model.addAttribute("nickNames", nickNames);

        return "movies/reviews/list";
    }

    /**
     * 영화 리뷰 쓰기
     */
    @GetMapping("/new")
    public String write(@CookieValue(value = "accessToken", required = false) String accessToken, @PathVariable("movieId") Long movieId, Model model, ReviewForm form){
        /**
         * 회원 비회원 식별
         * 비회원일 경우 NoLoginException
         **/
        headerService.loadHeader(accessToken, model);

        String movieName = movieService.findMovieName(movieId);
        model.addAttribute("movieName", movieName);
        return "movies/reviews/new";
    }

    @PostMapping("/new")
    public String register(@CookieValue(value = "accessToken", required = false) String accessToken,
                           @PathVariable("movieId") Long movieId, @Valid ReviewForm form, BindingResult bindingResult, Model model){
        Movie movie = movieService.findOne(movieId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("movieName", movie.getName());
            return "movies/reviews/new";
        }
        AuthenticationResponseDto authenticationResponseDto = authAPI.requestAuthentication(accessToken);
        reviewService.register(movie.getName(), authenticationResponseDto.getId(), movieId, form.getDescription(), form.getScore());
        movieAPI.sendMovieAverageScore(movieId, movie.getAverageScore());
        return "redirect:/movies/{movieId}/reviews";
    }


    /**
     * 비회원이 회원전용기능을 사용하는 경우
     */
    @ExceptionHandler
    public String noLoginExceptionHandler(NoLoginException e) {
        return "redirect:/reviews/login";
    }


    /**
     * 영화가 없는 경우거나 영화서버 오류인 경우
     */
    @ExceptionHandler
    public String noMovieExceptionHandler(NoMovieException e) {
        return "movies/reviews/error";
    }
}
