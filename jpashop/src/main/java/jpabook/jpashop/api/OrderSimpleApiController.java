package jpabook.jpashop.api;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * xToOne
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;
	private final OrderSimpleQueryRepository orderSimpleQueryRepository;

	/**
	 * 무한 루프에 빠진다
	 * json이 무한으로 돌기 때
	 * @return
	 */
	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1() {

		//Order 2개
		//N + 1 문제 -> 1 + 회원 N + 배송 N 문제
		List<Order> all = orderRepository.findAllByString(new OrderSearch());

		//2개
		for (Order order : all) {
			//Proxy 객체였지만 name까지 가져오게 되면 강제 초기화
			order.getMember().getName();
			order.getDelivery().getAddress();

		}
		return all;
	}

	@GetMapping("/api/v2/simple-orders")
	public List<SimpleOrderDto> ordersV2() {
		return orderRepository.findAllByString(new OrderSearch()).stream()
			.map(SimpleOrderDto::new)
			.collect(toList());

	}

	@GetMapping("/api/v3/simple-orders")
	public List<SimpleOrderDto> ordersV3() {
		return orderRepository.findAllWithMemberDelivery()
			.stream()
			.map(SimpleOrderDto::new)
			.collect(toList());
	}

	@GetMapping("/api/v4/simple-orders")
	public List<OrderSimpleQueryDto> ordersV4() {
		return orderSimpleQueryRepository.findOrderDtos();
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
