package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * XToOne 관계에서의 성능 최적화
 * Order
 * Order -> Member (ManyToOne)
 * Order -> Delivery (OneToOne)
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    // 결과:
    // Order와 Member는 양방향 관계이므로, 무한루프 돌면서 데이터 반환함
    // 해결: @JsonIgnore로 양방향 관계 중 하나는 끊어줌
    // 지연로딩 설정된 데이터는 기본적으로 null로 반환됨
    // 강제 지연 로딩 설정가능하나, 불필요한 엔티티도 외부로 노출됨
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch()); // 모든 주문 조회

        // 강제 지연 로딩 없이 원하는 데이터만 조회하는 방법
        for(Order order : all) {
            // getMember()까지는 프록시 객체이므로 DB에 쿼리 X
            // getName()부터는 실제 데이터가 필요하므로 DB에 쿼리 나감
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
        }
        return all;
    }
}
