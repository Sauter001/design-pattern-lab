// integration: 미니 스프링이 미니 DB를 @Repository로 감싸는 접점.
// 실제 Spring Data의 축소판. Proxy(커넥션 관리), Adapter(드라이버 추상화)가 등장할 자리.
dependencies {
    "implementation"(project(":mini-spring:container"))
    "implementation"(project(":mini-spring:mvc"))
    "implementation"(project(":mini-db:frontend"))
}
