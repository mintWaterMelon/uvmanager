# UV Alert - UV 지수 알림 백엔드

## 1. Project Introduction

**UV Alert**는 공공 데이터를 활용하여 사용자의 지역별 자외선 지수(UV Index)를 확인하고, 설정한 기준 이상일 경우 알림을 제공하는 백엔드 애플리케이션입니다.

이 프로젝트는 Java와 Spring Boot를 사용하여 백엔드 개발의 핵심 개념을 단계별로 학습하고 구현하기 위한 포트폴리오 프로젝트입니다.

주요 목표는 다음과 같습니다.

- Spring Boot 기반 REST API 서버 구현
- 공공 API 연동
- 사용자별 지역 및 알림 설정 관리
- 스케줄링을 통한 정기적인 UV 지수 확인
- 테스트, 문서화, 배포까지 포함한 백엔드 프로젝트 완성

---

## 2. Main Features

현재 구현 예정인 주요 기능은 다음과 같습니다.

### 기본 기능

- 서버 상태 확인 API
- 지역별 UV 지수 조회 API
- 공공 UV 지수 API 연동
- UV 지수 등급 분류

### 사용자 기능

- 회원가입
- 로그인
- JWT 기반 인증
- 사용자 정보 관리

### 알림 기능

- 사용자별 알림 지역 설정
- 사용자별 UV 지수 알림 기준값 설정
- 알림 활성화 / 비활성화
- 매일 정해진 시간에 UV 지수 확인
- 기준값 이상일 경우 알림 발송

### 향후 기능

- 이메일 알림
- Firebase Push Notification
- Redis 캐싱
- 관리자용 모니터링 API

---

## 3. Technology Stack

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
| External API | 기상청 또는 공공데이터포털 UV 지수 API 예정 |
| Test | JUnit 5, Mockito 예정 |
| Deployment | TBD |
| CI/CD | GitHub Actions 예정 |

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
