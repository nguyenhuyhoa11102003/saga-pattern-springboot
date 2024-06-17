package com.demo.orderservice.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
	private UUID id;
	private UUID customerId;
	private UUID productId;
	private BigDecimal price;
	private int quantity;
	private Timestamp createdAt;
	private OrderStatus status;
}
