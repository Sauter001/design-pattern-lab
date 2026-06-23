package lab.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** toolchain과 빌드 배선이 그린인지 확인하는 자명한 스모크 테스트. 패턴 구현 전 자리채움. */
class SmokeTest {

    @Test
    void moduleBuilds() {
        assertTrue(true, "integration 모듈이 컴파일되고 테스트가 실행된다");
    }
}
