package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

// 빈: 스프링 프레임워크에서 생명주기가 관리되는 객체, 의존성 주입을 통해 빈을 사용
@Repository // 컴포넌트 스캔의 대상 -> 자동으로 스프링 빈에 등록됨 (싱글톤)
public class MemberRepository {

    // EntityManager 주입 by 스프링부트
    // 해당 객체는 프록시 객체로 호출 시점에 스레드 트랜잭션 컨텍스트에 맞는 실제 EntityManager를 반환
    // 스레드마다 독립된 EntityManager 사용, 같은 스레드 내에는 동일한 EntityManager 제공
    // -> 스레드세이프하게 동작 가능
    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
