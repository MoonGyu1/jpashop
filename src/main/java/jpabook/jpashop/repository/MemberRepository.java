package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 스프링이 컴포넌트 스캔을 통해 스프링빈에 등록
@RequiredArgsConstructor
public class MemberRepository {

    //1. JPA 표준 어노테이션, 스프링이 엔티티매니저를 생성하여 주입해줌 (순수 JPA 사용시 엔티티매니저 팩토리에서 수동으로 꺼내서 주입해야 함)
//    @PersistenceContext
//    private EntityManager em;

    //2. 원래 위의 표준 어노테이션이 필요하나, Spring Data JPA에서 @Autowired 어노테이션도 지원해줌
    // -> 따라서 해당 어노테이션 생략 & @RequiredArgsConstructor 어노테이션으로 생성자 대체 가능
    private final EntityManager em;

    // EntityManagerFactory를 직접 주입받는 경우
    // @PersistenceUnit
    // private EntityManagerFactory emf;

    public void save(Member member) {
        // 영속성 컨텍스트에 넣는 순간에 (DB에 저장되기 전에)
        // member의 PK인 id 값을 채워줌
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
