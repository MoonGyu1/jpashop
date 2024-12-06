package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepositoryOld;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

// 스프링부트 통합 테스트
@RunWith(SpringRunner.class) // JUnit 실행 시 스프링과 엮어서 실행
@SpringBootTest // 스프링 띄운 상태로 테스트
@Transactional // 트랜잭션 내에서 테스트 실행 후 커밋 안 하고 롤백
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepositoryOld memberRepository;
    @Autowired EntityManager em;

    // 실제로는 INSERT 쿼리 나가지 않음
    // 이유: em.persist(member)가 될 때는 영속성 컨텍스트 내에만 존재
    // 트랜잭션이 커밋될 때 flush되면서 INSERT 쿼리 실행되면서 실제 DB에 저장됨
    @Test
//    @Rollback(false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);
//        em.flush(); // DB에 반영

        //then
        assertEquals(member, memberRepository.findOne(savedId)); // 같은 영속성 컨텍스트 내이므로 동일 객체 반환
    }

    @Test(expected = IllegalStateException.class) // 해당 메서드에서 IllegalStateException 예외 발생해야 함
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2);

        // 아래 코드를 @Test(expected = IllegalStateException.class)로 대체 가능
//        try {
//            memberService.join(member2); // 예외 발생
//        } catch(IllegalStateException e) {
//            return;
//        }

        //then
        fail("예외가 발생해야 한다.");
    }
}