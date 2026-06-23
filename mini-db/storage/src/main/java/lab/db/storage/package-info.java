/**
 * 미니 DB storage 층: 저장, 트랜잭션, 동시성.
 *
 * <p>의존 방향: 최하층. mini-db의 다른 층(engine, frontend)을 알지 않는다.
 *
 * <p>등장 예정 패턴 (계획서 3-1):
 * <ul>
 *   <li>Singleton: 버퍼 풀 매니저 (컨테이너 관리 싱글턴, 전역변수 싱글턴과의 차이 관찰)</li>
 *   <li>Proxy: 버퍼 풀 캐싱 (페이지 접근 가로채기, Decorator와 경계 비교)</li>
 *   <li>Memento: 트랜잭션 롤백 스냅샷 (ACID의 A)</li>
 *   <li>State: 트랜잭션 생명주기 (ACTIVE -&gt; COMMITTED/ABORTED, 자기 자신이 전이)</li>
 *   <li>Observer: WAL 로깅 (페이지 변경 -&gt; 로그 구독)</li>
 *   <li>Bridge: 저장 엔진 추상화 (인터페이스 x 파일/인메모리 구현, JDBC Driver 구조와 동형)</li>
 * </ul>
 *
 * <p>패턴은 도메인 구조 안에서 자연 발생시키고, 패턴별 폴더로 쪼개지 않는다.
 * before -&gt; 고통 -&gt; after는 git 커밋으로 남긴다.
 */
package lab.db.storage;
