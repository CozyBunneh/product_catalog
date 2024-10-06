package com.product.product_api;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.product.product_api.product.api.controller.contracts.CreateProductV1;
import com.product.product_api.product.api.controller.contracts.ProductV1;
import com.product.product_api.product.domain.command.CreateProductCommand;
import com.product.product_api.product.domain.command.DeleteProductCommand;
import com.product.product_api.product.domain.command.UpdateProductCommand;
import com.product.product_api.product.domain.model.Product;
import com.product.product_api.product.domain.query.GetProductByFuzzyFindQuery;
import com.product.product_api.product.domain.query.GetProductByIdQuery;
import com.product.product_api.product.infrastructure.repository.entity.ProductEntity;

@RegisterReflectionForBinding({ ProductEntity.class, Product.class, ProductV1.class, CreateProductV1.class,
		CreateProductCommand.class, UpdateProductCommand.class, DeleteProductCommand.class, GetProductByIdQuery.class,
		GetProductByFuzzyFindQuery.class })
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
