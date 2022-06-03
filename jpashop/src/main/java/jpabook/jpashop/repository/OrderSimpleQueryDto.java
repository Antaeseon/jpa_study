package jpabook.jpashop.repository;

import java.time.LocalDateTime;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

@Data
public class OrderSimpleQueryDto {
	private Long orderId;
	private String name;
	private LocalDateTime orderDate;
	private OrderStatus orderStatus;
	private Address address;

	public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus status, Address address) {
		this.orderId = orderId;
		this.name = name; // LAZY 초기화
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.address = address; // LAZY 초기화

	}

}
