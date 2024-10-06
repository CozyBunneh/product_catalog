package com.product.product_api.product.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.product.product_api.product.infrastructure.repository.entity.ProductEntity;

/**
 * ProductRepositoryCrud
 */
@Repository
public interface ProductRepositoryCrud extends ReactiveCrudRepository<ProductEntity, UUID> {
}
