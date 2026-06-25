# 레벨 8: 같은 그림인데, 이번엔 운전대가 안에 있다

또 봐요. 여덟 번째예요. 지난 단계 끝에 제가 그랬죠. 이거랑 그림이 똑같이 생긴 걸 또 만들 건데, 운전대 쥔 손이 다르다고. 그거 하러 왔어요. 레벨 5하고 6이 그랬던 것처럼, 7하고 8도 한 쌍이에요.
그러니 레벨 7을 손에 쥔 채로 시작하세요. 자꾸 옆에 놓고 비교하라고 할 거예요.

## 지난 단계 돌아보기

레벨 7에서 우리는 조인하는 방법 여러 개를 따로 떼어 두고, 실행기는 그대로 둔 채 옵티마이저가 바깥에서 하나를 골라 끼웠어요. 실행기는 시키는 대로 조인만 했지, 어느 방법을 쓸지는 자기가 안 정했죠. 핵심은
그거였어요. **고르는 손이 바깥에 있다.**

오늘은 그림이 거의 똑같아요. 무언가를 여러 개로 쪼개 두고, 그중 무엇이냐에 따라 동작이 갈리는 것까지요. 그런데 딱 하나가 바뀝니다. **고르는 손이 바깥이 아니라 안에 있어요.** 무슨 말인지는 만들어 보면
알아요.

## 이번에 마주칠 상황

작업 공간을 또 옮길게요. 같은 미니 DB인데 이번엔 한 층 아래, `mini-db/storage` 모듈(`lab.db.storage`)이에요. 트랜잭션을 다룹니다.

트랜잭션은 살아가는 동안 몇 가지 상태를 지나가요. 처음엔 일하는 중(ACTIVE), 잘 끝나면 확정(COMMITTED), 엎으면 중단(ABORTED). 그리고 똑같은 호출이라도 지금 어느 상태냐에 따라 답이
달라요.

- 일하는 중엔 `write`가 먹혀요. 그런데 이미 확정했거나 중단한 트랜잭션에 `write`하면 막아야죠.
- `commit`은 일하는 중일 때만 의미가 있어요. 이미 확정한 걸 또 커밋하거나, 중단한 걸 커밋하려 들면 안 돼요.
- `abort`도 마찬가지고요. 확정한 건 못 되돌리고, 이미 중단한 건 다시 중단할 것도 없죠.

즉 **상태마다 같은 동작이 다르게 굴러가고, 어떤 동작은 트랜잭션을 다음 상태로 넘깁니다.** 이걸 만들어요.

## 가장 정직하게 먼저

상태를 enum 하나로 들고, 메서드마다 지금 상태를 따져서 갈래를 트는 거예요. 제일 정직하게요.

```java
class Transaction {

    enum State {ACTIVE, COMMITTED, ABORTED}

    private State state = State.ACTIVE;

    void write(Page p, byte[] data) {
        switch (state) {
            case ACTIVE -> log.append(p, data);                       // 정상
            case COMMITTED -> throw new IllegalStateException("이미 커밋됨");
            case ABORTED -> throw new IllegalStateException("이미 중단됨");
        }
    }

    void commit() {
        switch (state) {
            case ACTIVE -> {
                log.flush();
                state = State.COMMITTED;
            }   // 여기서 전이
            case COMMITTED -> throw new IllegalStateException("이미 커밋됨");
            case ABORTED -> throw new IllegalStateException("중단된 건 커밋 못 함");
        }
    }

    void abort() {
        switch (state) {
            case ACTIVE -> {
                log.undo();
                state = State.ABORTED;
            }      // 여기서도 전이
            case COMMITTED -> throw new IllegalStateException("커밋된 건 못 되돌림");
            case ABORTED -> { /* 이미 중단, 그냥 둠 */ }
        }
    }
}
```

동작은 해요. 상태 따라 갈래가 갈리고, `commit`이랑 `abort`에서 다음 상태로 넘어가죠. **여기까지 첫 커밋.** before예요.

## DB 코어팀이 옵니다

저장 엔진이 자라면서 코어팀이 요구를 들고 와요. 또 한 보따리죠.

- "2단계 커밋을 붙일 거예요. ACTIVE랑 COMMITTED 사이에 PREPARING(준비됨) 상태가 하나 더 생겨요. 준비 중엔 새 쓰기는 막고, 커밋 확정만 받게요."
- "세이브포인트도 곧 넣어요. 부분 롤백이 되는 상태가 또 하나 늘 거예요."
- "지금은 한 상태에서 뭐가 되고 뭐가 막히는지가 메서드마다 흩어져 있어요. 'PREPARING이면 뭘 할 수 있더라?'를 알려면 write, commit, abort를 다 열어 봐야 해요. 한곳에서 보고
  싶어요."

