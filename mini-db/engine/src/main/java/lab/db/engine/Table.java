package lab.db.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * 토대 타입: 이름 붙은 테이블. 행들의 모음이다.
 *
 * <p>{@link Row}와 마찬가지로 패턴이 아니라 운반체다. 레벨 7(조인) 같은 곳의 before가
 * {@code table.rows()}를 가정한다. 인덱스, 스키마 검증 같은 건 일부러 뺐다. 필요한 레벨에서
 * 본인이 얹는다.
 */
public final class Table {

    private final String name;
    private final List<Row> rows;

    public Table(String name) {
        this(name, new ArrayList<>());
    }

    public Table(String name, List<Row> rows) {
        this.name = name;
        this.rows = rows;
    }

    public String name() {
        return name;
    }

    public List<Row> rows() {
        return rows;
    }

    public Table add(Row row) {
        rows.add(row);
        return this;
    }

    public int size() {
        return rows.size();
    }

    @Override
    public String toString() {
        return "Table(" + name + ", " + rows.size() + " rows)";
    }
}
