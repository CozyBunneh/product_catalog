package com.product.product_api.product.api.controller;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.product.product_api.product.api.controller.contracts.CreateProductV1;
import com.product.product_api.product.api.controller.contracts.ProductV1;
import com.product.product_api.product.domain.command.DeleteProductCommand;
import com.product.product_api.product.domain.query.GetProductByFuzzyFindQuery;
import com.product.product_api.product.domain.query.GetProductByIdQuery;
import com.product.product_api.product.domain.service.ProductService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import reactor.core.publisher.Mono;

/**
 * ProductController
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/")
public class ProductController {

  private static final Integer CAPACITY = 20;
  private static final Integer REFILL_TOKENS = 20;
  private static final Integer REFILL_DURATION_IN_MIN = 1;
  private static final Integer CONSUME_AMOUNT = 1;
  private static final String RATE_LIMIT_REMAINING_HEADER = "X-Rate-Limit-Remaining";
  private static final Integer DEFAULT_MAX_DISTANCE = 2;

  private final ProductService productService;
  private final Bucket bucket;

  public ProductController(ProductService productService) {
    this.productService = productService;
    Bandwidth limit = Bandwidth.builder().capacity(CAPACITY)
        .refillGreedy(REFILL_TOKENS, Duration.ofMinutes(REFILL_DURATION_IN_MIN)).build();
    this.bucket = Bucket.builder()
        .addLimit(limit)
        .build();
  }

  @GetMapping("products/{id}")
  public Mono<ResponseEntity<ProductV1>> getById(@PathVariable UUID id) {
    ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(CONSUME_AMOUNT);
    if (probe.isConsumed()) {
      return productService.getById(new GetProductByIdQuery(id))
          .map(model -> okResponse(ProductV1.fromModel(model), probe))
          .switchIfEmpty(Mono.just(notFoundResponseBuilder(probe).build()));
    }
    return Mono.just(tooManyRequestsBuilder().build());
  }

  @GetMapping("/products")
  public Mono<ResponseEntity<List<ProductV1>>> get() {
    ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(CONSUME_AMOUNT);
    if (probe.isConsumed()) {
      return productService.getAll().collectList()
          .map(products -> okResponse(products.stream().map(product -> ProductV1.fromModel(product)).toList(), probe));
    }
    return Mono.just(tooManyRequestsBuilder().build());
  }

  @GetMapping("/search")
  public Mono<ResponseEntity<List<ProductV1>>> get(@RequestParam Optional<String> query,
      @RequestParam Optional<Integer> maxDistance) {
    ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(CONSUME_AMOUNT);
    if (probe.isConsumed()) {
      // Try to search by query, fallback to get all
      return query.map(queryDef -> {
        return productService.fuzzySearch(new GetProductByFuzzyFindQuery(queryDef, maxDistance.orElse(DEFAULT_MAX_DISTANCE))).collectList()
            .map(
                products -> okResponse(products.stream().map(product -> ProductV1.fromModel(product)).toList(), probe));
      }).orElse(productService.getAll().collectList()
          .map(products -> okResponse(products.stream().map(product -> ProductV1.fromModel(product)).toList(), probe)));
    }
    return Mono.just(tooManyRequestsBuilder().build());
  }

  @PostMapping("/products")
  public Mono<ResponseEntity<ProductV1>> create(@RequestBody CreateProductV1 request) {
    ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(CONSUME_AMOUNT);
    if (probe.isConsumed()) {
      return productService.create(request.toCreateProductCommand())
          .map(model -> okResponse(ProductV1.fromModel(model), probe))
          .switchIfEmpty(Mono.just(badRequestResponseBuilder(probe).build()));
    }
    return Mono.just(tooManyRequestsBuilder().build());
  }

  @PutMapping("/products")
  public Mono<ResponseEntity<ProductV1>> update(@RequestBody ProductV1 request) {
    ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(CONSUME_AMOUNT);
    if (probe.isConsumed()) {
      return productService.update(request.toUpdateProductCommand())
          .map(model -> okResponse(ProductV1.fromModel(model), probe))
          .switchIfEmpty(Mono.just(badRequestResponseBuilder(probe).build()));
    }
    return Mono.just(tooManyRequestsBuilder().build());
  }

  @DeleteMapping("/products/{id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable UUID id) {
    ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(CONSUME_AMOUNT);
    if (probe.isConsumed()) {
      return productService.delete(new DeleteProductCommand(id)).map(voided -> okResponse(voided, probe));
    }
    return Mono.just(tooManyRequestsBuilder().build());
  }

  private <T> ResponseEntity<T> okResponse(T body, ConsumptionProbe probe) {
    return ResponseEntity.status(HttpStatus.OK)
        .header(RATE_LIMIT_REMAINING_HEADER, Long.toString(probe.getRemainingTokens())).body(body);
  }

  public BodyBuilder badRequestResponseBuilder(ConsumptionProbe probe) {
    return ResponseEntity.badRequest()
        .header(RATE_LIMIT_REMAINING_HEADER, Long.toString(probe.getRemainingTokens()));
  }

  public BodyBuilder notFoundResponseBuilder(ConsumptionProbe probe) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .header(RATE_LIMIT_REMAINING_HEADER, Long.toString(probe.getRemainingTokens()));
  }

  public BodyBuilder tooManyRequestsBuilder() {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS);
  }
}
