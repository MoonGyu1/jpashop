package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


// RestController는 아래 두 개 어노테이션을 포함
// ResponseBody는 데이터를 json, xml로 보낼 때 활용
//@Controller @ResponseBody

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // V1 - 문제
    // 1. 엔티티를 직접 반환하므로 원하지 않는 정보까지 노출됨
    //    (@JsonIgnore처럼 응답 스펙을 맞추기 위한 로직이 추가됨)
    // 2. 프레젠테이션 계층과 엔티티가 분리가 안 됨
    // 3. 엔티티가 바뀌면 API 스펙이 바뀜
    // 4. 최상위 데이터타입이 array면 다른 필드 추가하거나 확장이 어려움
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    // V2
    // DTO로 필요한 데이터만 노출
    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .toList();
        return new Result(collect);
//        return new Result(collect.size(), collect);
    }

    // Result 클래스로 컬렉션을 감싸서 배열이 아닌 객체를 반환하도록 함
    // -> 추후 필요한 필드를 추가할 수 있음
    @Data
    @AllArgsConstructor
    static class Result<T> {
//        private int count;
        private T data; // 컬렉션 데이터
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    // V1 - 문제
    // 1. presentation을 위한 검증로직이 엔티티에 들어감 (해당 부분은 변경 가능)
    // 2. 엔티티 변경사항이 API 스펙에 영향을 미침
    // 해결방안: DTO
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // V2 DTO - 장점
    // 1. API 스펙 유지 가능 - 엔티티 변경 시 컴파일 시점에 변경사항 잡아줌
    // 2. API 스펙 범위가 명확해짐
    // 3. 외부에 엔티티 노출 X
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());

        // 커맨드와 쿼리 분리 -> PK 기반 가벼운 조회는 서비스에서 member 객체를 반환하기보다는 다시 조회하기
        // 장점: 관심사 분리, 유지보수성 증가
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
