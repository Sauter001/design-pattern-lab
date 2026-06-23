/**
 * 미니 DB frontend 층: 파싱, 해석.
 *
 * <p>의존 방향: engine 위에서 동작한다 (Visitor가 실행 계획 노드를 생성).
 *
 * <p>등장 예정 패턴 (계획서 3-3):
 * <ul>
 *   <li>Composite: 파스 트리 / AST 노드 (노드와 트리를 동일 취급)</li>
 *   <li>Interpreter: WHERE 절 표현식 평가 (GoF Interpreter 예제 원형)</li>
 *   <li>Visitor: AST 순회 (타입 체크, 실행 계획 생성, Composite와 세트)</li>
 * </ul>
 *
 * <p>확장 과제: 같은 AST에 Interpreter와 Visitor를 둘 다 올려, 새 연산 추가가 쉬운가(Visitor)
 * vs 새 노드 타입 추가가 쉬운가(Interpreter)의 표현 문제(Expression Problem)를 직접 측정한다.
 */
package lab.db.frontend;
