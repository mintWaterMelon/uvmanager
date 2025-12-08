# UV Alert

UV Alert는 기상청 공공데이터를 활용하여 사용자가 선택한 지역의 날씨, 온도, 자외선지수, 강수확률 정보를 제공하는 모바일 앱 프로젝트입니다.

백엔드는 Java 17과 Spring Boot 3 기반 REST API로 구현하고, 모바일 앱은 Expo React Native 기반으로 구현합니다.

---

## 1. 프로젝트 소개

UV Alert는 사용자가 선택한 지역을 기준으로 오늘, 내일, 모레의 시간대별 날씨와 자외선 정보를 확인할 수 있는 앱입니다.

홈 화면에서는 다음 정보를 제공합니다.

- 현재 시간과 선택 지역
- 오늘 / 내일 / 모레 날짜 선택
- 시간대별 날씨 및 온도
- 시간대별 자외선지수
- 시간대별 강수확률
- 날씨, 밤/낮, 자외선 위험도에 따른 배경 테마
- 현재 상황에 맞는 사용자 안내 메시지

주요 데이터는 기상청 공공데이터 API를 통해 조회합니다.

---

## 2. 주요 기능

### 홈 대시보드

- 지역과 날짜 기준 홈 대시보드 조회
- 오늘 / 내일 / 모레 데이터 조회
- 시간대별 날씨 및 온도 표시
- 시간대별 자외선지수 표시
- 시간대별 강수확률 표시
- 현재 시간대 강조
- 대표 날씨, 대표 기온, 최대 자외선지수, 최대 강수확률 계산
- 조건별 홈 배경 테마 결정
- 자외선, 날씨, 강수확률, 밤/낮 조건에 따른 안내 메시지 생성

### 지역 검색

- 지역명 키워드 검색
- 기상청 단기예보 격자 좌표 제공
- 기본 지역 코드 기반 조회 지원

### 푸시 설정

- 자외선 알림 활성화 여부 설정
- 자외선 알림 기준값 설정
- 알림 시간 설정
- 현재 저장된 푸시 설정 조회 및 수정

> 실제 푸시 발송 기능은 추후 구현 예정입니다.

### 모바일 앱

- Expo React Native 기반 화면 구성
- 홈 화면, 위치 설정, 푸시 설정, 라이선스 화면 구성
- 백엔드 REST API 연동 구조 적용

---

## 3. 기술 스택

### Backend

| 구분 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5.x |
| Build Tool | Gradle |
| Build Script | Groovy |
| Packaging | Jar |
| ORM | Spring Data JPA / Hibernate |
| Database | H2, PostgreSQL |
| API Documentation | Swagger / OpenAPI, springdoc-openapi |
| Test | JUnit 5, AssertJ, Mockito, Spring MVC Test, Data JPA Test |
| CI | GitHub Actions |
| External API | 기상청 공공데이터 API |

### Mobile

| 구분 | 기술 |
|---|---|
| Framework | Expo |
| UI | React Native |
| Language | TypeScript |
| Routing | Expo Router |

### 추후 도입 예정

| 구분 | 예정 기술 |
|---|---|
| DB Migration | Flyway |
| Cache | Redis |
| Deployment | Docker, Render 또는 AWS EC2/RDS |
| CD | GitHub Actions 기반 자동 배포 |
| Authentication | Spring Security + JWT, 로그인 기능 추가 시 도입 |

---

## 4. 프로젝트 구조

```text
uvalert
├─ backend
│  ├─ src/main/java/com/mintWaterMelon/uvalert
│  │  ├─ area
│  │  ├─ home
│  │  ├─ push
│  │  ├─ setting
│  │  └─ weather
│  ├─ src/main/resources
│  └─ src/test
├─ mobile
├─ .github/workflows
└─ docker-compose.yml
```

### Backend 패키지 역할

| 패키지 | 역할 |
|---|---|
| `area` | 지역 검색, 기상청 격자 좌표 관리 |
| `home` | 홈 대시보드, 배경, 안내 메시지, 테이블 응답 생성 |
| `push` | 푸시 알림 설정 조회 및 수정 |
| `setting` | 앱 설정 관리 |
| `weather` | 기상청 자외선지수 API, 단기예보 API 연동 |

---

## 5. API 문서

Swagger UI를 통해 API 목록과 요청/응답 구조를 확인할 수 있습니다.

### Local

- Swagger UI: <http://localhost:8080/swagger-ui/index.html>
- OpenAPI JSON: <http://localhost:8080/v3/api-docs>

### 주요 API

| 구분 | Method | Endpoint | 설명 |
|---|---|---|---|
| 홈 | GET | `/api/home/dashboard` | 지역과 날짜 기준 홈 대시보드 조회 |
| 지역 | GET | `/api/areas` | 지역 목록 및 키워드 검색 |
| 푸시 설정 | GET | `/api/push-settings` | 푸시 설정 조회 |
| 푸시 설정 | PUT | `/api/push-settings` | 푸시 설정 수정 |

