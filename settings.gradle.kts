rootProject.name = "design-pattern-lab"

// 층마다 1모듈. 의존 방향은 각 모듈 build.gradle.kts에서 강제한다.
include(
    "mini-db:storage",
    "mini-db:engine",
    "mini-db:frontend",
    "mini-spring:container",
    "mini-spring:mvc",
    "integration",
    "appendix:flyweight",
)
