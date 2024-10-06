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
}
