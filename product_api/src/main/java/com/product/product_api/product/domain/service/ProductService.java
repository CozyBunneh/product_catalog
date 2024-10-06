package com.product.product_api.product.domain.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.product.product_api.product.domain.command.CreateProductCommand;
import com.product.product_api.product.domain.command.DeleteProductCommand;
import com.product.product_api.product.domain.command.UpdateProductCommand;
import com.product.product_api.product.domain.model.Product;
import com.product.product_api.product.domain.query.GetProductByFuzzyFindQuery;
import com.product.product_api.product.domain.query.GetProductByIdQuery;
import com.product.product_api.product.infrastructure.repository.ProductRepositoryCrud;
import com.product.product_api.product.infrastructure.repository.ProductRepositorySorting;
import com.product.product_api.product.infrastructure.repository.entity.ProductEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ProductService
 */
@Service
public class ProductService {

  private final ProductRepositoryCrud productRepositoryCrud;
  private final ProductRepositorySorting productRepositorySorting;

  public ProductService(ProductRepositoryCrud productRepositoryCrud,
      ProductRepositorySorting productRepositorySorting) {
    this.productRepositoryCrud = productRepositoryCrud;
    this.productRepositorySorting = productRepositorySorting;
  }

  public Mono<Product> getById(GetProductByIdQuery query) {
    return productRepositoryCrud.findById(query.id()).map(entity -> entity.toModel());
  }

  public Flux<Product> getAll() {
    return productRepositorySorting.findAllBy(Pageable.unpaged(Sort.by(Sort.Direction.ASC, "name")))
        .map(entity -> entity.toModel());
  }

  public Mono<Product> create(CreateProductCommand command) {
    return productRepositoryCrud.save(ProductEntity.fromModel(command.toModel())).map(entity -> entity.toModel());
  }

  public Mono<Product> update(UpdateProductCommand command) {
    var model = command.toModel();
    return productRepositoryCrud.findById(model.id())
        .flatMap(entity -> {
          var updated = new ProductEntity(entity.id(), model.name(), model.category(), model.description(),
              model.price(), model.imageUrl());
          return productRepositoryCrud.save(updated).map(entity2 -> entity2.toModel());
        }).switchIfEmpty(
            Mono.error(new IllegalArgumentException(String.format("product for id not % found", model.id()))));
  }

  public Mono<Void> delete(DeleteProductCommand command) {
    return productRepositoryCrud.deleteById(command.id());
  }

  public Flux<Product> getFuzzyByName(GetProductByFuzzyFindQuery query) {
    return productRepositorySorting
        .fuzzySearch(query.searchTerm(), query.maxDistance())
        .map(entity -> entity.toModel());
  }
}