### 홈 대시보드 요청 예시

```text
GET /api/home/dashboard?areaNo=1100000000&dateType=TODAY
```

`dateType`은 다음 값을 사용할 수 있습니다.

```text
TODAY
TOMORROW
DAY_AFTER_TOMORROW
```

---

## 6. 환경별 설정

Spring Profile을 사용하여 실행 환경별 설정을 분리했습니다.

| Profile | 설정 파일 | DB | 용도 |
|---|---|---|---|
| local | `application-local.properties` | PostgreSQL | 로컬 개발 환경 |
| test | `application-test.properties` | H2 | 일반 테스트 실행 환경 |
| prod | `application-prod.properties` | PostgreSQL | 운영 배포 환경 |

### local

- Docker Compose로 실행한 PostgreSQL 사용
- Flyway migration으로 DB 스키마 생성
- Hibernate는 `ddl-auto=validate`로 Entity와 DB 스키마 일치 여부만 검증
- SQL 로그 출력

### test

- H2 인메모리 DB 사용
- `ddl-auto=create-drop`
- Flyway 비활성화
- 테스트용 API Key 사용

### prod

- 운영 DB 정보는 환경변수로 주입
- Flyway migration으로 DB 스키마 관리
- Hibernate는 `ddl-auto=validate`
- SQL 로그 비활성화

---

## 7. 환경변수

프로젝트 실행에는 기상청 API Key가 필요합니다.

실제 환경변수는 프로젝트 루트의 `.env` 파일에 작성합니다.

```env
KMA_SERVICE_KEY=기상청_API_KEY
```

### local

| 변수명 | 설명 |
|---|---|
| `KMA_SERVICE_KEY` | 기상청 공공데이터 API 인증키 |

### prod

| 변수명 | 설명 |
|---|---|
| `DATABASE_URL` | 운영 DB JDBC URL |
| `DATABASE_USERNAME` | 운영 DB 사용자명 |
| `DATABASE_PASSWORD` | 운영 DB 비밀번호 |
| `KMA_SERVICE_KEY` | 기상청 공공데이터 API 인증키 |

Windows PowerShell 예시:

```powershell
$env:KMA_SERVICE_KEY="본인_기상청_API_KEY"
```

---

## 8. 로컬 실행 방법

### 1. Repository Clone

```bash
git clone <repository-url>
cd uvalert
```

### 2. Java 버전 확인

이 프로젝트는 Java 17을 사용합니다.

```bash
java -version
```

### 3. 환경변수 파일 생성

프로젝트 루트에 `.env` 파일을 생성합니다.

```env
KMA_SERVICE_KEY=본인_기상청_API_KEY
```

### 4. 로컬 PostgreSQL 실행

로컬 개발 DB는 Docker Compose로 실행할 수 있습니다.

```bash
docker compose up -d
```

PostgreSQL 접속 정보:

| 항목 | 값 |
|---|---|
| Host | localhost |
| Port | 5432 |
| Database | uvalert |
| Username | uvalert |
| Password | uvalert |
| JDBC URL | `jdbc:postgresql://localhost:5432/uvalert` |

컨테이너 확인:

```bash
docker ps
```

### 5. 로컬 DB 초기화

Flyway를 처음 적용했거나 기존에 Hibernate `ddl-auto=update`로 생성된 테이블이 남아 있는 경우, 로컬 개발 DB를 초기화한 뒤 실행합니다.

```bash
docker compose down -v
docker compose up -d
```

### 6. 백엔드 서버 실행

Windows PowerShell:

```powershell
cd backend
$env:KMA_SERVICE_KEY="본인_기상청_API_KEY"
.\gradlew.bat bootRun
```

Mac / Linux:

```bash
cd backend
export KMA_SERVICE_KEY="본인_기상청_API_KEY"
./gradlew bootRun
```

서버 실행 후 아래 주소로 접속합니다.

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## 9. 테스트

JUnit 5, AssertJ, Mockito, Spring MVC Test, Data JPA Test, Testcontainers를 사용하여 테스트를 구성했습니다.

### 테스트 실행

Windows PowerShell:

```powershell
cd backend
.\gradlew.bat clean test
```

Mac / Linux:

```bash
cd backend
./gradlew clean test
```

### 테스트 구성

| 테스트 | 설명 |
|---|---|
| `HomeTableSummaryCalculatorTest` | 홈 테이블의 최댓값, 대표 날씨, 대표 기온 계산 검증 |
| `HomeAdviceServiceTest` | 자외선, 날씨, 강수확률, 밤/낮 조건에 따른 안내 메시지 검증 |
| `HomeBackgroundServiceTest` | 홈 화면 배경 테마 결정 로직 검증 |
| `AreaControllerTest` | 지역 검색 API Controller 테스트 |
| `PushSettingControllerTest` | 푸시 설정 API Controller 테스트 |
| `PushSettingServiceTest` | 푸시 설정 비즈니스 로직 단위 테스트 |
| `PushSettingRepositoryTest` | JPA Repository 저장, 조회, 수정, 삭제 테스트 |

