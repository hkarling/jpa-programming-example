package io.hkarling.api;

import io.hkarling.domain.Address;
import io.hkarling.domain.Order;
import io.hkarling.domain.OrderSearch;
import io.hkarling.domain.OrderStatus;
import io.hkarling.repository.OrderRepository;
import io.hkarling.repository.order.simple.OrderSimpleQueryDTO;
import io.hkarling.repository.order.simple.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSampleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); // Lazy 강제초기화
            order.getDelivery().getAddress(); // Lazy 강제초기화
        }
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDTO> orderV2() {
        return orderRepository.findAllByString(new OrderSearch()).stream()
                .map(SimpleOrderDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDTO> orderV3() {
        return orderRepository.findAllMemberWithDelivery().stream()
                .map(SimpleOrderDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDTO> orderV4() {
        return orderSimpleQueryRepository.findOrderDTO();
    }

    @Data
    static class SimpleOrderDTO {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDTO(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
