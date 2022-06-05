package jpabook.jpashop.api;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

	private final OrderRepository orderRepository;


	@GetMapping("/api/v1/orders")
	public List<Order> ordersV1() {
		List<Order> all = orderRepository.findAllByString(new OrderSearch());

		for (Order order : all) {
			order.getMember().getName();
			order.getDelivery().getAddress();
			List<OrderItem> orderItems = order.getOrderItems();
			orderItems.stream().forEach(o->o.getItem().getName());
		}
		return all;
	}

	@GetMapping("/api/v2/orders")
	public List<OrderDto> ordersV2() {
		return orderRepository.findAllByString(new OrderSearch()).stream()
			.map(OrderDto::new)
			.collect(toList());
	}

	/**
	 * 1:다이면 다만큼 데이터가 뻥튀기가 되어 버린다.
	 * 의도한 것과 다른 결과값이 나타난다.
	 * @return
	 */
	@GetMapping("/api/v3/orders")
	public List<OrderDto> ordersV3() {
		return orderRepository.findAllWithItem()
			.stream()
			.map(OrderDto::new)
			.collect(toList());

	}

	/**
	 * V3에서 문제는 중복 데이터가 엄청 많아지게 된다
	 * 쿼리는 한방에 나가지만 데이터는 정말 많이 반환이 된다.
	 * 이렇게 사용하는 것이 더 나은 선택잃수도
	 * 성능도 더 좋을 수도 있다.
	 * 페이징 가능!
	 * @param offset
	 * @param limit
	 * @return
	 */
	@GetMapping("/api/v3.1/orders")
	public List<OrderDto> ordersV3_page(@RequestParam(value = "offset",defaultValue = "0")int offset,
		@RequestParam(value = "limit",defaultValue = "100") int limit) {

		List<Order> orders = orderRepository.findAllWithMemberDelivery(offset,limit);

		List<OrderDto> result = orders.stream()
			.map(o -> new OrderDto(o))
			.collect(toList());

		return result;

	}


	@Data
	static class OrderDto{

		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
		private List<OrderItemDto> orderItems;

		public OrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName();
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress();
			// order.getOrderItems().stream().forEach(o->o.getItem().getName());
			orderItems = order.getOrderItems().stream()
				.map(OrderItemDto::new)
				.collect(toList());
		}
	}

	@Getter
	static class OrderItemDto{
		private String itemName;
		private int orderPrice;
		private int count;

		public OrderItemDto(OrderItem orderItem) {
			itemName = orderItem.getItem().getName();
			orderPrice = orderItem.getOrderPrice();
			count = orderItem.getCount();
		}
	}


}
