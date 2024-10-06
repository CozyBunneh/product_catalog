package com.product.product_api.product.domain.query;

/**
 * GetProductByFuzzyFindQuery
 */
public record GetProductByFuzzyFindQuery(String searchTerm, Integer maxDistance) {
}
