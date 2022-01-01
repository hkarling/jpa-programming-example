package io.hkarling;

import io.hkarling.domain.*;
import io.hkarling.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitSampleData {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        public void dbInit1() {
            Member member = getMember("userA", "Seoul", "Street01", "1A2B3C");

            Book book1 = createBook("JPA1 BOOK", 10000, 100);
            em.persist(book1);
            Book book2 = createBook("JPA2 BOOK", 12000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 24000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }


        public void dbInit2() {
            Member member = getMember("userB", "Busan", "Street02", "3C2B1C");

            Book book1 = createBook("SPRING1 BOOK", 20000, 300);
            em.persist(book1);
            Book book2 = createBook("SPRING2 BOOK", 23000, 300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 60000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 92000, 4);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Member getMember(String userA, String seoul, String street01, String s) {
            Member member = new Member();
            member.setName(userA);
            member.setAddress(new Address(seoul, street01, s));
            em.persist(member);
            return member;
        }

        private Book createBook(String s, int i, int i2) {
            Book book1 = new Book();
            book1.setName(s);
            book1.setPrice(i);
            book1.setStockQuantity(i2);
            return book1;
        }
    }
}
