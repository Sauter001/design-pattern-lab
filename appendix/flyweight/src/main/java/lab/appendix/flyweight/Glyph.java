package lab.appendix.flyweight;

/**
 * 플라이웨이트(Flyweight) 본체.
 *
 * <p>오직 intrinsic state(공유 가능한 상태)만 들고 있다. 여기선 글자의 자형 비트맵이다.
 * 같은 글자 'S'는 문서에 1000번 나와도 비트맵은 하나면 된다. 그 하나를 모두가 공유한다.
 *
 * <p>extrinsic state(문맥마다 달라지는 상태: 위치, 색)는 절대 보관하지 않는다. 그릴 때
 * 인자로 받는다. 그래야 같은 Glyph 하나를 위치만 바꿔 어디에든 다시 쓸 수 있다. intrinsic을
 * 안에 가두고 extrinsic을 밖으로 빼는 이 분리가 플라이웨이트의 전부다.
 */
public final class Glyph {

    private final char symbol;    // intrinsic: 어떤 글자인가
    private final int[] bitmap;   // intrinsic: 그 글자의 자형 비트맵 (무겁다고 가정)

    Glyph(char symbol, int[] bitmap) {
        this.symbol = symbol;
        this.bitmap = bitmap;
    }

    /**
     * 이 자형을 화면 (x, y)에 color로 그린다.
     *
     * <p>x, y, color가 바로 extrinsic state다. Glyph가 보관하지 않고 호출자가 매번 건넨다.
     * 실제 구현이라면 bitmap을 (x, y)에 color로 블릿하겠지만, 여기선 한 줄 출력으로 대신한다.
     */
    public void drawAt(int x, int y, int color) {
        System.out.printf("'%c' @(%d, %d) color=#%06X, bitmap=%dB (공유본)%n",
                symbol, x, y, color, bitmap.length);
    }

    public char symbol() {
        return symbol;
    }

    int bitmapSize() {
        return bitmap.length;
    }

    @Override
    public String toString() {
        return "Glyph('" + symbol + "', bitmap=" + bitmap.length + "B)";
    }
}
