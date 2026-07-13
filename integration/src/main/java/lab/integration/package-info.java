/**
 * 접점(integration): 미니 스프링이 미니 DB를 감싸 쓰는 자리. 실제 Spring Data의 축소판이다.
 *
 * <p>의존 방향: container, mvc, mini-db frontend을 안다(레벨 25, 26에서 쓴다).
 *
 * <p>등장 예정 패턴 (계획서 2절 접점):
 * <ul>
 *   <li>Adapter: 드라이버 추상화 (레벨 25, JDBC Driver 구조. 미니 DB API를 균일한 약속 뒤로)</li>
 *   <li>Proxy: 커넥션 관리 (레벨 26, {@code @Transactional}/커넥션 풀. 접근과 생명주기 통제)</li>
 * </ul>
 *
 * <p>새 패턴을 발견하는 자리가 아니라(둘 다 레벨 4, 6에서 만난 패턴), 두 프로젝트를 잇는 데
 * 그걸 다시 쓰는 마무리 구간이다. 같은 패턴 다른 자리.
 */
package lab.integration;
