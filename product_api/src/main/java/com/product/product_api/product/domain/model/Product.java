package com.product.product_api.product.domain.model;

import java.util.UUID;

/**
 * Product
 */
public record Product(
    UUID id,
    String name,
    String category,
    String description,
    Long price,
    String imageUrl) {

  public Product {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be null or blank");
    } else if (category == null || category.isBlank()) {
      throw new IllegalArgumentException("Category cannot be null or blank");
    } else if (description == null || description.isBlank()) {
      throw new IllegalArgumentException("Description cannot be null or blank");
    } else if (price == null || price < 0) {
      throw new IllegalArgumentException("Price cannot be null or a negative value");
    } else if (imageUrl == null || imageUrl.isBlank()) {
      throw new IllegalArgumentException("imageUrl cannot be null or blank");
    }
  }
}
