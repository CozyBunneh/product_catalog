package com.product.product_api.product.api.controller.contracts;

/**
 * CreateProductV1
 */
public record CreateProductV1(
    String name,
    String category,
    String description,
    Long price,
    String imageUrl) {
}
