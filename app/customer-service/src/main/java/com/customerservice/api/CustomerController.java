package com.customerservice.api;


import com.customerservice.domain.CustomerRequest;
import com.customerservice.domain.entity.Customer;
import com.customerservice.domain.port.CustomerUseCasePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
	private final CustomerUseCasePort customerUseCase;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Customer create(@RequestBody @Valid CustomerRequest customerRequest) {
		return customerUseCase.create(customerRequest);
	}

	@GetMapping("/{customerId}")
	public Customer findById(@PathVariable UUID customerId) {
		return customerUseCase.findById(customerId);
	}
}
