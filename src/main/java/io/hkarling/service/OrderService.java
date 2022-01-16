package io.hkarling.service;

import io.hkarling.domain.*;
import io.hkarling.domain.item.Item;
import io.hkarling.repository.ItemRepository;
import io.hkarling.repository.MemberRepository;
import io.hkarling.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /* Order */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepository.findById(memberId).get();
        Item item = itemRepository.findOne(itemId);

        // 배송정보 설정
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        /* 도메인 모델 패턴 : 서비스 계층은 도메인에 비즈니스 로직을 위임 - JPA 사용 시 추천
           트랜잭션 스크립트 패턴 : 서비스 계층에서 비즈니스 로직을 처리 - SQL 을 직접 다룰 때 추천
           - 유지보수의 편이성에 따라 선택하여 사용한다. */
        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /* Cancel */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    /* Search */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
}
