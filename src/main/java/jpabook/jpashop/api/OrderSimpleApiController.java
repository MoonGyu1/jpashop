package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    // N+1 문제 발생
    // 첫 번째 쿼리의 결과 N만큼 쿼리가 추가 실행됨
    // 1 + 회원 N + 배송 N
    // => 총 5번
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() { // 실제로는 Result로 한 번 감싸주기
        List<Order> orders = orderRepository.findAllByString(new OrderSearch()); // SQL 1번 -> 결과 2개

        // 2번 * SQL 2번 (Member, Delivery) => SQL 4번
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
//                .map(SimpleOrderDto::new)
                .toList();

        return result;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY 초기화
        }
    }
}
