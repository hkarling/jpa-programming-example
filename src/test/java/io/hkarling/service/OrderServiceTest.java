package io.hkarling.service;

import io.hkarling.domain.Address;
import io.hkarling.domain.Member;
import io.hkarling.domain.Order;
import io.hkarling.domain.OrderStatus;
import io.hkarling.domain.item.Book;
import io.hkarling.exception.NotEnoughStockException;
import io.hkarling.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void order() {
        // given
        Member member = createMember();
        Book book = createBook();

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수");
        assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문가격 = 가격 * 수량");
        assertEquals(8, book.getStockQuantity(), "주문수량");
    }

    @Test
    public void orderAmountExist() {
        // given
        Member member = createMember();
        Book book = createBook();

        // when
        int orderCount = 11;

        // then
        assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), book.getId(), orderCount));
    }


    @Test
    public void cancelOrder() {
        // given
        Member member = createMember();
        Book book = createBook();

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문취소시 상태는 CANCEL");
        assertEquals(10, book.getStockQuantity(), "주문취소시 재고 원복");


    }

    private Book createBook() {
        Book book = new Book();
        book.setName("JPA BOOK");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("MEMBER_A");
        member.setAddress(new Address("Seoul", "Sanun", "123123"));
        em.persist(member);
        return member;
    }

}