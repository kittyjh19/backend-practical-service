# backend-practical-service

> 기존 Spring Boot 백엔드 서비스에 합류한 상황을 가정하여  
> 구조 분석, 유지보수, 성능 개선, **CI/CD 구축 및 배포까지 수행한 실무형 프로젝트**

본 프로젝트는 **기존 운영 중인 Spring Boot 기반 REST API 서비스 코드베이스를 대상으로**,  
신입 백엔드 개발자 관점에서  **서비스 구조 분석 → 유지보수/개선 → 배포 자동화(CI/CD)** 까지  
단계적으로 수행한 실무 분석 프로젝트입니다.


신규 서비스를 개발하는 것이 아니라,  
**기존 코드에 합류한 상황을 가정하여 환경 설정, 코드 구조 분석,  
REST API 스펙 정리, ERD 및 시퀀스 다이어그램 작성,  
유지보수 및 성능 개선, 배포 환경 구성**을 직접 수행했습니다.

---

## 🧭 프로젝트 진행 단계

본 프로젝트는 기존 백엔드 서비스 코드에 합류한 상황을 가정하여 아래와 같은 흐름으로 단계적으로 진행되고 있습니다.

- **Phase 1**: 서비스 구조 분석 및 문서화  
- **Phase 2**: 유지보수 및 성능 개선  
- **Phase 3**: 기능 확장 및 테스트 강화 
- **Phase 4**: CI/CD 구축 및 Docker 기반 배포 환경 구성
- **Phase 5**: Kubernetes 기반 배포 환경 구성 (예정)

---

## 📌 프로젝트 개요
- 기존 백엔드 서비스 코드 클론 및 로컬 개발 환경 구성
- REST API 구조 및 도메인 흐름 분석
- 도메인(Entity) 모델 기반 테이블 구조 분석 및 ERD 문서화
- 주요 API 요청 흐름에 대한 시퀀스 다이어그램 작성
- Hibernate SQL 로그 기반 버그 수정 및 성능 개선
- Jenkins + Docker Compose 기반 CI/CD 파이프라인 구축
- curl을 통한 배포 환경 API 검증


---

## 🛠 기술 스택
- Java 17
- Spring Boot 3.2.x
- Spring Data JPA
- MySQL 8.0
- Gradle (Wrapper) 
- JUnit 5 / MockMvc  
- Docker / Docker Compose  
- Jenkins

---

## 📂 프로젝트 구조 (분석 대상)

