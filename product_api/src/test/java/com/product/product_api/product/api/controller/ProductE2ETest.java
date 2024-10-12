package com.product.product_api.product.api.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.product.product_api.product.api.controller.contracts.CreateProductV1;
import com.product.product_api.product.api.controller.contracts.ProductV1;

/**
 * ProductE2ETest
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
@DisabledInAotMode
@ActiveProfiles("test") // Activate the test profile
@TestMethodOrder(OrderAnnotation.class)
public class ProductE2ETest {

  @Autowired
  private WebTestClient webTestClient;

  @Container
  public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
      .withDatabaseName("products")
      .withUsername("admin123")
      .withPassword("admin123");

  @DynamicPropertySource
  static void databaseProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.liquibase.url", postgres::getJdbcUrl);
    registry.add("spring.liquibase.user", postgres::getUsername);
    registry.add("spring.liquibase.password", postgres::getPassword);
    registry.add("spring.liquibase.enabled", () -> true); // Ensure Liquibase is enabled

    registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://" + postgres.getHost() + ":"
        + postgres.getFirstMappedPort() + "/" + postgres.getDatabaseName());
    registry.add("spring.r2dbc.username", postgres::getUsername);
    registry.add("spring.r2dbc.password", postgres::getPassword);
  }

  @BeforeAll
  public static void setUp() {
    postgres.start();
  }

  @AfterAll
  public static void close() {
    postgres.close();
  }

  @Test
  @Order(1)
  public void testCreateTest() {
    CreateProductV1 newProduct1 = new CreateProductV1("name1", "category1", "description1", 1L, "imageUrl1");
    CreateProductV1 newProduct2 = new CreateProductV1("name2", "category2", "description2", 2L, "imageUrl2");

    webTestClient.post()
        .uri("/api/v1/products")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newProduct1)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ProductV1.class)
        .value(createdProduct -> {
          assertThat(createdProduct, is(notNullValue()));
          assertThat(createdProduct.id(), is(notNullValue()));
          assertThat(createdProduct.name(), is("name1"));
          assertThat(createdProduct.category(), is("category1"));
          assertThat(createdProduct.description(), is("description1"));
          assertThat(createdProduct.price(), is(1L));
          assertThat(createdProduct.imageUrl(), is("imageUrl1"));
        });

    webTestClient.post()
        .uri("/api/v1/products")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newProduct2)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ProductV1.class)
        .value(createdProduct2 -> {
          assertThat(createdProduct2, is(notNullValue()));
          assertThat(createdProduct2.id(), is(notNullValue()));
          assertThat(createdProduct2.name(), is("name2"));
          assertThat(createdProduct2.category(), is("category2"));
          assertThat(createdProduct2.description(), is("description2"));
          assertThat(createdProduct2.price(), is(2L));
          assertThat(createdProduct2.imageUrl(), is("imageUrl2"));
        });
  }

  @Test
  @Order(2)
  public void testGetProducts() {
    webTestClient.get()
        .uri("/api/v1/products")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(ProductV1.class)
        .value(products -> {
          assertThat(products, is(notNullValue()));
          assertThat(products.size(), is(22));
        });
  }

  @Test
  @Order(3)
  public void testGetProduct() {
    webTestClient.get()
        .uri("/api/v1/products")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(ProductV1.class)
        .value(products -> {
          assertThat(products, is(notNullValue()));
          assertThat(products.size(), is(22));

          webTestClient.get()
              .uri("/api/v1/products/" + products.get(10).id())
              .exchange()
              .expectStatus().isOk()
              .expectBody(ProductV1.class)
              .value(product -> {
                assertThat(product, is(notNullValue()));
                assertThat(product.id(), is(products.get(10).id()));
                assertThat(product.name(), is("name1"));
                assertThat(product.category(), is("category1"));
                assertThat(product.description(), is("description1"));
                assertThat(product.price(), is(1L));
                assertThat(product.imageUrl(), is("imageUrl1"));
              });

        });
  }

  @Test
  @Order(4)
  public void testGetUserNotFound() {
    webTestClient.get()
        .uri("/api/v1/products/" + UUID.randomUUID())
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @Order(5)
  public void testUpdateProduct() {
    webTestClient.get()
        .uri("/api/v1/products")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(ProductV1.class)
        .value(products -> {
          assertThat(products, is(notNullValue()));
          assertThat(products.size(), is(22));

          var product1 = products.get(0);
          var updatedProduct = new ProductV1(product1.id(), "name3", "category3", "description3", 3L, "imageUrl3");
          webTestClient.put()
              .uri("/api/v1/products")
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue(updatedProduct)
              .exchange()
              .expectStatus().isOk()
              .expectBody(ProductV1.class)
              .value(updatedTest -> {
                assertThat(updatedTest, is(notNullValue()));
                assertThat(updatedTest.id(), is(product1.id()));
                assertThat(updatedTest.name(), is("name3"));
                assertThat(updatedTest.category(), is("category3"));
                assertThat(updatedTest.description(), is("description3"));
                assertThat(updatedTest.price(), is(3L));
                assertThat(updatedTest.imageUrl(), is("imageUrl3"));
              });
    });
  }

  @Test
  @Order(6)
  public void testDeleteProduct() {
    webTestClient.get()
        .uri("/api/v1/products")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(ProductV1.class)
        .value(products -> {
          assertThat(products, is(notNullValue()));
          assertThat(products.size(), is(22));

          webTestClient.delete()
              .uri("/api/v1/products/" + products.get(0).id())
              .exchange()
              .expectStatus().isOk();
    });
  }
}
