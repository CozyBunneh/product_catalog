package com.product.product_api.product.infrastructure.repository.entity;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.product.product_api.product.domain.model.Product;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

/**
 * ProductEntity
 */
@EntityScan
@Table("products")
public record ProductEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id", nullable = false) UUID id,

    @Column(name = "name", nullable = false) String name,

    @Column(name = "category", nullable = false) String category,

    @Column(name = "description", nullable = false) String description,

    @Column(name = "price", nullable = false) Long price,

    @Column(name = "image_url", nullable = false) String imageUrl) {

  public static List<Product> toModels(List<ProductEntity> entities) {
    return entities.stream().map(entity -> entity.toModel()).toList();
  }

  public Product toModel() {
    return new Product(id, name, category, description, price, imageUrl);
  }

  public static List<ProductEntity> fromModels(List<Product> models) {
    return models.stream().map(model -> ProductEntity.fromModel(model)).toList();
  }

  public static ProductEntity fromModel(Product model) {
    return new ProductEntity(model.id(), model.name(), model.category(), model.description(), model.price(), model.imageUrl());
  }

}
