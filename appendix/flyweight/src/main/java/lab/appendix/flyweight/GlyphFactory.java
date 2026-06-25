package lab.appendix.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * 플라이웨이트 팩토리. Glyph를 새로 만들지, 이미 만든 걸 돌려줄지를 여기서 정한다.
 *
 * <p>핵심은 단 하나다. 같은 글자를 두 번 요청하면 같은 Glyph 인스턴스를 돌려준다. 그래서
 * 'M', 'I', 'S', 'P' 네 자형으로 "MISSISSIPPI" 열한 자를 다 그린다. 공유가 일어나는 길목이
 * 바로 이 팩토리다. 호출자가 직접 new Glyph(...)를 하면 공유가 깨지므로, 생성을 팩토리에
 * 가두는 게 이 패턴의 약속이다.
 */
public final class GlyphFactory {

    private final Map<Character, Glyph> pool = new HashMap<>();
    private int rasterizeCalls = 0;   // 자형을 실제로 만든 횟수 (관찰용)

    /** 글자에 해당하는 Glyph를 돌려준다. 풀에 없으면 그때 한 번만 만든다. */
    public Glyph get(char symbol) {
        return pool.computeIfAbsent(symbol, this::rasterize);
    }

    private Glyph rasterize(char symbol) {
        rasterizeCalls++;
        // 자형 비트맵을 만드는 건 비싸다고 가정한다(폰트 힌팅, 안티앨리어싱 등). 그 비싼 일을
        // 글자당 딱 한 번만 치른다. 그게 플라이웨이트가 아끼는 비용이다.
        int[] bitmap = new int[16 * 16];   // 16x16 자형이라 치자
        return new Glyph(symbol, bitmap);
    }

    /** 지금까지 풀에 잡힌 서로 다른 Glyph 수. */
    public int distinctGlyphs() {
        return pool.size();
    }

    /** 자형을 실제로 만든 횟수. distinctGlyphs()와 같아야 한다(글자당 한 번). */
    public int rasterizeCalls() {
        return rasterizeCalls;
    }
}
