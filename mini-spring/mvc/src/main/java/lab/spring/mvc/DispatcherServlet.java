package lab.spring.mvc;

import java.util.LinkedHashMap;
import java.util.Map;

class DispatcherServlet {

    // 경로 패턴 -> 핸들러
    private final Map<String, Handler> registry = new LinkedHashMap<>();

    void register(String pattern, Handler handler) {
        registry.put(pattern, handler);
    }

    Handler getHandler(String path) {
        for (var entry : registry.entrySet()) {
            String pattern = entry.getKey();
            boolean matched;
            if (pattern.endsWith("/*")) {
                matched = path.startsWith(pattern.substring(0, pattern.length() - 2));
            } else if (pattern.endsWith("/")) {
                matched = path.startsWith(pattern);
            } else {
                matched = path.equals(pattern);
            }
            if (matched) {
                return entry.getValue();
            }
        }
        throw new IllegalStateException("핸들러 없음: " + path);
    }
}
