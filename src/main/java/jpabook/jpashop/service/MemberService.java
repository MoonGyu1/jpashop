package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 스프링 빈으로 등록
// 변경을 포함한 JPA 로직은 트랜잭션 안에서 실행되야 함 (lazy loading 등 가능)
// 클래스 레벨에 어노테이션 적용하면 public method에 적용됨
// jakarta의 Transactional 어노테이션도 있지만 지원기능이 많은 스프링의 어노테이션 쓰는 것 추천
@Transactional(readOnly = true) // 조회 시 JPA에서 성능 최적화 (dirty checking 안 함, DBMS에 따른 최적화 등)
@RequiredArgsConstructor
public class MemberService {

    //Autowired: 스프링빈에 등록된 리포지토리를 인젝션 해줌

    //1. field injection
    //단점: 필드이기 때문에 테스트코드 등에서 변경 불가
//    @Autowired
//    private MemberRepository memberRepository;

    //2. setter injection
    //장점: 테스트코드 작성 시 mock repository 주입 가능
    //단점: (실질적으로 해당 코드를 직접 호출할 필요가 없음에도) 해당 코드를 호출하는 부분이 있으면 런타임에 변경되어버림
//    private MemberRepository memberRepository;
//
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //3. 생성자 injection
    //장점
    // 1)생성자이므로 맨 처음 한 번만 호출됨, 중간에 리포지토리 변경 불가
    // 2)테스트코드 작성 시 인스턴스 생성 시점에 인자로 필요하므로 의존관계 명확하게 파악 가능
//    private final MemberRepository memberRepository; // 변경될 일 없는 값이고, 컴파일 시점에 파악하기 쉽도록 final 키워드 넣는 것 추천

    //생성자가 1개인 경우 어노테이션 생략해도 스프링에서 자동으로 인젝션 해줌
//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //4. lombok - @RequiredArgsConstructor
    //final이 있는 필드를 기반으로 생성자를 만들어줌
    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional // 데이터 변경 있는 경우 readOnly = false
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    // WAS가 여러 대 떠있는 경우, 해당 validation 자체를 동시에 여러 클라이언트가 통과할 수 있음
    // -> 최종 검증을 위해 DB의 name 필드에 유니크 제약조건 설정하는 것을 권장
    private void validateDuplicateMember(Member member) {
        //EXCEPTION
       List<Member> findMembers =  memberRepository.findByName(member.getName());
       if(!findMembers.isEmpty()) {
           throw new IllegalStateException("이미 존재하는 회원입니다.");
       }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
