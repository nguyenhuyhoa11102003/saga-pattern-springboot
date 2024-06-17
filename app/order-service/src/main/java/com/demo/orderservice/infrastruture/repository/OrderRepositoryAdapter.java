package com.demo.orderservice.infrastruture.repository;

import com.demo.orderservice.domain.entity.Order;
import com.demo.orderservice.domain.port.OrderRepositoryPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {
	private final ObjectMapper mapper;
	private final OrderJpaRepository orderJpaRepository;

	@Override
	public Optional<Order> findOrderById(UUID orderId) {
		return orderJpaRepository
				.findById(orderId)
				.map(orderEntity -> mapper.convertValue(orderEntity, Order.class));
	}

	@Override
	public void saveOrder(Order order) {
		OrderEntity entity = mapper.convertValue(order, OrderEntity.class);
		orderJpaRepository.save(entity);
	}

	@Override
	public void exportOutBoxEvent(Order order) {

	}
}
