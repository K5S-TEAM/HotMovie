# 개요
<img width="895" alt="image" src="https://user-images.githubusercontent.com/46098949/168469922-b1528ae7-249a-4bdd-8bde-369f9222a3eb.png">

- 이 프로젝트는 [MSA, CI/CD를 접목시킨 영화리뷰 사이트](https://github.com/K5S-TEAM)에서 **리뷰 기능**을 담당하는 리뷰 서버입니다

# 기능
- 리뷰조회:
  - 마이페이지 내 리뷰 조회(`/reviews/short-my`)
  - 회원의 리뷰 조회(`/reviews/my`)
  - 영화의 리뷰 조회(`/movies/{movieId}/reviews`)
- 리뷰 등록(`/reviews/new`)
- 리뷰 수정(`/reviews/{reviewId}/edit`)
  - 마이페이지 내 리뷰 수정
  - 회원의 리뷰 수정
  - 영화의 리뷰 수정
- 리뷰 삭제(`/reviews/{reviewId}/cancel`)
  - 마이페이지 내 리뷰 조회
  - 회원의 리뷰 조회
  - 영화의 리뷰 조회
- API
  - 인증서버: 회원 인증
  - Member서버: 회원 닉네임 조회
  - 영화서버: 영화 이름 조회
  - 영화서버: 영화 리뷰 평균점수 전송
  - ...



# [개발문서 및 이슈](https://github.com/K5S-TEAM/HotMovie/wiki)
- ### [Home](https://github.com/K5S-TEAM/HotMovie/wiki)
- ### [1. DevOps](https://github.com/K5S-TEAM/HotMovie/wiki/1.-DevOps)
- ### [1.1 CI](https://github.com/K5S-TEAM/HotMovie/wiki/1.1-CI)
- ### [1.2. CD](https://github.com/K5S-TEAM/HotMovie/wiki/1.2.-CD)
- ### [1.3. AutoScaling](https://github.com/K5S-TEAM/HotMovie/wiki/1.3.-AutoScaling)
- ### [2. 백엔드](https://github.com/K5S-TEAM/HotMovie/wiki/2.-%EB%B0%B1%EC%97%94%EB%93%9C)
- ### [2.1 API](https://github.com/K5S-TEAM/HotMovie/wiki/2.1-API)
- ### [2.2 리뷰 조회](https://github.com/K5S-TEAM/HotMovie/wiki/2.2-%EB%A6%AC%EB%B7%B0-%EC%A1%B0%ED%9A%8C)
- ### [3. Issue](https://github.com/K5S-TEAM/HotMovie/wiki/3.-Issue)
---
### 활용 기술
`#Spring` `#Java` `#JPA` `#Mysql` `#Thymeleaf` `#Git` `#AWS` `#Jenkins` `#Docker` `#Kubernetes`

### 실현영상
  [![youtube](http://img.youtube.com/vi/Xyd8f97MVls/0.jpg)](https://youtu.be/Xyd8f97MVls?t=0s) 