시키는 대로 PREPARING을 넣어 보세요. `write`의 switch도 열고, `commit`도 열고, `abort`도 열고, 앞으로 생길 `read`까지, 상태를 따지는 모든 메서드에
`case PREPARING`을 한 줄씩 더해야 해요. 하나라도 빠뜨리면 거기서 조용히 깨지고요. 그리고 그 한 곳이 꼭 제일 곤란할 때 터지죠.

```java
void write(Page p, byte[] data) {
    switch (state) {
        case ACTIVE -> log.append(p, data);
        case PREPARING -> throw new IllegalStateException("준비 중엔 못 씀");   // 여기 한 줄
        case COMMITTED -> throw new IllegalStateException("이미 커밋됨");
        case ABORTED -> throw new IllegalStateException("이미 중단됨");
    }
}
// commit에도, abort에도, read에도... 같은 case가 줄줄이 따라붙는다
```

**여기까지 두 번째 커밋.** 고통이 드러난 상태예요.

## 혹시 문제를 느꼈나요?

걸리는 데를 직접 꼽아 보세요. 그다음 제 거랑 맞춰 보고요.

- 상태가 하나 늘 때마다 동작 메서드를 전부 열어 `case`를 끼워야 해요. 정작 한 메서드가 하는 일은 단순한데, 상태 갈래가 그 앞을 다 차지했고요.
- "이 상태에서 뭐가 되나"가 한곳에 없어요. PREPARING이 뭘 허용하는지 알려면 write, commit, abort에 흩어진 `case PREPARING`을 다 그러모아야 하죠.
- 다음 상태로 넘기는 줄(`state = ...`)이 메서드 여기저기 박혀 있어요. 잘못된 전이를 막을 자리가 어딘지도 분명하지 않고요.

레벨 7이랑 나란히 놓고 보세요. 거기서도 여러 개 중 무엇이냐에 따라 동작이 갈렸죠. 푸는 모양도 거의 같을 거예요. 방향만요.

1. 변하는 건 "지금 어느 상태냐에 따라 같은 동작이 다르게 군다"예요. 상태 하나하나를 객체로 떼어내서, 그 상태에서 write/commit/abort가 어떻게 동작하는지를 그 객체 안에 모으면 어떨까요.
   트랜잭션은 "지금 내 상태" 객체한테 일을 넘기고요. 그러면 "PREPARING이 뭘 허용하나"가 PREPARING 객체 하나에 다 모여요.
2. 여기가 레벨 7이랑 갈리는 곳이에요. 잘 보세요. 레벨 7에선 **바깥(옵티마이저)이** 어느 걸 쓸지 골라 끼웠죠. 그런데 트랜잭션 상태는 바깥이 안 골라요. 일하는 중인 녀석이 `commit`을 받으면,
   스스로 "이제 난 확정이야" 하며 다음 상태로 넘어가요. **다음에 뭐가 될지를 자기가 정한다**는 거예요. 고르는 손이 바깥이 아니라 안에 있는 거죠. 코드 그림은 레벨 7과 쌍둥이인데, 이 한 끗이 둘을
   가릅니다.
3. 그렇게 짜 두면 새 상태(PREPARING)가 와도, 그 상태 객체 하나만 새로 만들어 "여기선 write를 막고, commit을 받으면 확정으로 넘긴다"를 그 안에 적으면 끝이에요. 기존 동작 메서드의 갈래를
   헤집을 일이 없어지고요. 잘못된 전이도 각 상태가 직접 거부하면 되고요.

이번 도착점은 이래요. **새 상태를 추가해도 기존 동작 메서드는 안 바뀐다. 한 상태에서 뭐가 되는지가 그 상태 한 곳에 모인다. 잘못된 전이는 그 상태가 거부한다.** 거기까지 가면 after예요.

레벨 7이랑 갈리는 그 한 끗이 통과 기준이고요. 새 상태(PREPARING) 하나 넣는데 기존 write/commit/abort의 갈래를 다시 여나요? 그리고 ACTIVE에서 `commit`을 받았을 때, "다음은 COMMITTED"라고 누가 정하나요, 바깥이에요 그 상태 자신이에요? 갈래를 안 열고 다음 상태를 자기가 정하면 after예요. 운전대가 안에 있는 거죠.

세 번째 커밋 찍고, 회고는 [docs/README-template.md](../README-template.md) 틀로. 이번엔 꼭 레벨 7 회고를 옆에 펴 놓고 한 줄 적으세요. "둘 다 여러 개 중 하나에 따라
동작이 갈렸는데, 고르는 손이 어디 있었나?" 그 답이 이 쌍의 전부예요. 비용도 적고요. 상태 클래스가 늘면서 "지금 무슨 상태지"를 따라가기가 얼마나 번거로워졌는지까지요.

## 다 끝났다면, 답을 맞춰 보세요

이젠 안 봐도 알죠. 끝냈으면 [정답과 이정표](answers/level-08.md) 열고, 아직이면 닫아요. 단 이번엔 꼭 레벨 7 정답을 옆에 펴요. 그림은 쌍둥이인데 운전대를 누가 쥐었나, 그 한 끗이 이 쌍의
전부거든요.
