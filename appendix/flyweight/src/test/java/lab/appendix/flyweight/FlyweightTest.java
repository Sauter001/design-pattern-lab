package lab.appendix.flyweight;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * 플라이웨이트가 정말 공유되는지, 그리고 JDK가 이미 같은 짓을 하고 있는지 눈으로 확인한다.
 * == 비교(객체 동일성)를 일부러 쓴다. equals가 아니라 "같은 인스턴스냐"가 이 패턴의 증거라서.
 */
class FlyweightTest {

    @Test
    void 같은_글자는_같은_Glyph_인스턴스를_공유한다() {
        GlyphFactory factory = new GlyphFactory();
        assertSame(factory.get('S'), factory.get('S'), "같은 글자는 같은 공유본이어야 한다");
    }

    @Test
    void 글자가_많아도_자형은_종류_수만큼만_만든다() {
        GlyphFactory factory = new GlyphFactory();
        String text = "MISSISSIPPI";   // 11자, 서로 다른 글자는 4종(M, I, S, P)
        for (char c : text.toCharArray()) {
            factory.get(c);
        }
        assertEquals(4, factory.distinctGlyphs(), "서로 다른 자형은 4개여야 한다");
        assertEquals(4, factory.rasterizeCalls(), "자형 생성은 글자당 한 번뿐이어야 한다");
    }

    @Test
    void JDK_Integer_캐시는_범위_안에서만_공유된다() {
        assertSame(Integer.valueOf(127), Integer.valueOf(127), "-128~127은 캐시 공유본");
        assertNotSame(Integer.valueOf(128), Integer.valueOf(128), "범위 밖은 매번 새 객체");
    }

    @Test
    void JDK_String_리터럴은_상수_풀에서_공유된다() {
        String literal = "kim";
        assertSame(literal, "kim", "리터럴은 상수 풀 공유본");
        assertNotSame(literal, new String("kim"), "new String은 새 객체");
        assertSame(literal, new String("kim").intern(), "intern은 풀로 보내 공유");
    }
}