```text
backend-practical-service
├─ docs/
│  ├─ api/
│  │  └─ api-spec.md          # REST API 명세
│  ├─ erd/
│  │  ├─ erd.md               # ERD 설명
│  │  └─ erd.png              # ERD 이미지
│  └─ sequence/
│     ├─ create-post.png     
│     ├─ create-post.puml     # 게시글 생성 시퀀스 다이어그램
│     ├─ diagram.md           # 시퀀스 다이어그램 설명
│     ├─ update-post.png     
│     └─ update-post.puml     # 게시글 수정 시퀀스 다이어그램
├─ src/
│  └─ main/java/com/company/techblog
│     ├─ controller           # REST API 컨트롤러
│     ├─ service              # 비즈니스 로직
│     ├─ repository           # JPA Repository
│     ├─ domain               # 도메인 모델 (JPA Entity 포함)
│     └─ dto                  # 요청/응답 DTO
├─ Jenkinsfile                # Jenkins CI/CD 파이프라인 정의
├─ Dockerfile                 # Spring Boot 애플리케이션 이미지 빌드
├─ Dockerfile.jenkins         # Jenkins 전용 이미지 (Docker CLI 포함)
├─ docker-compose.yml         # 애플리케이션 실행 환경 (App + MySQL)
├─ docker-compose.jenkins.yml # CI/CD 환경 (Jenkins)
└─ README.md                  # 프로젝트 설명 문서
````

---

## 🧩 Phase 1. 서비스 분석 및 문서화

* 기존 코드 기반 REST API 엔드포인트 구조 파악
* Controller → Service → Repository 호출 흐름 분석
* **domain 패키지 내 JPA Entity(User, Post) 모델 분석**
* JPA 연관관계 기반 테이블 구조 정리 및 ERD 작성
* 주요 API(create, update)에 대한 시퀀스 다이어그램 작성
* Postman을 통한 API 동작 검증

---

## 🔧 Phase 2. 유지보수 및 성능 개선

기존 서비스 코드 분석 이후, 실제 운영 환경을 가정하여  
Hibernate SQL 로그를 기반으로 한 버그 수정과 성능 개선 작업을 수행했습니다.


### 1. 퇴사자 계정 접근 제어 버그 수정

* 퇴사한 사용자도 게시글 생성/수정/삭제가 가능한 문제를 확인
* UserStatus 기반으로 Service 레벨에서 퇴사자 접근 차단 로직 추가
* Postman을 통해 버그 재현 및 수정 후 동작 검증
* JUnit 기반 Service 테스트를 작성하여 재직자/퇴사자 케이스 자동 검증

### 2. 게시글 목록 조회 N+1 문제 분석 및 개선

* Hibernate SQL 로그 분석을 통해 게시글 목록 조회 시
  게시글 수만큼 작성자(users) 조회 쿼리가 반복 실행되는
  N+1 문제를 확인
* 원인: LAZY 로딩된 연관 엔티티를 DTO 변환 과정에서 반복 접근
* 해결: Fetch Join을 적용하여 게시글과 작성자 정보를 한 번의 JOIN 쿼리로 조회
* 결과: 게시글 목록 조회 시 실행되는 SQL 쿼리를 1 + N → 1로 개선하여 조회 성능 향상
* 개선 전/후 SQL 로그 비교를 통해 쿼리 수 감소 확인

---

## 🧪 Phase 3. 기능 확장 및 테스트 강화

기존 Phase 2에서 유지보수에 대한 검증을 완료한 이후, 신규로 추가된 기능(좋아요 / 인기 게시글)에 대해  
테스트 범위를 확장했습니다.

### 1. 게시글 좋아요(Like) 기능 확장

* 게시글에 대한 **좋아요 토글 기능** 구현
  * 좋아요 미존재 → 좋아요 추가
  * 이미 좋아요 상태 → 좋아요 취소
* 게시글 작성자는 본인 게시글에 좋아요를 누를 수 없도록 정책 적용
* 게시글 Entity에 좋아요 개수(`likeCount`)를 직접 관리하여 조회 성능 고려

### 2. 인기 게시글 조회 기능 추가

* 좋아요 개수(`likeCount`) 기준으로 게시글 내림차순 정렬
* 동일한 좋아요 수일 경우 최신 게시글 우선 정렬
* 상위 10개의 인기 게시글 조회 기능 구현

---

## 🧪 테스트 전략 및 구현

### 1. Service Layer 테스트 (JUnit)

Service 레벨에서는 비즈니스 규칙 검증을 중심으로 테스트를 작성했습니다.

> Phase 2에서는 유지보수 대상 로직에 대한 검증을 수행했고,  
> Phase 3에서는 신규 기능에 대한 테스트를 추가했습니다.

#### Phase 2 (유지보수 검증)
- 퇴사자는 게시글을 작성할 수 없음
- 재직자는 게시글 작성 가능

#### Phase 3 (기능 확장 검증)
- 좋아요 토글 기능 정상 동작
- 본인 게시글 좋아요 방지
- 인기 게시글이 좋아요 개수 기준으로 정렬되는지 검증

```text
PostServiceTest
├─ 퇴사자는_게시글을_작성할_수_없다              (Phase 2)
├─ 재직자는_게시글을_작성할_수_있다              (Phase 2)
├─ 좋아요는_토글_형식으로_동작한다               (Phase 3)
├─ 본인_게시글에는_좋아요를_누를_수_없다         (Phase 3)
└─ 인기게시글은_좋아요_개수_내림차순으로_정렬된다 (Phase 3)
````

