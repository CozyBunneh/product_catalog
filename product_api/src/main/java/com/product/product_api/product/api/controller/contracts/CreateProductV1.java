package com.product.product_api.product.api.controller.contracts;

import com.product.product_api.product.domain.command.CreateProductCommand;

/**
 * CreateProductV1
 */
public record CreateProductV1(
    String name,
    String category,
    String description,
    Long price,
    String imageUrl) {

  public CreateProductCommand toCreateProductCommand() {
    return new CreateProductCommand(name, category, description, price, imageUrl);
  }
}
