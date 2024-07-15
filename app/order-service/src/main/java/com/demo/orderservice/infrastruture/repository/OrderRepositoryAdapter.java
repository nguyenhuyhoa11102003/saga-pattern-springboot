package com.demo.orderservice.infrastruture.repository;

import com.demo.orderservice.domain.entity.Order;
import com.demo.orderservice.domain.port.OrderRepositoryPort;
import com.demo.orderservice.infrastruture.message.outbox.OutBox;
import com.demo.orderservice.infrastruture.message.outbox.OutBoxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.demo.orderservice.infrastruture.message.EventHandlerAdapter.ORDER;
import static com.demo.orderservice.infrastruture.message.EventHandlerAdapter.ORDER_CREATED;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {
	private final ObjectMapper mapper;
	private final OrderJpaRepository orderJpaRepository;
	private final OutBoxRepository outBoxRepository;

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
		var outBox = OutBox.builder()
				.aggregateId(order.getId())
				.aggregateType(ORDER)
				.type(ORDER_CREATED)
				.payload(mapper.valueToTree(order))
				.build();
		outBoxRepository.save(outBox);
	}
}
