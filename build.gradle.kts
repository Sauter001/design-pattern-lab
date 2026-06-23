// 루트 프로젝트는 소스를 갖지 않는다. 모든 서브모듈에 공통 설정만 주입한다.
subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    dependencies {
        // 문자열 표기: java 플러그인이 plugins{} 블록이 아니라 subprojects에서 적용되므로
        // 타입 세이프 접근자(testImplementation(...))가 생성되지 않는다. 문자열 표기는 항상 동작한다.
        "testImplementation"(platform("org.junit:junit-bom:5.11.4"))
        "testImplementation"("org.junit.jupiter:junit-jupiter")
        // Gradle 9부터 JUnit Platform launcher를 테스트 런타임 클래스패스에 명시해야 한다.
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
