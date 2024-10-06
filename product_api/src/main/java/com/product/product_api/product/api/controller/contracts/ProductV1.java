package com.product.product_api.product.api.controller.contracts;

import java.util.UUID;

import com.product.product_api.product.domain.command.UpdateProductCommand;
import com.product.product_api.product.domain.model.Product;

/**
 * ProductV1
 */
public record ProductV1(
    UUID id,
    String name,
    String category,
    String description,
    Long price,
    String imageUrl) {

  public static ProductV1 fromModel(Product product) {
    return new ProductV1(product.id(), product.name(), product.category(), product.description(), product.price(),
        product.imageUrl());
  }

  public Product toModel() {
    return new Product(id, name, category, description, price, imageUrl);
  }

  public UpdateProductCommand toUpdateProductCommand() {
    return new UpdateProductCommand(id, name, category, description, price, imageUrl);
  }
}
