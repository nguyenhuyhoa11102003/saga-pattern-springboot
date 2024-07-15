package com.demo.orderservice.domain.port;

import org.springframework.messaging.Message;

import java.util.function.Consumer;

public interface EventHandlerPort {
	Consumer<Message<String>> reserveCustomerBalanceStage();
	Consumer<Message<String>> reserveProductStockStage();
}