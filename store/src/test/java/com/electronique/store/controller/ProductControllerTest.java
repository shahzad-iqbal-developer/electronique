package com.electronique.store.controller;

import com.electronique.store.document.Product;
import com.electronique.store.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
public class ProductControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ProductRepository productRepository;

    List<Product> products = Arrays.asList(new Product(null,"Dell",2500.00,"Dell laptop"),
            new Product(null,"Hp",2300.00,"Hp laptop"),
            new Product("ABC","Lenovo",1200.25,"Lenovo laptop"));

    @Before
    public void setUp(){
            productRepository.deleteAll()
                    .thenMany(Flux.fromIterable(products))
                    .flatMap(productRepository::save)
                    .blockLast();
    }

    @Test
    public void getAllProducts(){
         webTestClient.get().uri("/api/product").exchange()
                 .expectStatus().isOk()
                 //.expectHeader(MediaType)
                 .expectBodyList(Product.class)
                 .hasSize(3);
    }

    @Test
    public void getAllProducts_WithStepVerifier(){
       Flux<Product> productFlux = webTestClient.get().uri("/api/product").exchange()
                .expectStatus().isOk()
                .returnResult(Product.class)
                .getResponseBody();

        StepVerifier.create(productFlux.log("Products From API :"))
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void getProductByID(){
        webTestClient.get().uri("/api/product/ABC")
                                    .exchange()
                                    .expectStatus().isOk()
                                    .expectBody()
                                    .jsonPath("$.price",1200.25);
    }

    @Test
    public void getProductByID_NotFound(){
        webTestClient.get().uri("/api/product/ABC11")
                .exchange()
                .expectStatus().isNotFound();
                //.expectBody()
                //.jsonPath("$.price",1200.25);
    }

    @Test
    public void  createProduct(){
        Product product = new Product(null,"test",2500.99,"testing product");

        webTestClient.post().uri("/api/product").body(Mono.just(product),Product.class)
                .exchange()
                .expectStatus().isCreated();

    }
    @Test
    public void deleteProduct(){
        webTestClient.delete().uri("/api/product/ABC")
                .exchange().expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void updateProduct(){
        Product product = new Product(null,"hello",2900.22,"test update");
        webTestClient.put().uri("/api/product/ABC").body(Mono.just(product),Product.class)
                .exchange().expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price",2900.22);
    }
    @Test
    public void updateProduct_INVALID_ID(){
        Product product = new Product(null,"hello",2900.22,"test update");
        webTestClient.put().uri("/api/product/ABCD").body(Mono.just(product),Product.class)
                .exchange().expectStatus().isNotFound();
    }
}
