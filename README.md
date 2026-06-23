# 디자인 패턴 Lab

GoF 23개 패턴을, 두 개의 도메인을 직접 만들면서 "필요해서 생겨나는" 방식으로 재발견하는 학습 lab.
한 단계씩 [docs/levels/](docs/levels/)의 문서를 따라가며 진행한다. 레벨 0(시작)부터 차례로 연다.

## 목표

1. 디자인 패턴의 필연성을 체득한다.
2. 끝나면 의미 있는 산출물(동작하는 미니 DB, 미니 스프링 골격)이 남는다.

격리 예제는 학습 신호가 깨끗하지만 산출물이 안 되고, 단일 도메인은 산출물은 되지만 패턴이 부족하다.
도메인을 둘로 나눠 서로의 사각지대를 메운다.

## 5대 원칙

1. 패턴부터 정하지 않는다. "이 변경 압력을 깔끔하게 풀려면?"에서 출발해, 다 만든 뒤 카탈로그와 대조해 재발견한다.
2. before → 고통 → after. 먼저 패턴 없이 동작시키고, 변경 요구로 고통을 측정한 뒤 리팩토링한다.
3. 끼워 맞추기를 경계한다. 도메인이 요구하지 않으면 넣지 않는다.
4. 안 쓴 이유도 기록한다.
5. 비용을 적는다. 패턴은 복잡도를 한 곳에서 다른 곳으로 옮기는 거래다.

## 구조

```
design-pattern-lab/
├── mini-db/          프로젝트 A: 쿼리 언어를 실행하는 미니 데이터베이스
│   ├── storage/      저장, 트랜잭션, 동시성   (lab.db.storage,   의존: 없음)
│   ├── engine/       실행                     (lab.db.engine,    의존: storage)
│   └── frontend/     파싱, 해석               (lab.db.frontend,  의존: engine)
├── mini-spring/      프로젝트 B: DI 컨테이너 + MVC
│   ├── container/    빈 등록, 주입            (lab.spring.container, 의존: 없음)
│   └── mvc/          요청 처리                (lab.spring.mvc,       의존: container)
├── integration/      접점: 미니 스프링이 미니 DB를 @Repository로 감쌈
│                                              (lab.integration, 의존: container, mvc, frontend)
└── appendix/
    └── flyweight/    부록: Flyweight 관찰     (lab.appendix.flyweight, 의존: 없음)
```

의존 방향은 각 모듈의 `build.gradle.kts`가 컴파일 타임에 강제한다. 예를 들어 storage에서 engine을
import하려 하면 빌드가 실패한다. 이 경계 유지 훈련이 개별 패턴 지식보다 값지다.

## 진행 방식

- 진행 순서와 23개 패턴 체크리스트: [docs/progress.md](unseen/progress.md)
- 패턴 한 단위를 끝낼 때마다 [docs/README-template.md](docs/README-template.md) 틀로 회고를 남긴다.
- before/after는 격리 폴더가 아니라 git 커밋 히스토리로 남긴다. 권장 커밋 리듬:
  1. 패턴 없이 동작하는 버전 커밋
  2. 변경 요구사항 추가로 고통 드러내는 커밋
  3. 리팩토링(패턴 도입) 커밋
  커밋 로그 자체가 "이 패턴이 왜 들어왔는가"의 서사가 된다.

## 빌드 / 테스트

```bash
./gradlew build      # 전체 컴파일 + 테스트
./gradlew test       # 테스트만
./gradlew projects   # 모듈 트리 확인
```

요구 환경: JDK 21 (Gradle toolchain이 자동 선택). 빌드 도구는 Gradle wrapper로 고정되어 별도 설치 불필요.

## 현재 상태

뼈대만 존재한다. 각 모듈은 층 책임과 등장 예정 패턴을 적은 `package-info.java`와 빌드 배선을
검증하는 스모크 테스트 하나만 갖는다. 패턴 구현은 아직 없다(학습자가 채운다).