### 테스트 전략

- Service 로직은 순수 단위 테스트로 검증합니다.
- Controller는 `@WebMvcTest`와 `@MockitoBean`으로 API 요청/응답을 검증합니다.
- Repository는 `@DataJpaTest`로 JPA 저장/조회 동작을 검증합니다.
- 테스트 환경에서는 H2 인메모리 DB를 사용합니다.

---

## 10. CI

GitHub Actions를 사용하여 백엔드 테스트를 자동화했습니다.

Workflow 파일 위치:

```text
.github/workflows/backend-ci.yml
```

### 실행 조건

- `main` 브랜치 push
- `develop` 브랜치 push
- `main` / `develop` 대상 Pull Request
- 수동 실행

### 실행 작업

```text
Checkout source code
→ Set up JDK 17
→ Set up Gradle
→ Grant execute permission for Gradle wrapper
→ backend 기준 ./gradlew clean test 실행
```

---

## 11. 데이터베이스

현재 로컬 개발과 테스트는 다음 방식으로 분리합니다.

| 환경 | DB | 목적 |
|---|---|---|
| local | PostgreSQL 또는 H2 | 로컬 서버 실행 |
| test | H2 in-memory | 테스트 실행 |
| prod | PostgreSQL | 운영 배포 예정 |

운영 환경에서는 `ddl-auto=validate`를 사용하고, Flyway으로 DB 변경 이력을 SQL migration 파일로 관리합니다.

Migration 파일 위치:

```text
backend/src/main/resources/db/migration

---

## 12. 외부 API

현재 백엔드는 기상청 공공데이터 API를 사용합니다.

| API | 용도 |
|---|---|
| Living Weather Index API | 자외선지수 조회 |
| Short Forecast API | 단기예보, 날씨, 온도, 강수확률 조회 |

공통 API 주소와 path는 `application.properties`에 두고, API Key는 환경변수로 분리합니다.

---

## 13. 배포 계획

아직 운영 배포는 진행 전입니다.

예정 배포 흐름은 다음과 같습니다.

```text
1. Dockerfile 작성
2. Spring Boot Jar 빌드
3. Docker 이미지 생성
4. Render 또는 AWS EC2에 배포
5. PostgreSQL 운영 DB 연결
6. GitHub Actions로 자동 배포 구성
```

1차 배포는 Render 또는 Fly.io 같은 간단한 PaaS를 사용하고, 이후 AWS EC2/RDS 구조로 확장할 계획입니다.

---

## 14. 향후 개선 계획

- Flyway를 통한 DB migration 관리
- Redis 캐시 도입
- 기상청 API 응답 캐싱
- 실제 푸시 알림 발송 기능 구현
- Dockerfile 추가
- Render 또는 AWS 배포
- GitHub Actions CD 구성
- Testcontainers 기반 PostgreSQL 통합 테스트
- 로그인 기능 필요 시 Spring Security + JWT 도입

---

## 15. 학습 및 구현 포인트

이 프로젝트를 통해 다음 내용을 학습하고 적용합니다.

### Spring Boot

- Controller, Service, Repository 계층 분리
- DTO 기반 요청/응답 설계
- Profile 기반 환경 설정 분리
- 외부 API Client 구성

### JPA / Hibernate

- Entity와 Repository 구성
- H2와 PostgreSQL 환경 분리
- `ddl-auto` 전략 이해
- 운영 환경에서 `validate` 전략 사용 준비

### 테스트

- JUnit 5 기반 단위 테스트
- Mockito를 이용한 의존성 분리
- `@WebMvcTest` 기반 Controller 테스트
- `@DataJpaTest` 기반 Repository 테스트
- GitHub Actions에서 자동 테스트 실행

### 문서화

- Swagger/OpenAPI 적용
- API 요청/응답 문서화
- README 기반 실행 방법, 테스트 방법 정리

### 배포 준비

- Docker Compose를 이용한 로컬 PostgreSQL 실행
- 환경변수 기반 민감 정보 관리
- CI와 CD의 차이 이해
- 배포 환경별 설정 분리

---

## 16. 프로젝트 상태

현재 완료된 작업:

```text
Spring Boot 백엔드 기본 구조 구성
Expo React Native 모바일 앱 구조 구성
기상청 자외선지수 API 연동
기상청 단기예보 API 연동
지역 검색 API 구현
홈 대시보드 API 구현
푸시 설정 API 구현
local / test / prod 설정 분리
Swagger / OpenAPI 문서화
JUnit 5 기반 테스트 코드 작성
GitHub Actions CI 구성
로컬 PostgreSQL 실행 준비
```

진행 예정 작업:

```text
Flyway 도입
Redis 캐시 도입
Dockerfile 작성
운영 배포
GitHub Actions CD 구성
실제 푸시 알림 발송 구현
```
