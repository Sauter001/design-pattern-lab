# 부록: 마지막 한 조각

여긴 레벨이 아니라 부록이에요. 레벨 26까지 닫고 온 사람용이고요. 아직이면 하던 레벨로 돌아가요. 스포일러 때문은 아니에요. 여기서 까는 이름은 본문 어느 레벨의 정답도 아니라서요. 그냥 본문 스물두 개를 다 겪은 눈이라야, 이게 왜 부록으로 밀렸는지가 제대로 보여서 그래요.

## 왜 부록이냐면

GoF는 스물세 개인데 본문에서 다시 발견한 건 스물두 개예요. 하나가 비죠. **플라이웨이트(Flyweight).** 미니 DB에서도 미니 스프링에서도 자연스럽게 안 불렸어요. 원칙 3번 기억하죠. 도메인이 요구 안 하면 안 넣는다. 개수 채우겠다고 끼워 넣는 순간 망하니까요.

그리고 이 하나는 규칙을 바꿔서 다뤄요. 방금 이름부터 깠잖아요. 재발견 없고, 게이트 없고, 코드도 완성본으로 제공돼요. 이유는 단순해요. 이 패턴은 요즘 환경에서 본인 손으로 짤 일이 거의 없거든요. 플랫폼이 이미 다 해 놨어요. 그래서 여기 목적은 "짜 보기"가 아니라 **"이미 깔려 있는 걸 알아보기"**예요.

## 뭘 하면 되냐면

시키는 건 세 개예요. 오래 안 걸려요.

1. **설명부터 읽어요.** 본 설명 문서는 [appendix/flyweight/README.md](../../appendix/flyweight/README.md)예요. 한 줄 요약, intrinsic/extrinsic 상태 가르기, JDK가 이미 해 둔 자리, 언제 손으로 짜는지, 비용까지 전부 거기 있어요.
2. **코드를 열어 봐요.** 세 조각뿐이에요.
   - [`Glyph`](../../appendix/flyweight/src/main/java/lab/appendix/flyweight/Glyph.java): 공유되는 본체
   - [`GlyphFactory`](../../appendix/flyweight/src/main/java/lab/appendix/flyweight/GlyphFactory.java): 공유가 일어나는 길목
   - [`FlyweightDemo`](../../appendix/flyweight/src/main/java/lab/appendix/flyweight/FlyweightDemo.java): 돌려 보는 곳

   주석까지 읽어요. 설명의 절반이 주석에 있어요.
3. **돌려 봐요.**

   ```bash
   ./gradlew :appendix:flyweight:run     # 데모: 글자 11개를 자형 4개로 그린다
   ./gradlew :appendix:flyweight:test    # Integer 캐시, String 인터닝을 눈으로 확인
   ```

관찰 포인트는 하나예요. `equals`가 아니라 `==`. "같은 값이냐"가 아니라 "같은 인스턴스냐"가 이 패턴의 유일한 증거라서요. `Integer.valueOf(127)`끼리는 왜 같고 128부터는 왜 다른지, 그 악명 높은 함정의 정체까지 README에서 확인해요.

## 커밋은요?

없어요. before -> 고통 -> after 리듬은 본인이 만든 코드에나 있는 거지, 여긴 제공된 코드를 관찰하는 자리라서요. 굳이 남기고 싶으면 회고 한 줄 정도요. "내가 지금까지 짠 코드에 이 패턴이 정당해질 자리가 있었나." 아마 없었을 거예요. 그게 정상이고, 그게 이 부록의 결론이에요.

이걸로 스물세 개 전부예요. 진짜 끝이에요. ...가서 쉬어요. 저도 쉴 거고요.
