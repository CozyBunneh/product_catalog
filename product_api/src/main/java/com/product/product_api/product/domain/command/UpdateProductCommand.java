package com.product.product_api.product.domain.command;

import java.util.UUID;

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
}
