// engine: 실행 층. storage 위에서만 동작한다 (frontend는 알지 않는다).
dependencies {
    "implementation"(project(":mini-db:storage"))
}
