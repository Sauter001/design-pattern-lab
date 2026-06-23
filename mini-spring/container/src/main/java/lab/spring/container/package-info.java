/**
 * 미니 스프링 container 층: 빈 등록, 주입 (DI 컨테이너).
 *
 * <p>의존 방향: 최하층. mvc를 알지 않는다.
 *
 * <p>등장 예정 패턴 (계획서 4-1):
 * <ul>
 *   <li>Factory Method: 빈 생성 위임 (타입만 알고 생성은 위임)</li>
 *   <li>Abstract Factory: BeanFactory (빈 정의 -&gt; 빈 인스턴스 군)</li>
 *   <li>Prototype: 프로토타입 스코프 빈 (깊은 복사)</li>
 *   <li>Singleton: 싱글턴 스코프 빈 (컨테이너 관리 -&gt; 정당한 싱글턴)</li>
 *   <li>Proxy: @Transactional, AOP 부가기능 (이미 분석한 영역, 검증 모드)</li>
 *   <li>Mediator: 컴포넌트 이벤트 버스 [의도 삽입] (규모상 직접 호출이 더 깔끔할 수 있음)</li>
 * </ul>
 */
package lab.spring.container;
