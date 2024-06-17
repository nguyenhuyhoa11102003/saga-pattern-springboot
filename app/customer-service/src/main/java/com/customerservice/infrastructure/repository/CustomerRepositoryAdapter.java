package com.customerservice.infrastructure.repository;

import com.customerservice.domain.entity.Customer;
import com.customerservice.domain.port.CustomerRepositoryPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {
	private final ObjectMapper mapper;
	private final CustomerJpaRepository customerJpaRepository;

	@Override
	public Optional<Customer> findCustomerById(UUID customerId) {
		var entity = customerJpaRepository.findById(customerId);
		return entity.map(customerEntity -> mapper.convertValue(customerEntity, Customer.class));
	}

	@Override
	public Customer saveCustomer(Customer customer) {
		var entity = mapper.convertValue(customer, CustomerEntity.class);
		customerJpaRepository.save(entity);
		return customer;
	}
}