→ **비즈니스 규칙 검증은 Service 테스트에서 집중 수행**

---

### 2. Controller Layer 테스트 (MockMvc)

API 레이어의 책임을 검증하기 위해 Controller 테스트를 추가했습니다.

**테스트 대상**

* 게시글 좋아요 토글 API
  `POST /api/posts/{postId}/likes`

**검증 내용**

* URL 매핑 정상 여부
* RequestBody → DTO 바인딩
* Service 호출 연결
* Response JSON 구조 검증

> Controller 테스트에서는
> 비즈니스 로직이 아닌 **API 계약(API Spec)** 검증에 집중했습니다.

---

## 🔧 트러블슈팅 & 문제 해결 경험

### 1. 인기 게시글 테스트 실패 원인 분석

**문제**

* 인기 게시글 테스트에서 기대한 결과와 실제 결과 불일치

**원인**

* `findTop10ByOrderByLikeCountDescCreatedAtDesc()`는
  데이터가 10개 미만일 경우 존재하는 데이터만 반환
* 테스트 작성 당시, 인기 게시글이 3개만 존재했기 때문에 결과 개수도 3개일 것이라 가정하고 테스트를 작성함
* 그러나 실제 구현은 `Top10` 조회였고, 테스트 데이터가 늘어나면서 기대값과 불일치 발생


**해결**

* 결과 리스트가 비어있지 않은지 검증
* 게시글 정렬 순서(좋아요 개수 내림차순)에 초점을 맞춰 테스트 수정

**배운 점**

> 테스트는 데이터 개수가 아니라
> **비즈니스 규칙과 정렬 조건을 검증해야 한다.**

---

### 2. Controller 테스트에서 발생한 MockMvc 오류

**문제**

* `post()` 요청에서 `.content()` 메서드가 동작하지 않는 오류 발생

**원인**

* `MockServerHttpRequest.post`를 잘못 import

**해결**

* `MockMvcRequestBuilders.post`로 import 수정

**배운 점**

> 테스트 코드에서는 잘못된 import 하나만으로도
> 테스트 환경 자체가 달라질 수 있다.

---

## 🚀 Phase 4. 배포 및 운영 환경 구성
기존 Phase 3까지 로컬 환경에서의 기능 검증을 완료한 이후, 실제 운영 환경을 가정하여 빌드·배포 과정을 자동화하고
애플리케이션을 컨테이너 기반으로 실행할 수 있도록 환경을 구성했습니다.

### 🔄 CI/CD 파이프라인 개요

```text
GitHub (push)
   ↓
Jenkins Pipeline
   ↓
Gradle bootJar
   ↓
Docker Image Build
   ↓
Docker Compose
   ├─ MySQL
   └─ Spring Boot App
```

* Jenkins는 Docker 컨테이너로 실행
* Jenkinsfile 기반 Declarative Pipeline 구성
* 민감 정보(DB 계정, Datasource 정보)는 Jenkins Credentials로 관리
* Jenkins Pipeline 실행 시 환경 변수로 주입하여 Git 저장소와 실행 환경을 분리

---

### 🧾 Jenkinsfile 요약

* Checkout
* Gradle JAR 빌드
* Docker Compose 기반 배포
* 배포 성공/실패 로그 분리

---

### 🐳 Docker Compose 역할 분리

* `docker-compose.yml`
  → 애플리케이션 실행용 (Spring Boot + MySQL)

* `docker-compose.jenkins.yml`
  → CI/CD 전용 (Jenkins + Docker CLI 포함)

---

## 🔐 환경 변수 및 보안 전략

| 항목                  | 방식                                              |
| ------------------- | ------------------------------------------------- |
| DB 계정 / 비밀번호      | Jenkins Credentials로 관리                         |
| 애플리케이션 환경 변수   | Jenkins Pipeline 실행 시 환경 변수로 주입           |
| GitHub              | 민감 정보 미커밋 (Credentials 완전 분리)            |


