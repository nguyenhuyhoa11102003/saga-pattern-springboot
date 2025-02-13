package com.customerservice.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record PlacedOrderEvent(UUID id, UUID customerId, UUID productId, BigDecimal price,
                               Integer quantity) {

}