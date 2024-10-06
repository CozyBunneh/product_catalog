package com.product.product_api.product.domain.command;

import com.product.product_api.product.domain.model.Product;

/**
 * CreateProductCommand
 */
public record CreateProductCommand(
    String name,
    String category,
    String description,
    Long price,
    String imageUrl) {

  public Product toModel() {
    return new Product(null, name, category, description, price, imageUrl);
  }
}
