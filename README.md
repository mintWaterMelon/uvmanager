# UV Alert

UV Alert는 기상청 공공데이터를 활용하여 사용자의 위치를 기준으로 날씨, 온도, 자외선지수, 미세먼지 관련 정보를 확인할 수 있는 모바일 앱 프로젝트입니다.

백엔드는 Java Spring Boot로 구현하고, 모바일 앱은 Expo React Native로 구현합니다.

---

## 1. Project Introduction

이 프로젝트는 사용자가 설정한 지역의 시간대별 날씨 정보와 자외선 정보를 제공하는 앱입니다.

홈 화면에서는 오늘, 내일, 모레의 정보를 낮/밤 모드로 확인할 수 있습니다.

주요 데이터는 기상청 공공데이터 API를 통해 조회합니다.

---

## 2. Main Features

### Home

- 현재 시간 표시
- 현재 설정 위치 표시
- 오늘 / 내일 / 모레 선택
- 낮 / 밤 모드 전환
- 시간대별 대시보드 표시
  - 날씨 및 온도
  - 자외선 지수
  - 미세먼지 표시
- 현재 시간대 강조
- 날씨, 자외선, 대기 상태에 따른 배경 테마 변경
- 상황별 조언 박스 표시

### UV Information

- SPF와 PA 설명
- 자외선지수 단계 설명
- 올바른 선크림 사용법 안내
- 자외선 보호 체크리스트 제공

### Settings

- 위치 설정
- 푸시 설정
- 사용방법
- 라이센스 정보

### Location Settings

- 지역명 검색
- 행정구역 코드 검색
- 선택한 지역을 기본 위치로 저장
- 단기예보 격자 좌표 사용

### Push Settings

- 자외선 알림 ON/OFF
- 자외선 알림 기준값 설정
- 미세먼지 알림 ON/OFF
- 알림 시간 설정

> 실제 푸시 발송 기능은 TBD입니다.

---

## 3. Technology Stack

### Backend

| Category | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5.14 |
| Build Tool | Gradle |
| Build Script | Groovy |
| Packaging | Jar |
| Database | TBD |
| ORM | Spring Data JPA 예정 |
| Authentication | Spring Security + JWT 예정 |
| API Documentation | Swagger / OpenAPI 예정 |
| Test | JUnit 5, Mockito 예정 |
| Deployment | TBD |
| CI/CD | GitHub Actions 예정 |

### Mobile

- Expo
- React Native
- TypeScript
- Expo Router
- React Native Safe Area Context

### External API

- 기상청 자외선지수 API
- 기상청 단기예보 조회서비스
- 기상청 대기정체지수 API

---

## 4. Architecture Diagram

TBD

예정 아키텍처는 다음과 같습니다.

```text
Client
  ↓
Spring Boot REST API Server
  ↓
Service Layer
  ↓
Repository Layer
  ↓
Database

Spring Boot Scheduler
  ↓
External UV Index API
  ↓
Notification Service
```

추후 프로젝트 구조가 확정되면 아키텍처 다이어그램을 추가할 예정입니다.

---

## 5. ERD

TBD

예정 엔티티는 다음과 같습니다.

```text
User
NotificationSetting
Location
UvIndex
```

예상 관계는 다음과 같습니다.

```text
User 1 ─── 1 NotificationSetting
Location 1 ─── N NotificationSetting
Location 1 ─── N UvIndex
```

추후 데이터베이스 설계가 확정되면 ERD 이미지를 추가할 예정입니다.

---

## 6. API Documentation Link

TBD

Swagger / OpenAPI 문서화 적용 후 아래 위치에 API 문서 링크를 추가할 예정입니다.

```text
http://localhost:8080/swagger-ui/index.html
```

---

## 7. Local Execution Method

### 1. Repository Clone

```bash
git clone TBD
cd uv-alert
```

### 2. Java Version Check

이 프로젝트는 Java 17을 사용합니다.

```bash
java -version
```

예상 버전:

```text
java 17
```

### 3. Build

Windows 환경:

```bash
gradlew.bat build
```

