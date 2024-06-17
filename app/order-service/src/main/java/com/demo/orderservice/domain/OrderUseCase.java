package com.demo.orderservice.domain;

import com.demo.orderservice.domain.OrderRequest;
import com.demo.orderservice.domain.entity.Order;
import com.demo.orderservice.domain.entity.OrderStatus;
import com.demo.orderservice.domain.port.OrderRepositoryPort;
import com.demo.orderservice.domain.port.OrderUseCasePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.sql.Timestamp;
import java.time.Instant;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderUseCase implements OrderUseCasePort {

	private final ObjectMapper mapper;
	private final OrderRepositoryPort orderRepository;

	@Override
	public void placeOrder(OrderRequest orderRequest) {
		var order = mapper.convertValue(orderRequest, Order.class);
		order.setCreatedAt(Timestamp.from(Instant.now()));
		order.setStatus(OrderStatus.PENDING);
		order.setId(UUID.randomUUID());
		orderRepository.saveOrder(order);
//		orderRepository.exportOutBoxEvent(order);

	}

	@Override
	public void updateOrderStatus(UUID orderId, boolean success) {

	}
}
