# 레벨 1: 요청을 알맞은 핸들러에게 보내기

안녕하세요. 첫 단계예요. 우리는 미니 스프링의 MVC를 만들기 시작합니다.

이 구간은 이미 한 번쯤 써 봤을 도메인이라, 새로운 무언가를 발명한다기보다 알던 것을 직접 손으로 다시 짜는 데 가까워요. 목적은 before -> 고통 -> after의 리듬을 몸에 익히는 거예요. 그리고 한 가지 약속: 저는 이 단계가 무슨 패턴인지 끝까지 말하지 않을 거예요. 마지막에 여러분이 스스로 "아, 이거였구나" 하고 만나는 게 이 실습의 전부거든요.

작업 공간은 `mini-spring/mvc` 모듈(`lab.spring.mvc`)입니다.

## 우리가 만들고 싶은 것

웹 프레임워크의 심장은 "들어온 요청을 누가 처리할지 고르는 일"이에요. 클라이언트가 `GET /users`를 보내면 사용자 핸들러가, `GET /orders`면 주문 핸들러가 응답해야 하죠.

그래서 우리의 첫 목표는 이겁니다. **요청 경로를 받아 그 요청을 처리할 핸들러를 찾아 주는 작은 디스패처를 만든다.**

핸들러는 일단 이 정도로 단순하게 둬요. 무엇으로 응답할지만 아는 객체면 됩니다.

```java
interface Handler {
    String handle(String path);
}
```

그리고 디스패처는 "어떤 경로를 어떤 핸들러가 맡는지" 등록해 두고, 요청이 오면 맞는 핸들러를 골라 줍니다. 가장 정직한 방법부터 가 봅시다.

```java
class DispatcherServlet {

    // 경로 패턴 -> 핸들러
    private final Map<String, Handler> registry = new LinkedHashMap<>();

    void register(String pattern, Handler handler) {
        registry.put(pattern, handler);
    }

    Handler getHandler(String path) {
        for (var entry : registry.entrySet()) {
            if (path.equals(entry.getKey())) {   // 등록한 경로와 정확히 같은가
                return entry.getValue();
            }
        }
        throw new IllegalStateException("핸들러 없음: " + path);
    }
}
```

`register("/users", ...)`, `register("/orders", ...)`로 등록하고, `getHandler("/users")`가 맞는 핸들러를 돌려주면 성공이에요. 작은 테스트로 초록불을 확인하세요.

**여기까지를 첫 커밋으로 남기세요.** 이게 우리의 before입니다. 동작하니까요.

## 그런데, 요구사항이 들어옵니다

현실은 "정확히 같은 경로"만으로 끝나지 않아요. 다음이 차례로 도착합니다.

1. 정적 파일은 `/static/`으로 시작하는 모든 경로를 정적 핸들러가 맡아야 해요. 정확히 일치가 아니라 **접두사 매칭**이죠.
2. 관리자 화면은 `/admin/*` 처럼 한 칸을 와일드카드로 받는 **패턴 매칭**이 필요해요.
3. 곧 누군가는 정규식으로 매칭하고 싶다고 할 거예요.

이 요구를 지금의 `getHandler`에 그대로 넣어 보세요. 십중팔구 저 `if (path.equals(...))` 한 줄이 이렇게 부풀어 오를 거예요.

```java
Handler getHandler(String path) {
    for (var entry : registry.entrySet()) {
        String pattern = entry.getKey();
        boolean matched;
        if (pattern.endsWith("/*")) {
            matched = path.startsWith(pattern.substring(0, pattern.length() - 2));
        } else if (pattern.endsWith("/")) {
            matched = path.startsWith(pattern);
        } else if (pattern.startsWith("regex:")) {
            matched = path.matches(pattern.substring(6));
        } else {
            matched = path.equals(pattern);
        }
        if (matched) return entry.getValue();
    }
    throw new IllegalStateException("핸들러 없음: " + path);
}
```

동작은 할 거예요. **여기까지를 두 번째 커밋으로 남기세요.** 이게 고통을 드러낸 상태입니다. 그리고 잠깐 멈춰서 이 코드를 노려보세요.

## 혹시 문제를 느꼈나요?

몇 가지가 마음에 걸릴 거예요.

- 매칭 "방식"이 하나 늘 때마다 디스패처의 핵심 메서드를 다시 열어 `else if`를 끼워 넣어야 해요. 건드리면 안 될 곳을 자꾸 건드리게 되죠.
- "경로를 순회하며 핸들러를 찾는 일"과 "경로를 어떻게 비교할지 정하는 일", 성격이 전혀 다른 두 가지가 한 메서드에 엉켜 있어요.
- 접두사 매칭만 따로 떼어 테스트하고 싶은데, 디스패처 전체를 세워야만 시험할 수 있어요.

이걸 푸는 길이 두세 가지 떠오를 거예요. 정답은 말하지 않을게요. 방향만 드립니다.

1. 변하는 부분은 딱 하나예요. "이 경로가 이 패턴에 맞는가?"를 판단하는 **비교 방식**. 이 한 조각만 따로 떼어내, 공통된 약속(예: `boolean matches(String pattern, String path)` 하나짜리 인터페이스) 뒤로 숨길 수 있다면 어떨까요. 디스패처는 "맞는지 봐 줘"라고 부탁만 하고, 어떻게 보는지는 모르게요.
2. 그 비교 방식 하나를 디스패처 **바깥에서 골라 끼워** 줄 수 있다면? 정확매칭에서 패턴매칭으로 갈아끼우는데도 디스패처 코드는 한 줄도 바뀌지 않겠죠.
3. 예전에 인증을 토큰 방식에서 세션 방식으로 바꿔 본 적 있나요? 그때 무엇을, 어디에, 어떻게 끼워 넣었는지 떠올려 보세요. 지금 필요한 구조와 똑 닮아 있을 거예요.

목표 지점은 분명해요. **새 매칭 방식을 추가할 때 디스패처의 핵심 메서드는 단 한 줄도 바뀌지 않는다.** 거기까지 리팩토링하면 그게 after입니다.

다 되면 세 번째 커밋을 남기고, 회고는 [docs/README-template.md](../README-template.md) 틀로 적어 두세요. 무엇이 어디로 옮겨졌고, 그 대가로 무엇이 늘었는지(클래스 수, 따라가야 할 간접 단계)까지 솔직하게요. 다음 단계 첫머리에서 우리가 무엇을 얻었는지 같이 되짚겠습니다.
