/**
 * 미니 스프링 mvc 층: 요청 처리.
 *
 * <p>의존 방향: container 위에서 동작한다.
 *
 * <p>등장 예정 패턴 (계획서 4-2):
 * <ul>
 *   <li>Front Controller (Facade 계열): DispatcherServlet (복잡한 내부를 한 창구로)</li>
 *   <li>Strategy: 핸들러 매핑 / ArgumentResolver (인증 방식 교체와 직결)</li>
 *   <li>Adapter: HandlerAdapter (서로 다른 핸들러를 한 인터페이스로)</li>
 *   <li>Chain of Responsibility: 필터 / 인터셉터 체인 (Spring 구조 그대로)</li>
 *   <li>Command: 핸들러 메서드 캡슐화 (요청을 객체로)</li>
 *   <li>Decorator: 요청/응답 래핑 (Proxy와 경계 비교)</li>
 *   <li>Observer: ApplicationEvent 발행/구독</li>
 * </ul>
 *
 * <p>확장 과제: Decorator(요청 래핑)와 Proxy(트랜잭션)를 둘 다 구현하고, 구조는 거의 같은데
 * 의도가 어떻게 갈리는가(Decorator=기능 추가, Proxy=접근 제어)를 주석으로 남긴다.
 */
package lab.spring.mvc;
