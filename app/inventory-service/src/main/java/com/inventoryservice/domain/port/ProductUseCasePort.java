package com.inventoryservice.domain.port;

import com.inventoryservice.domain.PlacedOrderEvent;
import com.inventoryservice.domain.ProductRequest;
import com.inventoryservice.domain.entity.Product;

import java.util.UUID;

public interface ProductUseCasePort {

    Product findById(UUID productId);

    Product create(ProductRequest productRequest);

    boolean reserveProduct(PlacedOrderEvent orderEvent);
}