# backend-practical-service

본 프로젝트는 **기존 운영 중인 Spring Boot 기반 REST API 서비스 코드베이스를 기반으로**,  
신입 백엔드 개발자 관점에서 **서비스 구조를 분석하고 문서화하는 실무 경험 프로젝트**입니다.

신규 서비스를 개발하는 것이 아니라,  
**기존 코드에 합류한 상황을 가정하여 환경 설정부터 코드 구조 파악,  
REST API 스펙 정리, ERD 및 시퀀스 다이어그램 작성까지의 과정을 단계적으로 진행하고 있습니다.**

> ⚠️ 본 프로젝트는 현재 **서비스 분석 및 환경 설정 단계 진행 중**이며,  
> 이후 유지보수, 기능 확장, 배포 단계로 순차적으로 확장할 예정입니다.

---

## 📌 프로젝트 개요
- 기존 백엔드 서비스 코드 클론 및 로컬 개발 환경 구성
- REST API 구조 및 도메인 흐름 분석
- **도메인(Entity) 모델 기반 테이블 구조 분석 및 ERD 문서화**
- 주요 API 요청 흐름에 대한 시퀀스 다이어그램 작성
- 신입 개발자 관점에서의 코드 구조 이해 및 개선 포인트 정리

본 프로젝트는 **기존 백엔드 서비스를 빠르게 이해하고 문서화하는 능력**에 초점을 맞추고 있습니다.

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

backend-practical-service  
├─ docs/  
│   ├─ api-spec.md              # REST API 명세  
│   ├─ erd.md                   # 테이블 구조 및 ERD  
│   ├─ sequence-diagram.md      # API 시퀀스 다이어그램 (PlantUML)  
│   └─ analysis-notes.md        # 코드 분석 및 개선 포인트 정리  
├─ src/  
│   └─ main/java/com/company/techblog  
│       ├─ controller           # REST API 컨트롤러  
│       ├─ service              # 비즈니스 로직  
│       ├─ repository           # JPA Repository  
│       ├─ domain               # 도메인 모델 (JPA Entity 포함)  
│       └─ dto                  # 요청/응답 DTO  
└─ README.md  

---

## 🧩 진행한 작업 (진행 중)
- 기존 코드 기반 REST API 엔드포인트 구조 파악
- Controller → Service → Repository 호출 흐름 분석
- **domain 패키지 내 도메인(Entity) 모델 분석**
- JPA 연관관계 기반 테이블 구조 정리 및 ERD 초안 작성
- 주요 API에 대한 시퀀스 다이어그램 작성
- 유지보수 관점에서 코드 가독성 및 구조 개선 포인트 정리

---

## 🧠 배운 점
*(서비스 분석 및 문서화 작업 진행 후 순차적으로 정리 예정)*

---

## 📎 참고 사항
- 본 프로젝트는 **실무 경험 학습을 목적으로 진행 중**이며,
- 서비스 코드의 원형은 학습용으로 제공된 기존 코드베이스를 기반으로 합니다.
- 분석, 문서화 및 개선 포인트 정리는 개인적으로 수행하고 있습니다.
