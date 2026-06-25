package lab.appendix.flyweight;

/**
 * 부록 데모. 두 가지를 한 번에 보여준다.
 *
 * <ol>
 *   <li>손으로 짠 Glyph 플라이웨이트: 열한 자를 네 개의 공유본으로 그린다.</li>
 *   <li>JDK가 이미 깔아 둔 플라이웨이트: Integer.valueOf 캐시와 String 인터닝.</li>
 * </ol>
 *
 * <p>실행: {@code ./gradlew :appendix:flyweight:run} (또는 IDE에서 이 main을 실행).
 */
public final class FlyweightDemo {

    public static void main(String[] args) {
        handWrittenGlyphs();
        System.out.println();
        jdkFlyweights();
    }

    /** 1) 손으로 짠 플라이웨이트: 같은 글자는 같은 자형을 공유한다. */
    private static void handWrittenGlyphs() {
        System.out.println("== 손으로 짠 플라이웨이트: 자형 공유 ==");
        GlyphFactory factory = new GlyphFactory();
        String text = "MISSISSIPPI";

        int x = 0;
        for (char c : text.toCharArray()) {
            Glyph glyph = factory.get(c);   // 같은 글자면 같은 인스턴스
            glyph.drawAt(x, 0, 0x000000);   // 위치(extrinsic)만 바꿔 다시 그린다
            x += 10;
        }

        System.out.printf("글자 %d개를 그렸지만 자형은 %d개만 만들었다 (rasterize 호출 %d회).%n",
                text.length(), factory.distinctGlyphs(), factory.rasterizeCalls());

        Glyph s1 = factory.get('S');
        Glyph s2 = factory.get('S');
        System.out.println("factory.get('S') == factory.get('S') ? " + (s1 == s2));
    }

    /** 2) JDK가 이미 해 둔 플라이웨이트: 손으로 짤 일이 거의 없는 이유. */
    private static void jdkFlyweights() {
        System.out.println("== JDK가 이미 해 둔 플라이웨이트 ==");

        // Integer.valueOf: -128~127은 캐시해서 공유한다.
        Integer a = Integer.valueOf(127);
        Integer b = Integer.valueOf(127);
        System.out.println("valueOf(127) == valueOf(127) ? " + (a == b) + "  (캐시 범위 안, 공유본)");

        Integer c = Integer.valueOf(128);
        Integer d = Integer.valueOf(128);
        System.out.println("valueOf(128) == valueOf(128) ? " + (c == d) + "  (범위 밖, 매번 새 객체)");

        // String: 컴파일 타임 리터럴은 상수 풀에서 공유된다.
        String s1 = "kim";
        String s2 = "kim";
        System.out.println("\"kim\" == \"kim\" ? " + (s1 == s2) + "  (상수 풀 공유본)");

        String s3 = new String("kim");
        System.out.println("\"kim\" == new String(\"kim\") ? " + (s1 == s3) + "  (new는 새 객체)");
        System.out.println("\"kim\" == new String(\"kim\").intern() ? " + (s1 == s3.intern())
                + "  (intern으로 풀에 보내 공유)");
    }
}
