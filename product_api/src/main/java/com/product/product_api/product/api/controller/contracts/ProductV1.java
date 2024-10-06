package com.product.product_api.product.api.controller.contracts;

import java.util.UUID;

/**
 * ProductV1
 */
public record ProductV1(
  UUID id,
  String name,
  String category,
  String description,
  Long price,
  String imageUrl 
) {}