---

## 🔎 배포 환경 API 검증 (curl)

```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 3,
    "title": "Docker 성공",
    "content": "컨테이너에서 동작"
  }'
```

### 응답 예시

```json
{
  "id": 1,
  "authorId": 3,
  "title": "Docker 성공",
  "content": "컨테이너에서 동작",
  "likeCount": 0,
  "createdAt": "2026-01-23T17:25:57",
  "updatedAt": "2026-01-23T17:25:57"
}
```

---

## 🔧 트러블슈팅 경험

### 1. Gradle JAVA_COMPILER 오류 (WSL)

* 원인: JDK/JRE 인식 문제
* 해결: 로컬 설정과 CI 설정 분리

### 2. Jenkins에서 docker 명령어 인식 실패

* 원인: Jenkins 컨테이너에 Docker CLI 미설치
* 해결: Jenkins 전용 Dockerfile로 Docker CLI 포함

### 3. docker compose / docker-compose 혼용 문제

* Jenkins 환경에서는 `docker-compose`로 통일

### 4. 컨테이너 이름 충돌

* 기존 수동 실행 컨테이너와 충돌
* `docker-compose down` 선행으로 해결

### 5. 환경 변수 주입 방식 전환

* 원인: 로컬 `.env` 파일 의존으로 CI 환경에서 변수 누락
* 해결: Jenkins Credentials를 통해 환경 변수를 주입하도록 구조 개선


---

## ☸️ Phase 5. Kubernetes 기반 배포 환경 구성 (예정)
기존 Phase 4에서 Docker Compose 기반 배포를 완료한 이후,
컨테이너 오케스트레이션 환경으로 확장하기 위해 Kubernetes 기반 배포 환경을 구성할 예정입니다.

* Docker Compose → Kubernetes 전환
* Jenkins + Kubernetes 연계 배포

---
## 🧠 배운 점

### Phase 1

* 기존 서비스 코드베이스를 빠르게 파악하는 방법
* Controller–Service–Repository 구조의 책임 분리 이해

### Phase 2

* JPA 연관관계에서 N+1 문제가 발생하는 원인과 해결 방법
* Fetch Join을 통한 조회 성능 개선 경험
* 테스트 코드를 통한 비즈니스 로직 검증의 중요성

### Phase 3

* Service 테스트와 Controller 테스트의 역할 분리
* 비즈니스 로직은 Service 테스트에서 검증
* API 요청/응답 계약은 Controller 테스트에서 검증
* 테스트 실패 로그를 기반으로 원인을 추적하는 디버깅 경험
* “테스트를 통과시키는 코드”보다 “의도를 드러내는 테스트”의 중요성을 체감

### Phase 4
* Jenkins를 Docker 컨테이너로 구성하며 CI 서버 또한 하나의 애플리케이션이라는 관점을 학습
* Jenkinsfile 기반 Declarative Pipeline으로 빌드–배포 흐름을 코드로 관리
* Docker Compose를 활용해 애플리케이션 실행 환경과 CI/CD 환경을 분리 설계
* Docker CLI 의존성, 컨테이너 이름/네트워크 충돌 등 실제 배포 오류를 로그 기반으로 해결
* curl을 통해 배포된 컨테이너 환경에서 API 정상 동작을 직접 검증
* Jenkins Credentials를 활용해 민감 정보를 안전하게 관리하며 Git 저장소와 실행 환경을 완전히 분리

---

## 📎 참고 사항

* 본 프로젝트는 **실무 환경에서의 서비스 분석 및 유지보수 경험을 학습하기 위한 목적**으로 진행되었습니다.
* 서비스 코드의 원형은 학습용으로 제공된 기존 코드베이스를 기반으로 합니다.
* 분석, 문서화 및 개선 작업은 개인적으로 수행했습니다.

