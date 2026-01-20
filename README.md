# backend-practical-service

> 기존 Spring Boot 백엔드 서비스에 합류한 상황을 가정하여  
> 구조 분석, 유지보수, 성능 개선, 배포까지 단계적으로 수행하는 실무형 프로젝트

본 프로젝트는 **기존 운영 중인 Spring Boot 기반 REST API 서비스 코드베이스를 대상으로**,  
신입 백엔드 개발자 관점에서 **서비스 구조를 분석하고 유지보수·개선을 수행한 실무 분석 프로젝트**입니다.

신규 서비스를 개발하는 것이 아니라,  
**기존 코드에 합류한 상황을 가정하여 환경 설정, 코드 구조 분석,  
REST API 스펙 정리, ERD 및 시퀀스 다이어그램 작성,  
유지보수 및 성능 개선 작업을 단계적으로 수행**하고 있습니다.

---

## 🧭 프로젝트 진행 단계

본 프로젝트는 기존 백엔드 서비스 코드에 합류한 상황을 가정하여 아래와 같은 흐름으로 단계적으로 진행되고 있습니다.

- **Phase 1**: 서비스 구조 분석 및 문서화  
- **Phase 2**: 유지보수 및 성능 개선  
- **Phase 3**: 기능 확장 및 테스트 강화 (예정)  
- **Phase 4**: 배포 및 운영 환경 구성 (예정)

---

## 📌 프로젝트 개요
- 기존 백엔드 서비스 코드 클론 및 로컬 개발 환경 구성
- REST API 구조 및 도메인 흐름 분석
- **도메인(Entity) 모델 기반 테이블 구조 분석 및 ERD 문서화**
- 주요 API 요청 흐름에 대한 시퀀스 다이어그램 작성
- 신입 개발자 관점에서의 코드 구조 이해 및 개선 포인트 정리

본 프로젝트는 **기존 백엔드 서비스를 빠르게 이해하고,  
실제 유지보수 및 개선 작업까지 수행할 수 있는 역량을 기르는 것**에 초점을 맞추고 있습니다.

---

## 🛠 기술 스택
- Java 17
- Spring Boot 3.2.x
- Spring Data JPA
- MySQL 8.0
- Gradle (Wrapper)
- Spring Boot Actuator

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
└─ README.md
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

## 🧪 Phase 3. 기능 확장 및 테스트 강화 (예정)

* Controller 레벨 테스트 및 API 단위 테스트 추가
* 예외 처리 공통화 및 응답 포맷 정리
* 테스트 커버리지 개선 및 회귀 테스트 환경 구성
* 신규 요구사항 기반 기능 확장

---

## 🚀 Phase 4. 배포 및 운영 환경 구성 (예정)

* Docker 기반 로컬/운영 환경 구성
* AWS EC2 + RDS 배포 환경 구축
* 실행 가능한 JAR 빌드 및 배포 자동화
* Actuator 기반 헬스 체크 및 모니터링 적용
* curl을 통한 배포 환경 API 검증

---

## 🧠 배운 점

### Phase 1

* 기존 서비스 코드베이스를 빠르게 파악하는 방법
* Controller–Service–Repository 구조의 책임 분리 이해

### Phase 2

* JPA 연관관계에서 N+1 문제가 발생하는 원인과 해결 방법
* Fetch Join을 통한 조회 성능 개선 경험
* 테스트 코드를 통한 비즈니스 로직 검증의 중요성

---

## 📎 참고 사항

* 본 프로젝트는 **실무 환경에서의 서비스 분석 및 유지보수 경험을 학습하기 위한 목적**으로 진행되었습니다.
* 서비스 코드의 원형은 학습용으로 제공된 기존 코드베이스를 기반으로 합니다.
* 분석, 문서화 및 개선 작업은 개인적으로 수행했습니다.

