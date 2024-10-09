package com.product.product_api.product.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

import com.product.product_api.product.infrastructure.repository.entity.ProductEntity;

import reactor.core.publisher.Flux;

/**
 * ProductRepositorySorting
 */
@Repository
public interface ProductRepositorySorting extends ReactiveSortingRepository<ProductEntity, UUID> {

  Flux<ProductEntity> findAllByOrderByName(Pageable pageable);

  Flux<ProductEntity> findByNameContainingIgnoreCase(String namePattern, Pageable pageable);

  @Query(value = "SELECT " +
    "id, name, category, description, price, image_url, " +
    "strict_word_similarity(name, :searchTerm) AS score " +
    "FROM products " +
    "WHERE strict_word_similarity(name, :searchTerm) > 0 " +
    "ORDER BY score DESC; ")
  Flux<ProductEntity> fuzzySearch(@Param("searchTerm") String searchTerm);
}
