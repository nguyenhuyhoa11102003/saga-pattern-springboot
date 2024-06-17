package com.customerservice.domain.port;

import com.customerservice.domain.CustomerRequest;
import com.customerservice.domain.entity.Customer;

import java.util.UUID;

public interface CustomerUseCasePort {

  Customer findById(UUID customerId);

  Customer create(CustomerRequest customerRequest);

//  boolean reserveBalance(PlacedOrderEvent orderEvent);

//  void compensateBalance(PlacedOrderEvent orderEvent);
}