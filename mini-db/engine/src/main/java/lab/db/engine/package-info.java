/**
 * 미니 DB engine 층: 실행.
 *
 * <p>의존 방향: storage 위에서만 동작한다. frontend를 알지 않는다.
 *
 * <p>등장 예정 패턴 (계획서 3-2):
 * <ul>
 *   <li>Strategy: 조인 알고리즘 교체 (NLJ/Hash/Sort-Merge, 외부 옵티마이저가 갈아끼움)</li>
 *   <li>Iterator: Volcano 실행 모델 (연산자 next() 체인, 실제 DB 실행 모델 그 자체)</li>
 *   <li>Factory: 실행 계획 노드(연산자) 생성 (계획 트리 -&gt; 실행 객체)</li>
 *   <li>Chain of Responsibility: 옵티마이저 규칙 적용 (규칙이 충분히 많을 때만, 적으면 안 쓰고 기록)</li>
 *   <li>Builder: 쿼리 빌더 API (선택적 파라미터 폭발 지점)</li>
 *   <li>Template Method: 연산자 실행 골격 (open/next/close, Strategy와 자리 겹침)</li>
 * </ul>
 */
package lab.db.engine;
