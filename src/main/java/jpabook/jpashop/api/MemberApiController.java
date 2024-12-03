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


// RestController는 아래 두 개 어노테이션을 포함
// ResponseBody는 데이터를 json, xml로 보낼 때 활용
//@Controller @ResponseBody

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

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
