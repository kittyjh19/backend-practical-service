## ERD

본 ERD는 JPA Entity(User, Post)를 기준으로 테이블 구조를 도출하였으며,
User(1) : Post(N) 관계를 기반으로 게시글 작성자를 관리하도록 설계되었습니다.

- User는 다수의 Post를 작성할 수 있습니다.
- Post는 반드시 하나의 User(작성자)를 참조합니다.
- 주요 컬럼에 NOT NULL 제약조건을 적용하여 데이터 무결성을 보장합니다.

![ERD](./erd.png)
