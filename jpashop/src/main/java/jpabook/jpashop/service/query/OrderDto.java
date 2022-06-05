package jpabook.jpashop.service.query;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

@Data
public class OrderDto {

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

	public OrderDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus,
		Address address, List<OrderItemDto> orderItems) {
		this.orderId = orderId;
		this.name = name;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.address = address;
		this.orderItems = orderItems;
	}
}
