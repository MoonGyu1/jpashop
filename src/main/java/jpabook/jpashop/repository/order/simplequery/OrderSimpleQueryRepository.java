package jpabook.jpashop.repository.order.simplequery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    // JPA는 기본적으로 엔티티나 Address와 같은 value object만 반환 가능
    // 그외 타입을 반환하기 위해서는 new 오퍼레이션 사용
    // new 오퍼레이션은 엔티티를 파라미터로 받을 수 없기 때문에(이유: 기본적으로 PK로 인식) 필드로 넣음
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
}
