/**
 * 부록: Flyweight.
 *
 * <p>직접 구현이 목적이 아니라, 왜 현대 애플리케이션에선 손으로 짤 일이 거의 없는가를 관찰한다.
 * <ul>
 *   <li>Integer.valueOf()의 -128~127 캐시 분석 (JDK가 이미 해놓은 Flyweight)</li>
 *   <li>String.intern(), 컴파일 타임 상수 풀 확인</li>
 *   <li>intrinsic state(공유 가능) vs extrinsic state(문맥 의존) 분리 비교</li>
 *   <li>Flyweight가 정당해지는 극단 상황 정리 (임베디드, 게임 파티클 수백만, 모바일 글리프 비트맵)</li>
 * </ul>
 *
 * <p>의존 방향: 독립. 다른 모듈을 알지 않는다.
 */
package lab.appendix.flyweight;
