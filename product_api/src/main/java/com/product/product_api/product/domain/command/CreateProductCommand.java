package com.product.product_api.product.domain.command;

/**
 * CreateProductCommand
 */
public record CreateProductCommand(
    String name,
    String category,
    String description,
    Long price,
    String imageUrl) {
}
