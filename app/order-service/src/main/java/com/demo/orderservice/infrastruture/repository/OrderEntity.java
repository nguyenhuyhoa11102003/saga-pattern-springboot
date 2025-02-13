package com.demo.orderservice.infrastruture.repository;

import com.demo.orderservice.domain.entity.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "order_entity")
public class OrderEntity {

	@Id
	private UUID id;

	@Column(nullable = false)
	private UUID customerId;

	@Column(nullable = false)
	private UUID productId;

	private BigDecimal price;

	private int quantity;

	private Timestamp createdAt;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
}
