package com.customerservice.domain;

import com.customerservice.domain.entity.Customer;
import com.customerservice.domain.exception.NotFoundException;
import com.customerservice.domain.port.CustomerRepositoryPort;
import com.customerservice.domain.port.CustomerUseCasePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class CustomerUseCase implements CustomerUseCasePort {
	private final ObjectMapper mapper;
	private final CustomerRepositoryPort customerRepository;

	@Override
	public Customer findById(UUID customerId) {
		return this.customerRepository.findCustomerById(customerId).orElseThrow(NotFoundException::new);
	}

	@Override
	public Customer create(CustomerRequest customerRequest) {
		var customer = mapper.convertValue(customerRequest, Customer.class);
		customer.setId(UUID.randomUUID());
		return customerRepository.saveCustomer(customer);
	}

	@Override
	public boolean reserveBalance(PlacedOrderEvent orderEvent) {
		var customer = findById(orderEvent.customerId());
		var newBalance = customer
				.getBalance()
				.subtract(orderEvent.price().multiply(BigDecimal.valueOf(orderEvent.quantity())));
		if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
			return false;
		}
		customer.setBalance(newBalance);
		customerRepository.saveCustomer(customer);
		return true;
	}

	@Override
	public void compensateBalance(PlacedOrderEvent orderEvent) {
		var customer = findById(orderEvent.customerId());
		var newBalance = customer
				.getBalance()
				.add(orderEvent.price().multiply(BigDecimal.valueOf(orderEvent.quantity())));
		customer.setBalance(newBalance);
		customerRepository.saveCustomer(customer);
	}

}