Mac / Linux 환경:

```bash
./gradlew build
```

### 4. Run

Windows 환경:

```bash
gradlew.bat bootRun
```

Mac / Linux 환경:

```bash
./gradlew bootRun
```

### 5. Server Check

서버 실행 후 아래 주소로 접속합니다.

```text
http://localhost:8080
```

Health Check API는 추후 추가 예정입니다.

```text
GET /health
```

---

## 8. Setting Environment Variables

TBD

공공 API Key, 데이터베이스 접속 정보, JWT Secret 등 민감한 정보는 환경 변수로 관리할 예정입니다.

예정 환경 변수는 다음과 같습니다.

```text
UV_API_KEY=TBD
UV_API_BASE_URL=TBD

DB_URL=TBD
DB_USERNAME=TBD
DB_PASSWORD=TBD

JWT_SECRET=TBD
JWT_EXPIRATION=TBD
```

실제 API Key나 비밀번호는 GitHub에 업로드하지 않습니다.

추후 `.env` 또는 `application-local.yml` 설정 방식을 정리할 예정입니다.

---

## 9. Test Method

TBD

테스트 코드는 JUnit 5와 Mockito를 사용하여 작성할 예정입니다.

전체 테스트 실행:

Windows 환경:

```bash
gradlew.bat test
```

Mac / Linux 환경:

```bash
./gradlew test
```

예정 테스트 항목:

- UV 지수 등급 분류 테스트
- 회원가입 테스트
- 로그인 테스트
- 알림 설정 저장 테스트
- 외부 API Client 테스트
- Scheduler 동작 테스트

---

## 10. Distribution Method

TBD

추후 Docker와 클라우드 서버를 활용하여 배포할 예정입니다.

예정 배포 방식:

```text
1. Spring Boot 애플리케이션 Jar 빌드
2. Docker 이미지 생성
3. 서버에 Docker 컨테이너 실행
4. 환경 변수 설정
5. 배포 서버에서 API 동작 확인
```

예정 Docker 실행 흐름:

```bash
./gradlew build
docker build -t uv-alert .
docker run -p 8080:8080 uv-alert
```

CI/CD는 GitHub Actions를 사용하여 자동화할 예정입니다.

---

## 11. What I Learned Through This Project

이 프로젝트를 통해 다음 내용을 학습하고 정리하는 것을 목표로 합니다.

### Spring Boot 기본 구조

- Spring Boot 프로젝트 생성 방법
- Gradle 기반 프로젝트 구조
- Controller, Service, Repository 계층 분리
- REST API 설계 방식

### 외부 API 연동

- 공공 API 요청 방식
- API Key 관리 방법
- 외부 API 응답을 내부 DTO로 변환하는 방법
- 외부 API 장애 상황 처리 방법

### 데이터베이스와 JPA

- Entity 설계
- Repository 사용
- 사용자와 알림 설정 간의 관계 설계
- 데이터 저장과 조회 흐름

### 인증과 보안

- Spring Security 기본 구조
- JWT 기반 로그인 처리
- 인증이 필요한 API 보호
- 비밀번호 암호화

### 스케줄링

- `@Scheduled` 사용 방법
- 정해진 시간마다 UV 지수 확인
- 조건에 따라 알림 발송 처리

### 테스트

- 단위 테스트와 통합 테스트의 차이
- JUnit 5 사용법
- Mockito를 이용한 Mock 테스트
- 외부 API 의존성을 분리하는 방법

### 배포

- Jar 파일 빌드
- Docker 이미지 생성
- 환경 변수 기반 설정 관리
- GitHub Actions를 활용한 CI/CD 자동화

---

## Project Status

현재 상태:

```text
Project Created
Initial Spring Boot Setup
Git Repository Connected
Push Not Yet Completed
```

진행 예정:

```text
1. Initial Commit
2. Health Check API 구현
3. 기본 패키지 구조 정리
4. UV Index API 설계
5. 공공 API 연동
6. DB 연동
7. 알림 설정 기능 구현
8. 인증 기능 구현
9. 테스트 코드 작성
10. 배포
```
