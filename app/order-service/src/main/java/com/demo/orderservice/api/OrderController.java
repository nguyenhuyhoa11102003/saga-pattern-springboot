package com.demo.orderservice.api;


import com.demo.orderservice.domain.OrderRequest;
import com.demo.orderservice.domain.port.OrderUseCasePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
	private final OrderUseCasePort orderUseCasePort;

	@PostMapping
	public void placeOrder(@RequestBody @Valid OrderRequest orderRequest) {
		log.info("Received new order request {}", orderRequest);
		orderUseCasePort.placeOrder(orderRequest);
	}
}
