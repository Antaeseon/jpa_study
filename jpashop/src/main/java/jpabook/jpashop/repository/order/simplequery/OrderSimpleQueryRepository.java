package jpabook.jpashop.repository.order.simplequery;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import jpabook.jpashop.repository.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;

/**
 * 화면에 박혀있기 때문에 이렇게 Repository랑 따로 하는 것이 좋다.
 * => 유지보수성 향상
 */
@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

	private final EntityManager em;

	public List<OrderSimpleQueryDto> findOrderDtos() {
		return em.createQuery(
			"select new jpabook.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, m.address)"
				+ " from Order o "
				+ "join o.member m "
				+ "join o.delivery d",OrderSimpleQueryDto.class
		).getResultList();
	}

}
