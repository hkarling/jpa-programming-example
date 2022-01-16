package io.hkarling.repository.order.simple;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    // 재사용성 없음, 데이터 변경불가
    public List<OrderSimpleQueryDTO> findOrderDTO() {
        return em.createQuery("select new io.hkarling.repository.order.simple.OrderSimpleQueryDTO(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleQueryDTO.class)
                .getResultList();
    }
}
