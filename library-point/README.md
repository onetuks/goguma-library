# 🏦 포인트 모듈

## 모듈 목적
- 포인트 관련 기능을 제공하는 모듈

## 요구사항
1. 포인트 지급
2. 포인트 차감
3. 포인트 일일 지급 한계 잔여치 확인
4. 포인트 일일 지급 한계 잔여치 갱신

## 필요 라이브러리
- JDBC / DataBase / JPA
  - 멤버 객체 포인트 관련 정보 관리
- Redis
  - 일일 지급 한계치 관련 정보 관리

## 구현방향
- `Domain 모듈에서 포인트 관련 책임을 모두 전가`
  - 도메인 모듈에서는 포인트 관련된 상세 요구사항을 전혀 알지 못함.
  - 지급/차감에 대한 수행을 시작하는 트리거만 두는 방향으로 구현
  - Domain 모듈 <- Point 모듈 `의존성 역전`
    - Domain 모듈에 PointService 인터페이스 선언
    - Point 모듈에서 PointService 인터페이스 구현
- Db-Storage 모듈의 JPA 영속성 관련 로직 참조
  - Point 모듈에서 Db-Storage 모듈 implementation 의존
- 요구사항 수행 Repository 인터페이스 분리
  - `PointRepository` : 포인트 지급/차감 동작 수행
  - `DailyPointLimitRepository` : 일일 지급 한계 동작 수행
  - `PointServiceImpl` : 위 두 Repository 빈 주입받아, PointService 인터페이스 구현

