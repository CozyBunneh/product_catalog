package com.product.product_api.product.infrastructure.repository.entity;

import java.util.UUID;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

/**
 * ProductEntity
 */
@EntityScan
@Table("products")
public record ProductEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id", nullable = false) UUID id,

    @Column(name = "name", nullable = false) String name,

    @Column(name = "category", nullable = false) String category,

    @Column(name = "description", nullable = false) String description,

    @Column(name = "price", nullable = false) Long price,

    @Column(name = "imageUrl", nullable = false) String imageUrl) {
}
