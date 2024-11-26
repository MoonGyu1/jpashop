package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 스프링이 컴포넌트 스캔을 통해 스프링빈에 등록
public class MemberRepository {

    @PersistenceContext // JPA 표준 어노테이션, 스프링이 엔티티매니저를 생성하여 주입해줌 (순수 JPA 사용시 엔티티매니저 팩토리에서 수동으로 꺼내서 주입해야 함)
    private EntityManager em;

    // EntityManagerFactory를 직접 주입받는 경우
    // @PersistenceUnit
    // private EntityManagerFactory emf;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        // em.find(type, PK)
        return em.find(Member.class, id); // JPA가 제공하는 find 메서드
    }

    public List<Member> findAll() {
        // JPQL은 엔티티 객체를 대상으로 쿼리
        // createQuery(JPQL, return type)
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
