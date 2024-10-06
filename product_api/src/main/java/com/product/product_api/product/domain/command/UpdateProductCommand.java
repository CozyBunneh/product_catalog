package com.product.product_api.product.domain.command;

import java.util.UUID;

import com.product.product_api.product.domain.model.Product;

/**
 * UpdateProductCommand
 */
public record UpdateProductCommand(
    UUID id,
    String name,
    String category,
    String description,
    Long price,
    String imageUrl) {

  public Product toModel() {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }

    return new Product(id, name, category, description, price, imageUrl);
  }
}
