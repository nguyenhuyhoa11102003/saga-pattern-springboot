package com.demo.orderservice.domain.port;

import com.demo.orderservice.domain.OrderRequest;

import java.util.UUID;

public interface OrderUseCasePort {

	void placeOrder(OrderRequest orderRequest);

	void updateOrderStatus(UUID orderId, boolean success);

}
