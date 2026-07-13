package lab.db.storage;

/**
 * 토대 타입: 저장 페이지 하나. 바이트 한 덩어리다.
 *
 * <p>패턴이 아니라 운반체다. 레벨 8(트랜잭션), 레벨 13(롤백 스냅샷) 같은 곳의 before가 이 모양을
 * 가정한다. "스냅샷을 떠서 되돌린다" 같은 구조는 본인이 그 레벨에서 이 위에 올린다. 여긴 페이지
 * 데이터만 들고 있다.
 *
 * <p>참고: 레벨 8 before에 나오는 {@code log}(WAL)는 토대가 아니라 패턴 주제다(레벨 14에서
 * 본격적으로 다룬다). 그 전까지는 최소 스텁으로 두고 가면 된다.
 */
public final class Page {

    private final int id;
    private byte[] data;

    public Page(int id) {
        this(id, new byte[0]);
    }

    public Page(int id, byte[] data) {
        this.id = id;
        this.data = data.clone();
    }

    public int id() {
        return id;
    }

    /** 페이지 내용을 읽는다. 복사본을 준다(밖에서 바꿔도 페이지는 안 변한다). */
    public byte[] read() {
        return data.clone();
    }

    /** 페이지 내용을 통째로 바꾼다. */
    public void write(byte[] data) {
        this.data = data.clone();
    }

    @Override
    public String toString() {
        return "Page#" + id + "(" + data.length + "B)";
    }
}
