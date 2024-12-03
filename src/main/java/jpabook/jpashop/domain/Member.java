package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id") // 테이블 컬럼명 지정
    private Long id;

//    @NotEmpty
    private String name;

    @Embedded
    private Address address;

//    @JsonIgnore // 반환 정보에서 빠짐, 하지만 다른 API에서는 필요로 한다면..?
    @OneToMany(mappedBy = "member") // order 테이블의 member 필드에 의해 매핑됨
    private List<Order> orders = new ArrayList<>();
}
