package com.electronique.store.repository;

import com.electronique.store.document.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    List<Product> products = Arrays.asList(new Product(null,"Dell",2500.00,"Dell laptop"),
                                           new Product(null,"Hp",2300.00,"Hp laptop"),
                                           new Product("ABC","Lenovo",1200.25,"Lenovo laptop"));
    @Before
    public void setUp(){
        productRepository.deleteAll().
                thenMany(Flux.fromIterable(products))
                .flatMap(productRepository::save)
                .doOnNext(product -> System.out.println("product added : "+product))
                .blockLast();
    }

    @Test
    public void getAllProducts(){
        StepVerifier.create(productRepository.findAll().log())
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void getProdcutById(){
        StepVerifier.create(productRepository.findById("ABC"))
                .expectSubscription()
                .expectNextMatches(product -> product.getName().equalsIgnoreCase("Lenovo"))
                .verifyComplete();
    }

    @Test
    public void getProdcutByName(){
        StepVerifier.create(productRepository.findByName("Lenovo"))
                .expectSubscription()
                //.expectNextMatches(product -> product.getName().equalsIgnoreCase("Lenovo"))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveProdcut(){
        Mono<Product> productMono = productRepository.save(new Product(null,"Asus",2500.99,"asus the best"));
        StepVerifier.create(productMono.log("saved product :"))
                .expectSubscription()
                .expectNextMatches(product -> product.getName().equalsIgnoreCase("Asus"))
                //.expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void updateProdcut(){

        Double newProductPrice = 599.99;
        Flux<Product> productFlux = productRepository.findByName("Hp").
                                    map(product -> {
                                        product.setPrice(newProductPrice);
                                        return product;
                                    }).flatMap(productRepository::save);
        StepVerifier.create(productFlux.log("product price update :"))
                    .expectSubscription()
                    .expectNextMatches(product -> product.getPrice().equals(newProductPrice))
                    .verifyComplete();

    }

    @Test
    public void deleteProduct(){

        //productRepository.deleteById("ABC").log("deleting product :");

        Mono<Void> productMono = productRepository.findById("ABC")
                                    .map(Product::getId)
                                    .flatMap(id->productRepository.deleteById(id));

        StepVerifier.create(productMono.log("Deleted Product :"))
                .expectSubscription()
               // .expectNextCount(0)
                .verifyComplete();
    }
}
