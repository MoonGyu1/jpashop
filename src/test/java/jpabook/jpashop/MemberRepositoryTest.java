package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

// 스프링 애플리케이션 컨텍스트 초기화 & 스프링 기능을 테스트 코드에서 사용할 수 있게 함
@RunWith(SpringRunner.class) // .class는 클래스 메타정보 나타내는 Class 객체 반환
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository; // injection

    @Test
    // EntityManager를 통한 모든 데이터 변경은 트랜잭션 안에서 이루어져야 함
    @Transactional // 테스트케이스에 있는 경우 테스트 끝난 후 롤백 -> DB에 데이터 남아있지 않음
    @Rollback(false) // 롤백 없이 커밋
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

        // 같은 트랜잭션 안에서 저장 - 조회하므로 영속성 컨텍스트가 동일
        // 같은 영속성 컨텍스트 안에서는 ID(식별자)가 같으면 같은 엔티티로 인식
        // 같은 영속성 컨텍스트에 있으면 1차 캐시 내 관리하던 걸 그냥 반환함
        // 실제로 select 쿼리 안 나감
        Assertions.assertThat(findMember).isEqualTo(member); // true
        System.out.println("findMember == member: " + (findMember==member));
    }
}