package com.inventoryservice.domain.port;

import com.inventoryservice.domain.entity.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryPort {
	 Optional<Product> findProductById(UUID productId);
    Product saveProduct(Product product);
}
