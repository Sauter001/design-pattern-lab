package lab.db.engine;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 토대 타입: 행 하나. 컬럼 이름에서 값으로 가는 작은 지도다.
 *
 * <p>이건 패턴이 아니라 그냥 데이터 운반체다. 여러 레벨의 before 코드가 이 모양을 가정한다
 * (예: 레벨 7 조인의 {@code l.get(key)}, 레벨 22 평가의 {@code evaluate(Row)}). "행을 어떤
 * 모양으로 만들지"에서 막히지 말라고 미리 깔아 둔다. 패턴이 되는 구조(쿼리, 조건 트리, 실행 계획)는
 * 이 위에 본인이 해당 레벨에서 올린다.
 */
public final class Row {

    private final Map<String, Object> values;

    public Row() {
        this.values = new LinkedHashMap<>();
    }

    private Row(Map<String, Object> values) {
        this.values = values;
    }

    /** 컬럼-값을 번갈아 주면 행을 만든다. 예: {@code Row.of("id", 1, "name", "kim")}. */
    public static Row of(Object... columnThenValue) {
        if (columnThenValue.length % 2 != 0) {
            throw new IllegalArgumentException("컬럼과 값이 짝이 안 맞아요: " + columnThenValue.length + "개");
        }
        Row row = new Row();
        for (int i = 0; i < columnThenValue.length; i += 2) {
            row.values.put((String) columnThenValue[i], columnThenValue[i + 1]);
        }
        return row;
    }

    public Object get(String column) {
        return values.get(column);
    }

    public Row put(String column, Object value) {
        values.put(column, value);
        return this;
    }

    public Set<String> columns() {
        return values.keySet();
    }

    /** 두 행을 한 행으로 합친다. 조인에서 쓸 만한 편의 메서드라 둔다(안 써도 된다). */
    public Row merged(Row other) {
        Map<String, Object> merged = new LinkedHashMap<>(this.values);
        merged.putAll(other.values);
        return new Row(merged);
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
