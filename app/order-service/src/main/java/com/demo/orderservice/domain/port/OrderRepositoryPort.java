package com.demo.orderservice.domain.port;

import com.demo.orderservice.domain.entity.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {
	Optional<Order> findOrderById(UUID orderId);

	void saveOrder(Order order);

	void exportOutBoxEvent(Order order);

}
