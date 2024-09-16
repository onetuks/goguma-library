# 🍠 고구마 서재 서버
![service-banner](https://github.com/user-attachments/assets/318a2bf2-bf5a-484c-a0ea-fbf24a68d72e)
- 배포 URL: https://goguma-chimpanzees.site
- 프론트엔드 Github: https://github.com/onetuks/goguma-library-client
## ☕️ 프로젝트 소개
> 독립서적을 보기 위해선 독립서점에 방문해야만 합니다.
어떤 책이 있는지, 어떤 내용일지 알지도 못한채 책을 읽을 엄두를 내지 못하고 그만두게 됩니다.
독립출판물에 대한 서평을 남기고 서재에 담아 타인과 공유할 수 있도록 하면서 독립출판에 대한 관심이 행동으로 이어지기를 바랍니다.
- 서평 피드 페이지에서 다른 사람의 서평을 실시간으로 확인할 수 있습니다.
- 매주 월요일 3권씩 추천되는 독립출판물에 서평을 남기고 포인트를 받을 수 있습니다.
- 관심있는 카테고리를 기반으로 독립출판 서적을 추천받을 수 있습니다.
- 작성한 서평을 서재에서 모아보고, 독서 통계를 그래프로 제공합니다.
- 나만 알고있는 재밌는 독립서적을 직접 등록하여 공유할 수 있습니다.
- 포인트와 뱃지로 나의 활동을 점검할 수 있습니다.
## 🏭 팀구성

| 개발                                                        | 디자인                                                        |
| --------------------------------------------------------- | ---------------------------------------------------------- |
| ![](https://avatars.githubusercontent.com/u/54990890?v=4) | ![](https://avatars.githubusercontent.com/u/178856248?v=4) |
| [박세영](https://www.github.com/onetuks)                     | [임은규](https://github.com/potatonian12345)                  |

## 📌 프로젝트 주요 관심사
- 의존역전을 통해 도메인에 집중한 개발 아키텍처
- 관심사에 따른 멀티 모듈 적용
- 모듈간 의존성 감소를 위한 Message Stream 시스템 적용
- Docker Compose 를 통한 배포 패키지 구성
- Jacoco, sonar, TestContainer 를 활용한 코드 품질 관리
### 🪄 백엔드 기술 스택

| Application                                                                                                                                                                                                                                            | Infra                                                                                                                                                                                                         |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| - Java 21<br>- Spring Boot 3.6.0<br>- Spring actuator<br>- Spring Security<br>- Spring Validation<br>- Spring Cache<br>- Spring Cloud Config<br>- Spring Data JPA<br>- QueryDSL<br>- Jackson<br>- WebFlux<br>- JWT<br>- TestFixture<br>- TestContainer | - MySQL 8.0.35<br>- Flyway<br>- Prometheus<br>- Grafana<br>- K6<br>- Loki<br>- OAuth2<br>- Caffeine Cache<br>- Github Action<br>- Redis 7.4.0<br>- Docker<br>- spotless<br>- jacoco<br>- sonar<br>- Arch-Unit |
### 🛠️ 백엔드 인프라
#### 배포
![image](https://github.com/user-attachments/assets/1056c641-d232-4355-9cae-9533208f2943)
#### 서버
![image](https://github.com/user-attachments/assets/60a4068f-b8d1-434b-884a-d4e4c7f15caf)
### 🍉 프로젝트 구조
![image](https://github.com/user-attachments/assets/60a47ad3-bcd4-449e-832d-01e0d9132487)
## 📽️ 시연영상
### 인증
#### 로그인
![로그인](https://github.com/user-attachments/assets/e5221910-4cee-4404-8f4c-29d99a54d162)
#### 로그아웃
![로그아웃](https://github.com/user-attachments/assets/00c3d7a1-fe54-4472-a7c1-adb62c8dea5f)
### 메인
![메인](https://github.com/user-attachments/assets/f1be50d1-d668-421c-bb06-ff78d28c73ba)
### 도서
#### 도서 검색
![도서검색](https://github.com/user-attachments/assets/1b5e08d0-0ebc-4e96-9dab-6c68b660ce57)
#### 도서 등록
![도서등록](https://github.com/user-attachments/assets/684cd0d5-3583-4f38-a1f3-a80bb7447753)
### 서재
#### 나의 서재
![나의서재](https://github.com/user-attachments/assets/021413fb-9196-49b1-9151-5d04d9f90e4b)
#### 타인의 서재
![타인의서재](https://github.com/user-attachments/assets/151e3dba-ce69-49a3-8476-923280902768)
### ETC
#### 피드
![피드](https://github.com/user-attachments/assets/b7a8a829-90a0-456f-add0-19874a0e8ede)
#### 마이페이지
![마이페이지](https://github.com/user-attachments/assets/9b52e5f2-f90e-4153-a63a-a4816aecfc1c)
#### 출석 체크
![출석체크](https://github.com/user-attachments/assets/dca8d471-d57e-4ab6-a38c-40afec19e4ec)
## ⛓️ 관련 링크
- [메시지 스트림을 활용한 동시성 문제 처리](https://velog.io/@onetuks/락-말고-동시성-문제-처리하기)
- [ETag, Cache 를 활용한 서버 성능 개선 시도](https://velog.io/@onetuks/ETag와-캐시로-서버-성능-올리기)
- [락 이외의 동시성 처리를 시도하게된 계기](https://velog.io/@onetuks/Transactional-과-비관적-락)
- [유니크 제약조건과 동시성 문제](https://velog.io/@onetuks/도서-등록-동시성-문제-해결)
- [멀티모듈 경험담](https://velog.io/@onetuks/멀티모듈-경험담)
