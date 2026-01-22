# DinePick Backend

레스토랑 예약 시스템 DinePick의 백엔드 API 서버입니다.

##  프로젝트 개요

DinePick은 사용자가 레스토랑을 검색하고 예약할 수 있는 서비스입니다. 이 프로젝트는 Spring Boot 기반의 RESTful API를 제공합니다.

##  기술 스택

- **Framework**: Spring Boot
- **Language**: Java 17
- **Build Tool**: Gradle
- **Database**: MySQL
- **ORM**: Spring Data JPA (Hibernate)
- **Security**: Spring Security + JWT
- **Authentication**: JWT (JSON Web Token)
- **Libraries**:
  - Lombok
  - JJWT (JWT 라이브러리)
  - Spring Validation

##  주요 기능

### 인증 및 회원 관리
- 회원가입 및 로그인
- JWT 기반 인증 (Access Token + Refresh Token)
- 내 정보 조회 및 수정
- 회원 탈퇴 및 관리자용 회원 복구
- 관리자 전용 전체 회원/탈퇴 회원 목록 조회

### 레스토랑 관리
- 레스토랑 목록 조회 및 검색 (키워드, 카테고리)
- 레스토랑 상세 정보 조회
- **위치 기반 주변 레스토랑 검색 (거리순 정렬)**

### 예약 관리
- **실시간 예약 가능 여부 확인**
- 예약 생성
- 내 예약 목록 조회 (회원용 별도 엔드포인트 제공)
- 예약 상세 조회, 수정 및 취소

##  프로젝트 구조

```
src/main/java/com/dinepick/dinepickbackend/
├── config/              # 설정 파일 (Security, DataInit, Web)
├── controller/          # REST API 컨트롤러
│   ├── AuthController        # 인증 (회원가입, 로그인 등)
│   ├── MemberController      # 회원 관리 (내 정보, 관리자 기능)
│   ├── MyPageController      # 마이페이지 (내 예약 목록)
│   ├── ReservationController # 예약 서비스
│   └── RestaurantController  # 레스토랑 서비스
├── dto/                 # 데이터 전송 객체
├── entity/              # JPA 엔티티
├── exception/           # 예외 처리
├── repository/          # JPA 리포지토리
├── security/            # JWT 및 보안 관련
└── service/             # 비즈니스 로직
```

##  시작하기

### 사전 요구사항

- Java 17
- MySQL 8.0 이상
- Gradle 8.5 이상

### 데이터베이스 설정

1. MySQL에 데이터베이스 생성:
```sql
CREATE DATABASE restaurant_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. `src/main/resources/application.properties` 파일에서 데이터베이스 연결 정보 수정:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/restaurant_db?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 애플리케이션 실행

#### Windows (PowerShell)
```powershell
.\gradlew.bat bootRun
```

#### Linux/Mac
```bash
./gradlew bootRun
```

애플리케이션은 기본적으로 `http://localhost:8080`에서 실행됩니다.

##  환경 변수

`application.properties` 또는 환경 변수로 설정 가능합니다:

| 설정 | 설명 | 기본값 |
|------|------|--------|
| `server.port` | 서버 포트 | 8080 |
| `jwt.secret` | JWT 시크릿 키 | (충분히 긴 비밀키 권장) |
| `spring.jpa.hibernate.ddl-auto` | DDL 자동 생성 모드 | update |

>  **주의**: 프로덕션 환경에서는 `jwt.secret`을 반드시 변경하고, `spring.jpa.hibernate.ddl-auto`를 `validate` 또는 `none`으로 설정하세요.

##  API 엔드포인트

### 인증 (Auth)
- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/login` - 로그인
- `POST /api/auth/logout` - 로그아웃
- `POST /api/auth/refresh` - Access Token 갱신

### 회원 (Member)
- `GET /api/members/me` - 내 정보 조회
- `PUT /api/members/me` - 내 정보 수정
- `DELETE /api/members/me` - 회원 탈퇴
- `GET /api/members` - 전체 회원 조회 (관리자)
- `GET /api/members/{id}` - 특정 회원 조회 (관리자)
- `POST /api/members/{id}/restore` - 회원 복구 (관리자)
- `GET /api/members/withdrawn` - 탈퇴 회원 목록 조회 (관리자)

### 마이페이지 (MyPage)
- `GET /api/me/reservations` - 내 예약 목록 조회

### 레스토랑 (Restaurant)
- `GET /api/restaurants` - 레스토랑 목록 조회 및 검색
- `GET /api/restaurants/{id}` - 레스토랑 상세 조회
- `GET /api/restaurants/nearby` - 주변 레스토랑 검색 (위치 기반)

### 예약 (Reservation)
- `GET /api/reservations/availability` - 예약 가능 여부 확인
- `POST /api/reservations` - 예약 생성
- `GET /api/reservations/my` - 내 예약 목록 조회
- `PUT /api/reservations/{id}` - 예약 수정
- `DELETE /api/reservations/{id}` - 예약 취소

##  테스트

```bash
./gradlew test
```

##  개발 참고사항

### JWT 인증 흐름
1. 사용자가 로그인하면 Access Token과 Refresh Token을 발급받습니다.
2. API 요청 시 Authorization 헤더에 `Bearer {accessToken}` 형식으로 토큰을 포함합니다.
3. Access Token이 만료되면 Refresh Token으로 새로운 Access Token을 발급받습니다.

### 예외 처리
- 모든 예외는 `GlobalExceptionHandler`에서 중앙 집중식으로 처리됩니다.
- 커스텀 예외는 `exception` 패키지에 정의되어 있습니다.

### 데이터 초기화
- `DataInit` 클래스에서 애플리케이션 시작 시 초기 데이터를 생성합니다.

##  기여

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

##  라이선스

이 프로젝트는 팀 프로젝트입니다.

##  연락처

프로젝트 관련 문의사항이 있으시면 이슈를 등록해주세요.
