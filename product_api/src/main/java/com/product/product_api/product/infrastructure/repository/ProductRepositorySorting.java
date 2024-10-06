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

  Flux<ProductEntity> findAllBy(Pageable pageable);

  Flux<ProductEntity> findByNameContainingIgnoreCase(String namePattern, Pageable pageable);

  @Query(value = "SELECT * FROM products " +
      "WHERE replace(lower(name), ' ', '') LIKE lower(:searchLikeTerm) OR " +
      "LEVENSHTEIN(replace(lower(name), ' ', ''), replace(lower(:searchTerm), ' ', '')) <= :maxDistance")
  Flux<ProductEntity> fuzzySearch(@Param("searchLikeTerm") String searchLikeTerm, @Param("searchTerm") String searchTerm, @Param("maxDistance") int maxDistance);
}
